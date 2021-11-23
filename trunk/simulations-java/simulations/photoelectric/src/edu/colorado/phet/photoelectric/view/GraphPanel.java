// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.photoelectric.view;

import edu.colorado.phet.common.phetcommon.model.clock.IClock;
import edu.colorado.phet.common.phetgraphics.view.ApparatusPanel2;

import java.awt.*;

/**
 * GraphPanel
 * <p/>
 * An ApparatusPanel2 that contains a PhotoelectricGraph instance, for putting in Swing
 * Containters
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class GraphPanel extends ApparatusPanel2 {
    private PhotoelectricGraph graph;

    public GraphPanel( IClock clock ) {
        super( clock );
        setUseOffscreenBuffer( true );
        setDisplayBorder( false );
    }

    public void setGraph( PhotoelectricGraph graph, Insets insets ) {
        setPreferredSize( new Dimension( (int)graph.getChartSize().getWidth() + insets.left + insets.right,
                                         (int)graph.getChartSize().getHeight() + insets.top + insets.bottom ) );
        graph.setLocation( insets.left, insets.top );
        addGraphic( graph );
        this.graph = graph;
    }

    public void clearGraph() {
        graph.clearData();
    }

    public PhotoelectricGraph getGraph() {
        return graph;
    }
}
