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
import edu.colorado.phet.common.quantum.model.GroundState;

/**
 * DefaultEnergyEmissionStrategy
 * <p/>
 * Always sets the atom to the ground state
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class DefaultEnergyEmissionStrategy implements EnergyEmissionStrategy {

    public AtomicState emitEnergy( Atom atom ) {
        AtomicState newState = null;
        AtomicState[] states = atom.getStates();
        for ( int i = 0; i < states.length; i++ ) {
            AtomicState state = states[i];
            if ( state instanceof GroundState ) {
                newState = state;
            }
        }
        return newState;
    }
}
