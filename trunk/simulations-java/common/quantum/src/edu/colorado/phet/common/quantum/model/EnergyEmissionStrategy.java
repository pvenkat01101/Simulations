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
 * EnergyAbsorptionStrategy
 *
 * @author Ron LeMaster
 * @version $Revision: 47761 $
 */
public interface EnergyEmissionStrategy {
    public AtomicState emitEnergy( Atom atom );
}
