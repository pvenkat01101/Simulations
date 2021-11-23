// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.dischargelamps.model;

import java.util.Random;

import edu.colorado.phet.common.quantum.model.Atom;
import edu.colorado.phet.common.quantum.model.AtomicState;
import edu.colorado.phet.common.quantum.model.EnergyEmissionStrategy;

/**
 * NextLowestEnergyEmissionStrategy
 * <p/>
 * Half the time, goes to the next energy level down. The other half of the time, it goes to
 * a random state lower than that
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class ConfigurableAtomEnergyEmissionStrategy implements EnergyEmissionStrategy {
    private static Random random = new Random();

    public AtomicState emitEnergy( Atom atom ) {
        AtomicState newState = null;
        AtomicState[] states = atom.getStates();
        // Find the current state
        int currStateIdx = 0;
        for ( int i = 0; i < states.length; i++ ) {
            AtomicState state = states[i];
            if ( state.equals( atom.getCurrState() ) ) {
                currStateIdx = i;
            }
        }
        int nextStateIdx;
        if ( random.nextBoolean() || currStateIdx == 1 ) {
            nextStateIdx = Math.max( currStateIdx - 1, 0 );
        }
        else {
            nextStateIdx = random.nextInt( Math.max( currStateIdx - 1, 0 ) );
        }
        return states[nextStateIdx];
    }
}
