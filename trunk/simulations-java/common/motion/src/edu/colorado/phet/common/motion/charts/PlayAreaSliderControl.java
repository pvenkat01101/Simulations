// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.common.motion.charts;

import java.awt.Color;
import java.io.IOException;

import edu.colorado.phet.common.phetcommon.util.DefaultDecimalFormat;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.common.piccolophet.nodes.HTMLNode;
import edu.colorado.phet.common.piccolophet.nodes.ShadowPText;
import edu.umd.cs.piccolo.PNode;

/**
 * These sliders should be Piccolo sliders for the following reasons:
 * 1. So we can color the thumbs
 * 2. So we can indicate when the value is out of range
 * 3. So we can easily identify when a change event is caused by the user vs. by a callback from the model.
 *
 * @author Sam Reid
 */
public class PlayAreaSliderControl extends PNode {
    private MotionSliderNode slider;
    private TextBox textBox;
    final DefaultDecimalFormat decimalFormat = new DefaultDecimalFormat( "0.00" );

    public PlayAreaSliderControl( double min, double max, double value, String title, String units, Color color, TextBox textBox ) {
        this.textBox = textBox;
        ShadowPText text = new ShadowPText( title );
        text.setTextPaint( color );
        text.setFont( new PhetFont( 20, true ) );
        addChild( text );
        addChild( textBox );
        textBox.setOffset( 200, text.getFullBounds().getCenterY() - textBox.getFullBounds().getHeight() / 2 );//todo: align with other controls but make sure doesn't overlap text

        try {
            slider = new MotionSliderNode.Horizontal( new Range( min, max ), 0.0, new Range( 0, 350 ), color );
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        addChild( slider );
        slider.setOffset( 0, text.getFullBounds().getHeight() + 10 );

        HTMLNode unitsPText = new HTMLNode( units );//Use HTMLNode for support for provided translations, which use HTML
        unitsPText.setHTMLColor( color );

        unitsPText.setFont( new PhetFont( 20, true ) );
        addChild( unitsPText );
        unitsPText.setOffset( textBox.getFullBounds().getMaxX() + 2, 0 );

        setValue( value );
    }

    public void setValue( double value ) {
        slider.setValue( value );
        textBox.setText( decimalFormat.format( value ) );
    }

    public void addListener( MotionSliderNode.Listener listener ) {
        slider.addListener( listener );
    }

    public double getValue() {
        return slider.getValue();
    }

    public void setHighlighted( boolean positionDriven ) {
        slider.setHighlighted( positionDriven );
    }
}
