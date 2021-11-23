// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47761 $
 * Date modified : $Date: 2011-01-07 23:19:12 +0530 (Fri, 07 Jan 2011) $
 */

package edu.colorado.phet.common.quantum.model;


/**
 *
 */
public class GroundState extends AtomicState {

    public GroundState() {
        setEnergyLevel( 0 );
        setMeanLifetime( Double.POSITIVE_INFINITY );
    }

    public AtomicState getNextLowerEnergyState() {
        return AtomicState.MinEnergyState.instance();
    }
}
