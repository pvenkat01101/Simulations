// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.lasers.model.mirror;

import edu.colorado.phet.common.quantum.model.Photon;

/**
 * A ReflectionStrategy that reflects to the left. That is, it reflects
 * photons that are traveling to the right.
 */
public class LeftReflecting implements ReflectionStrategy {

    public boolean reflects( Photon photon ) {
        return photon.getVelocity().getX() > 0;
    }
}
