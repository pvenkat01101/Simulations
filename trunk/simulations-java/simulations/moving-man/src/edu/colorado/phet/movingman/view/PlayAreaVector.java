// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.movingman.view;

import java.awt.*;
import java.awt.geom.Point2D;

import edu.colorado.phet.common.phetcommon.view.graphics.Arrow;
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.umd.cs.piccolo.PNode;

/**
 * @author Sam Reid
 */
public class PlayAreaVector extends PNode {
    private final PhetPPath path;
    private final double tailWidth;
    private double headWidth;

    public PlayAreaVector( Color color, double tailWidth ) {
        this.tailWidth = tailWidth;
        this.headWidth = tailWidth * 2;
        path = new PhetPPath( color, new BasicStroke( 1 ), Color.black );
        addChild( path );
    }

    public void setArrow( double x, double y, double x2, double y2 ) {
        if ( Math.abs( x - x2 ) < 1 ) {//Don't try to show arrows less than one pixel long
            path.setPathTo( new Rectangle( 0, 0, 0, 0 ) );
        }
        else {
            path.setPathTo( new Arrow( new Point2D.Double( x, y ), new Point2D.Double( x2, y2 ), headWidth, headWidth, tailWidth,
                                       1 / 2.0, //so that the arrow shrinks proportionately
                                       false ).getShape() );
        }
    }
}
