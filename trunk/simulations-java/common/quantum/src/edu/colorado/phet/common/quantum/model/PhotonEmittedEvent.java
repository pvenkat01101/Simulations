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

import java.util.EventObject;

/**
 * PhotonEmittedEvent
 */
public class PhotonEmittedEvent extends EventObject {
    private Photon photon;

    public PhotonEmittedEvent( Object source, Photon photon ) {
        super( source );
        this.photon = photon;
    }

    public Photon getPhoton() {
        return photon;
    }
}
