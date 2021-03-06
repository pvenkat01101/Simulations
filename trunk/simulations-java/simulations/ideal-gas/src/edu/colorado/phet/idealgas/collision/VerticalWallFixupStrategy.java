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
 * VerticalWallFixupStrategy
 * <p/>
 * Keeps spheres from getting through a wall from left to right or right to left
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class VerticalWallFixupStrategy implements WallFixupStrategy {
    public void fixup( Wall wall, SphericalBody sphere ) {
        WallDescriptor wallDesc = new WallDescriptor( wall, sphere.getRadius() );
        double dAB = wallDesc.AB.ptLineDistSq( sphere.getPosition() );
        double dBC = wallDesc.BC.ptLineDistSq( sphere.getPosition() );
        double dCD = wallDesc.CD.ptLineDistSq( sphere.getPosition() );
        double dAD = wallDesc.AD.ptLineDistSq( sphere.getPosition() );

        if( dBC < dAD ) {
            sphere.setPosition( wallDesc.BC.getX1(), sphere.getPosition().getY() );
            sphere.setVelocity( Math.abs( sphere.getVelocity().getX() ), sphere.getVelocity().getY() );
        }
        else {
            sphere.setPosition( wallDesc.AD.getX1(), sphere.getPosition().getY() );
            sphere.setVelocity( -Math.abs( sphere.getVelocity().getX() ), sphere.getVelocity().getY() );
        }
    }
}
