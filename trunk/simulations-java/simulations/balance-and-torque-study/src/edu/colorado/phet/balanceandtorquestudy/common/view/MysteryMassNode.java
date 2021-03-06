// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.balanceandtorquestudy.common.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.RoundRectangle2D;

import edu.colorado.phet.balanceandtorquestudy.balancelab.view.ImageMassNode;
import edu.colorado.phet.balanceandtorquestudy.common.model.masses.MysteryMass;
import edu.colorado.phet.common.phetcommon.model.property.BooleanProperty;
import edu.colorado.phet.common.phetcommon.view.graphics.transforms.ModelViewTransform;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.common.piccolophet.PhetPCanvas;
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * This class represents a mystery mass in the view.
 *
 * @author John Blanco
 */
public class MysteryMassNode extends ImageMassNode {
    private static final double INSET_PROPORTION = 0.2;

    public MysteryMassNode( final ModelViewTransform mvt, final MysteryMass mass, PhetPCanvas canvas, BooleanProperty massLabelVisibilityProperty ) {
        super( mvt, mass, canvas, massLabelVisibilityProperty );
        double inset = getFullBoundsReference().width * INSET_PROPORTION;
        // Create the label.
        TestLabelNode label = new TestLabelNode( mass.getLabelText() );
        // Scale the label to fit.
        double widthScale = ( getFullBoundsReference().width - ( 2 * inset ) ) / label.getFullBoundsReference().width;
        double heightScale = ( getFullBoundsReference().height - ( 2 * inset ) ) / label.getFullBoundsReference().height;
        label.setScale( Math.min( widthScale, heightScale ) );
        // Position the label on the image.  TWEAK WARNING - These labels are
        // positioned a little below the center because it looked better on the
        // initial set of mystery masses.  May require adjustment if the
        // artwork for the mystery masses changes.
        label.centerFullBoundsOnPoint( imageNode.getFullBoundsReference().getCenterX(),
                                       imageNode.getFullBoundsReference().getCenterY() + imageNode.getFullBoundsReference().height * 0.1 );
        // Add the label as a child.
        addChild( label );
    }

    private static class TestLabelNode extends PNode {
        private static final Font FONT = new PhetFont( 14, true );

        private TestLabelNode( String text ) {
            PText textNode = new PText( text ) {{
                setFont( FONT );
                setTextPaint( Color.BLACK );
            }};
            double dimension = Math.max( textNode.getFullBoundsReference().width,
                                         textNode.getFullBoundsReference().height );
            RoundRectangle2D.Double backgroundShape = new RoundRectangle2D.Double( 0, 0, dimension, dimension, 3, 3 );
            PNode backgroundNode = new PhetPPath( backgroundShape,
                                                  Color.WHITE,
                                                  new BasicStroke( 0.5f ),
                                                  Color.BLACK );
            addChild( backgroundNode );
            textNode.centerFullBoundsOnPoint( backgroundShape.getCenterX(), backgroundShape.getCenterY() );
            addChild( textNode );
        }
    }
}
