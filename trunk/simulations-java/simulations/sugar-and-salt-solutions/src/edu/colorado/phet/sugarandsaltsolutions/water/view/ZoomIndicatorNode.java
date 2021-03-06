// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.water.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import edu.colorado.phet.common.phetcommon.model.property.ObservableProperty;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.umd.cs.piccolo.PNode;

/**
 * This node draws a box around a small rectangle in the mini beaker, with lines to the ParticleWindowNode to indicate that is the magnified region.
 *
 * @author Sam Reid
 */
public class ZoomIndicatorNode extends PNode {
    public ZoomIndicatorNode( final ObservableProperty<Color> lineColor, MiniBeakerNode miniBeakerNode, ParticleWindowNode particleWindowNode ) {

        //Get the bounds of the relevant regions
        Rectangle2D miniBeakerBounds = miniBeakerNode.getFullBounds();
        Rectangle2D particleWindowBounds = particleWindowNode.getFullBounds();

        //Invisible rectangle that the zoom lines will be pointing at
        double size = 3;
        Rectangle2D zoomRect = new Rectangle2D.Double( miniBeakerBounds.getCenterX() - size / 2, ( miniBeakerBounds.getCenterY() + miniBeakerBounds.getMaxY() ) / 2 - size / 2, size, size );

        //Draw lines from the zoomed in box to the particle box
        //Make it wide enough to be seen on a projector in a lit classroom
        Stroke zoomLineStroke = new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] { 20, 10 }, 0 );
        addChild( new PhetPPath( new Line2D.Double( zoomRect.getCenterX(), zoomRect.getY(), particleWindowBounds.getMaxX(), particleWindowBounds.getY() ), zoomLineStroke, lineColor.get() ) {{
            lineColor.addObserver( new VoidFunction1<Color>() {
                public void apply( Color color ) {
                    setStrokePaint( color );
                }
            } );
        }} );
        addChild( new PhetPPath( new Line2D.Double( zoomRect.getCenterX(), zoomRect.getMaxY(), particleWindowBounds.getMaxX(), particleWindowBounds.getMaxY() ), zoomLineStroke, lineColor.get() ) {{
            lineColor.addObserver( new VoidFunction1<Color>() {
                public void apply( Color color ) {
                    setStrokePaint( color );
                }
            } );
        }} );
    }
}