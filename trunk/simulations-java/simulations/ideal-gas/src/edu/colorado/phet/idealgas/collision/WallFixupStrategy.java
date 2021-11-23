// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.idealgas.collision;

/**
 * WallFixupStrategy
 * <p/>
 * A way to fixup problems with a wall and sphere collision. Intended to be used as a last-ditch
 * effort to fix problems that can't be addressed through physically accurate model features.
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public interface WallFixupStrategy {
    void fixup( Wall wall, SphericalBody sphere );
}
