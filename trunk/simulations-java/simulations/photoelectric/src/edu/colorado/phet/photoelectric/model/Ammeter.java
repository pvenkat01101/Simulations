// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.photoelectric.model;

import edu.colorado.phet.common.phetcommon.model.clock.IClock;
import edu.colorado.phet.photoelectric.model.util.ScalarDataRecorder;

/**
 * Ammeter
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class Ammeter extends ScalarDataRecorder {

    private int clientUpdateInterval = 500;
    private int simulationTimeWindow = 1000;

    public Ammeter( IClock clock ) {
//    public Ammeter( AbstractClock clock ) {
        super( clock );
        super.setTimeWindow( simulationTimeWindow );
        super.setClientUpdateInterval( clientUpdateInterval );
    }

    public double getCurrent() {
        computeDataStatistics();
        double current = getDataTotal() / getTimeSpanOfEntries();
        if( Double.isNaN( current ) || Double.isInfinite( current ) ) {
            current = 0;
        }
        return current;
    }

    public void recordElectron() {
        recordElectrons( 1 );
    }

    public void recordElectrons( int numElectrons ) {
        addDataRecordEntry( numElectrons );
    }

    public int getSimulationTimeWindow() {
        return simulationTimeWindow;
    }
}
