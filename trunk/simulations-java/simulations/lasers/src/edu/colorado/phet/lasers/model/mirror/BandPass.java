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
 * A ReflectionStrategy that reflects photons whose wavelengths
 * are between two cutoff points. Probably misnamed. It's really
 * more of a notch.
 */
public class BandPass implements ReflectionStrategy {

    private double cutoffLow;
    private double cutoffHigh;

    public BandPass( double cutoffLow, double cutoffHigh ) {
        this.cutoffLow = cutoffLow;
        this.cutoffHigh = cutoffHigh;
    }

    public boolean reflects( Photon photon ) {
        return ( photon.getWavelength() >= cutoffLow
                 && photon.getWavelength() <= cutoffHigh );
    }
}
