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
 * FloorFixupStrategy
 * <p/>
 * Keeps spheres from getting through a wall from top to bottom
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class FloorFixupStrategy implements WallFixupStrategy {

    public void fixup( Wall wall, SphericalBody sphere ) {
        sphere.setVelocity( sphere.getVelocity().getX(), -Math.abs( sphere.getVelocity().getY() ) );
        sphere.setPosition( sphere.getPosition().getX(), wall.getBounds().getMinY() - sphere.getRadius() );
    }
}
