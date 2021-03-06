// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.fractionsintro.intro.view;

import java.awt.Font;

import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.colorado.phet.common.piccolophet.nodes.kit.ZeroOffsetNode;
import edu.colorado.phet.fractions.buildafraction.view.BuildAFractionCanvas;
import edu.colorado.phet.fractions.common.math.Fraction;
import edu.umd.cs.piccolo.PNode;

/**
 * Node that shows a fraction (numerator and denominator and dividing line) along with controls to change the values.
 * Layout is not normalized (top left is not 0,0), so it must be wrapped in a ZeroOffsetNode to position accurately.
 *
 * @author Sam Reid
 */
public class FractionNode extends PNode {

    public FractionNode( Fraction fraction, double scale ) {
        this( fraction.numerator, fraction.denominator );
        setScale( scale );
    }

    //Convenience constructor for a constant FractionNode
    private FractionNode( int numerator, int denominator ) {
        this( FractionNumberNode.DEFAULT_NUMBER_FONT, Property.property( numerator ), Property.property( denominator ) );
    }

    public FractionNode( Font font, final Property<Integer> numerator, final Property<Integer> denominator ) {
        final RoundedDivisorLine line = new RoundedDivisorLine();
        addChild( line );
        final ZeroOffsetNode num = new ZeroOffsetNode( new FractionNumberNode( font, numerator ) ) {{
            setOffset( line.getFullBounds().getCenterX() - getFullBounds().getWidth() / 2, line.getFullBounds().getY() - getFullBounds().getHeight() );
        }};
        addChild( num );
        final ZeroOffsetNode den = new ZeroOffsetNode( new FractionNumberNode( font, denominator ) ) {{
            setOffset( line.getFullBounds().getCenterX() - getFullBounds().getWidth() / 2, line.getFullBounds().getY() );
        }};
        addChild( den );

        //Make the entire region grabbable
        final PhetPPath background = new PhetPPath( getFullBounds(), BuildAFractionCanvas.TRANSPARENT );
        addChild( background );
        background.moveToBack();
    }
}