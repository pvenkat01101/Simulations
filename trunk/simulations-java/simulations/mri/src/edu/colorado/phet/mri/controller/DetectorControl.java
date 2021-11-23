// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.controller;

import edu.colorado.phet.common.phetcommon.view.ModelSlider;
import edu.colorado.phet.mri.MriConfig;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * PrecessionControl
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class DetectorControl extends ModelSlider {
    private static double maxPeriod = MriConfig.DETECTOR_DEFAULT_PERIOD * 2;

    public DetectorControl( final HeadModule module ) {
        super( "Detector period", "msec (sim)", 0, maxPeriod, MriConfig.DETECTOR_DEFAULT_PERIOD );

        addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                module.getDetector().setDetectingPeriod( getValue() );
            }
        } );
    }
}
