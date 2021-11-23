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

import edu.colorado.phet.common.quantum.model.Atom;
import edu.colorado.phet.common.quantum.model.AtomicState;
import edu.colorado.phet.common.quantum.model.EnergyEmissionStrategy;

/**
 * NextLowestEnergyEmissionStrategy
 * <p/>
 * Always sets the atom to the next lower state down from its current state
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class NextLowestEnergyEmissionStrategy implements EnergyEmissionStrategy {

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
        return states[Math.max( currStateIdx - 1, 0 )];
    }
}
