// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.movingman.view;

import java.text.DecimalFormat;
import java.text.MessageFormat;

import edu.colorado.phet.common.phetcommon.util.SimpleObserver;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.movingman.MovingManStrings;
import edu.colorado.phet.movingman.model.ObservableDouble;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * @author Sam Reid
 */
public class TimeReadout extends PNode {
    public TimeReadout( final ObservableDouble timeProperty ) {
        final PText text = new PText();
        final DecimalFormat decimalFormat = new DecimalFormat( "0.0" );//using 0.0 precision masks the problem that time resets to 0.04 instead of 0.00
        text.setFont( new PhetFont( 24, true ) );
        addChild( text );
        setPickable( false );
        setChildrenPickable( false );
        final SimpleObserver updateReadout = new SimpleObserver() {
            public void update() {
                text.setText( MessageFormat.format( MovingManStrings.TIME_LABEL_PATTERN, decimalFormat.format( timeProperty.getValue() ), MovingManStrings.SECONDS ) );
            }
        };
        timeProperty.addObserver( updateReadout );
        updateReadout.update();
    }
}