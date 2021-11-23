// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.view.charts;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * StripChartAdjuster
 * <p/>
 * Adjusts the visible range of a StripChart. Used in conjunction with
 * a control like a JScrollBar
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class StripChartAdjuster implements AdjustmentListener {
    private StripChart stripChart;

    public StripChartAdjuster( StripChart stripChart ) {
        this.stripChart = stripChart;
    }

    public void adjustmentValueChanged( AdjustmentEvent e ) {
        int value = Math.max( e.getValue(), 0 );
        stripChart.setMinX( value );
    }
}
