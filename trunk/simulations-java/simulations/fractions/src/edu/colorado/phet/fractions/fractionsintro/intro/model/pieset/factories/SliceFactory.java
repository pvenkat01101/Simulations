// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.fractionsintro.intro.model.pieset.factories;

import fj.F;
import fj.data.List;
import lombok.Data;

import java.awt.Color;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.Bucket;
import edu.colorado.phet.common.phetcommon.util.functionaljava.FJUtils;
import edu.colorado.phet.common.phetcommon.view.Dimension2DDouble;
import edu.colorado.phet.fractions.common.util.Size2D;
import edu.colorado.phet.fractions.fractionsintro.intro.model.containerset.Container;
import edu.colorado.phet.fractions.fractionsintro.intro.model.containerset.ContainerSet;
import edu.colorado.phet.fractions.fractionsintro.intro.model.pieset.Pie;
import edu.colorado.phet.fractions.fractionsintro.intro.model.pieset.PieSet;
import edu.colorado.phet.fractions.fractionsintro.intro.model.pieset.Slice;

import static fj.data.List.iterableList;

/**
 * Immutable abstract factory class for creating slices, subclasses provide specific behavior/shapes/layout for that kind of object.
 *
 * @author Sam Reid
 */
public @Data abstract class SliceFactory {

    public static final Dimension2DDouble stageSize = new Dimension2DDouble( 1024, 768 );
    public final Color bucketColor = new Color( 136, 177, 240 );//A shade that looks good behind the green objects
    public final Bucket bucket;
    public final double yRange;

    //Slice color, only used for some shapes (not for pre-rendered 3d cakes)
    public final Color sliceColor;

    SliceFactory( double yRange, Vector2D bucketPosition, Size2D bucketSize, Color sliceColor ) {
        this.yRange = yRange;
        this.sliceColor = sliceColor;
        this.bucket = new Bucket( bucketPosition.x + 50, bucketPosition.y, bucketSize.toDimension2DDouble(), bucketColor, "" );
    }

    //Create some cells for the empty pies
    public List<Pie> createEmptyPies( final int numPies, final int denominator ) {
        ArrayList<Pie> pies = new ArrayList<Pie>() {{
            for ( int i = 0; i < numPies; i++ ) {
                ArrayList<Slice> cells = new ArrayList<Slice>();
                for ( int k = 0; k < denominator; k++ ) {
                    cells.add( createPieCell( numPies, i, k, denominator ) );
                }
                add( new Pie( iterableList( cells ) ) );
            }
        }};

        return iterableList( pies );
    }

    public abstract Slice createBucketSlice( int denominator, long randomSeed );

    public abstract Slice createPieCell( int max, int container, int cell, int denominator );

    public PieSet fromContainerSetState( ContainerSet containerSetState ) {
        //Use the same random seed each time so that the bucket slices don't jump around when the max changes
        final long randomSeed = 0L;

        final List<Pie> emptyPies = createEmptyPies( containerSetState.containers.length(), containerSetState.denominator );
        return new PieSet( containerSetState.denominator, emptyPies, createSlices( emptyPies, containerSetState, randomSeed ), this );
    }

    //Create movable slices (in pies) and also slices in the buckets
    private List<Slice> createSlices( final List<Pie> emptyPies, final ContainerSet containerSet, long randomSeed ) {
        ArrayList<Slice> all = new ArrayList<Slice>();
        for ( int i = 0; i < containerSet.containers.length(); i++ ) {
            Container c = containerSet.containers.index( i );
            for ( Integer cell : c.filledCells ) {
                all.add( emptyPies.index( i ).cells.index( cell ) );
            }
        }
        final int numSlicesForBucket = 10;
        return iterableList( all ).append( createSlicesForBucket( containerSet.denominator, numSlicesForBucket, randomSeed ) );
    }

    //Slices to put in the buckets
    public List<Slice> createSlicesForBucket( final int denominator, final int numSlices, final long randomSeed ) {
        final Random random = new Random( randomSeed );
        return iterableList( new ArrayList<Slice>() {{
            for ( int i = 0; i < numSlices; i++ ) {
                add( createBucketSlice( denominator, random.nextLong() ) );
            }
        }} );
    }

    public Vector2D getBucketCenter() {
        final double x = bucket.getHoleShape().getBounds2D().getCenterX() + bucket.getPosition().getX();
        final double y = -bucket.getHoleShape().getBounds2D().getCenterY() - bucket.getPosition().getY();
        return new Vector2D( x, y );
    }

    public Slice getDropTarget( final PieSet pieSet, final Slice s ) {
        if ( pieSet.getEmptyCells().length() == 0 ) { return null; }
        final Slice closestCell = pieSet.getEmptyCells().minimum( FJUtils.ord( new F<Slice, Double>() {
            @Override public Double f( final Slice slice ) {
                return slice.getCenter().distance( s.getCenter() );
            }
        } ) );

        //Only allow it if the shapes actually overlapped
        return closestCell != null && !( new Area( closestCell.getShape() ) {{
            intersect( new Area( s.getShape() ) );
        }}.isEmpty() ) ? closestCell : null;
    }
}