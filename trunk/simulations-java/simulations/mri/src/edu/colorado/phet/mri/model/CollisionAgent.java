// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.model;

import edu.colorado.phet.common.phetcommon.model.ModelElement;
import edu.colorado.phet.common.quantum.model.Photon;

import java.util.ArrayList;
import java.util.List;

/**
 * CollisionAgent
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class CollisionAgent implements ModelElement {
    private MriModel model;
    private List collisionExperts = new ArrayList();
    private PhotonDipoleCollisionAgent photonDipoleCollisonCollisionAgent;

    public CollisionAgent( MriModel model ) {
        this.model = model;

        photonDipoleCollisonCollisionAgent = new PhotonDipoleCollisionAgent( model );
    }

    public void stepInTime( double dt ) {

        List dipoles = model.getDipoles();
        List photons = model.getPhotons();
        for( int j = photons.size() - 1; j >= 0; j-- ) {
            for( int i = 0; i < dipoles.size(); i++ ) {
                Dipole dipole = (Dipole)dipoles.get( i );
                Photon photon = (Photon)photons.get( j );
                photonDipoleCollisonCollisionAgent.detectAndDoCollision( dipole, photon );
            }
        }
    }
}
