// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 54200 $
 * Date modified : $Date: 2011-07-19 06:15:40 +0530 (Tue, 19 Jul 2011) $
 */
package edu.colorado.phet.common.quantum.model;

import java.awt.Shape;
import java.util.EventListener;
import java.util.EventObject;

/**
 * PhotonSource
 *
 * @author Ron LeMaster
 * @version $Revision: 54200 $
 */
public interface PhotonSource {

    Shape getBounds();

    double getPhotonsPerSecond();

    public double getMaxPhotonsPerSecond();

    double getWavelength();

    void addRateChangeListener( RateChangeListener rateChangeListener );

    void addWavelengthChangeListener( WavelengthChangeListener wavelengthChangeListener );

    void addPhotonEmittedListener( PhotonEmissionListener photonEmittedListener );

    void removeListener( EventListener listener );

    //----------------------------------------------------------------
    // Inner classes
    //----------------------------------------------------------------

    public class RateChangeEvent extends EventObject {
        public RateChangeEvent( PhotonSource source ) {
            super( source );
        }

        public double getRate() {
            return ( (PhotonSource) getSource() ).getPhotonsPerSecond();
        }
    }

    public interface RateChangeListener extends EventListener {
        public void rateChangeOccurred( RateChangeEvent event );
    }

    public class WavelengthChangeEvent extends EventObject {
        public WavelengthChangeEvent( PhotonSource source ) {
            super( source );
        }

        public double getWavelength() {
            return ( (PhotonSource) getSource() ).getWavelength();
        }
    }

    public interface WavelengthChangeListener extends EventListener {
        public void wavelengthChanged( WavelengthChangeEvent event );
    }
}
