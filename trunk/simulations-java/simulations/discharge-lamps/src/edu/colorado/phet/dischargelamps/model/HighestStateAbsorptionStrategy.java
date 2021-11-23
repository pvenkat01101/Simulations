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
import edu.colorado.phet.dischargelamps.quantum.model.Electron;

/**
 * HighestStateAbsorptionStrategy
 * <p/>
 * Elevates the atom to the highest possible state to which the electron can raise it
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class HighestStateAbsorptionStrategy extends EnergyAbsorptionStrategy {

    Random random = new Random();

    /**
     * @param electron
     */
    public void collideWithElectron( Atom atom, Electron electron ) {
        AtomicState[] states = atom.getStates();
        AtomicState currState = atom.getCurrState();
        double electronEnergy = getElectronEnergyAtCollision( (DischargeLampAtom) atom, electron );

        // Find the index of the current state
        int currStateIdx = 0;
        for ( ; currStateIdx < states.length; currStateIdx++ ) {
            if ( states[currStateIdx] == currState ) {
                break;
            }
        }

        // Find the index of the highest energy state whose energy is not higher than that of the current state
        // by more than the energy of the electron
        int highestPossibleNewStateIdx = currStateIdx + 1;
        for ( ; highestPossibleNewStateIdx < states.length; highestPossibleNewStateIdx++ ) {
            if ( states[highestPossibleNewStateIdx].getEnergyLevel() - currState.getEnergyLevel() > electronEnergy ) {
                break;
            }
        }
        highestPossibleNewStateIdx--;
        AtomicState newState = states[highestPossibleNewStateIdx];
        double energyDiff = newState.getEnergyLevel() - currState.getEnergyLevel();
        atom.setCurrState( newState );
        electron.setEnergy( electronEnergy - energyDiff );

        // Randomize the direction of the electron's travel to give it more of a look of having collided
        // with the atom
        ElectronRedirector.setElectronDirection( electron );
    }
}
