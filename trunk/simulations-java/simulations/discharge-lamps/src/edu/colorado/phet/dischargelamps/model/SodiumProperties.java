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

import edu.colorado.phet.dischargelamps.DischargeLampsResources;

/**
 * SodiumProperties
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class SodiumProperties extends DischargeLampElementProperties {
    private static double[] energyLevels = {
            -5.14,
            -3.03,
            -1.95,
            -1.52,
            -1.39,
            -1.02
    };

    // Likelihoods of emission transitions from one
    // state to another
    static TransitionEntry[] teA = new TransitionEntry[]{
            new TransitionEntry( 4, 0, 0.05 ),
            new TransitionEntry( 5, 1, 0.24 ),
            new TransitionEntry( 1, 0, 1.23 ),
            new TransitionEntry( 5, 1, 0.07 ),
            new TransitionEntry( 3, 1, 1.03 ),
            new TransitionEntry( 2, 1, 0.26 ),
            new TransitionEntry( 5, 3, 0.15 ),
            new TransitionEntry( 4, 2, 0.13 ),
            new TransitionEntry( 5, 4, 0.13 ),
            new TransitionEntry( 0, 0, 1 ),
    };

    public SodiumProperties() {
        super( DischargeLampsResources.getString( "Element.sodium" ), energyLevels, teA );
    }
}
