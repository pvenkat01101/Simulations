// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.lasers.model.atom;

import edu.colorado.phet.common.quantum.model.AtomicState;
import edu.colorado.phet.common.quantum.model.ElementProperties;
import edu.colorado.phet.common.quantum.model.EnergyEmissionStrategy;
import edu.colorado.phet.lasers.LasersConfig;

/**
 * LaserElementProperties
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public abstract class LaserElementProperties extends ElementProperties {

    protected LaserElementProperties( String name, double[] energyLevels,
                                      EnergyEmissionStrategy energyEmissionStrategy,
                                      double meanStateLifetime ) {
        super( name, energyLevels, energyEmissionStrategy, meanStateLifetime );

        // Set the mean lifetimes of the states
        AtomicState[] states = getStates();
        for ( int i = 1; i < states.length; i++ ) {
            AtomicState state = states[i];
            state.setMeanLifetime( LasersConfig.MAXIMUM_STATE_LIFETIME / 2 );
        }

    }

//    public AtomicState getGroundState() {
//        return getStates()[0];
//    }

    //

    public AtomicState getMiddleEnergyState() {
        return getStates()[1];
    }

    abstract public AtomicState getHighEnergyState();
}
