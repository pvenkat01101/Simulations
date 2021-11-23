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
 * A strategy that specifies how a mirror reflects a photon that strikes a mirror
 * that uses this strategy.
 */
interface ReflectionStrategy {

    public boolean reflects( Photon photon );

}
