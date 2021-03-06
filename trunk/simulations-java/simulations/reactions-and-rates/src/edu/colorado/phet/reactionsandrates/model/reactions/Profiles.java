// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.model.reactions;

import edu.colorado.phet.reactionsandrates.MRConfig;
import edu.colorado.phet.reactionsandrates.model.EnergyProfile;

/**
 * Profiles
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class Profiles {

    public static final EnergyProfile DEFAULT = MRConfig.DEFAULT_ENERGY_PROFILE;

    public static final EnergyProfile R1 = new EnergyProfile( MRConfig.DEFAULT_REACTION_THRESHOLD * .1,
                                                              MRConfig.DEFAULT_REACTION_THRESHOLD,
                                                              MRConfig.DEFAULT_REACTION_THRESHOLD * .1,
                                                              100 );

    public static final EnergyProfile R2 = new EnergyProfile( MRConfig.DEFAULT_REACTION_THRESHOLD * .1,
                                                              MRConfig.DEFAULT_REACTION_THRESHOLD * .7,
                                                              MRConfig.DEFAULT_REACTION_THRESHOLD * .4,
                                                              100 );

    public static final EnergyProfile R3 = new EnergyProfile( MRConfig.DEFAULT_REACTION_THRESHOLD * .7,
                                                              MRConfig.DEFAULT_REACTION_THRESHOLD * .7,
                                                              MRConfig.DEFAULT_REACTION_THRESHOLD * .1,
                                                              100 );

    public static final EnergyProfile DYO = new EnergyProfile( MRConfig.DEFAULT_ENERGY_PROFILE );
}
