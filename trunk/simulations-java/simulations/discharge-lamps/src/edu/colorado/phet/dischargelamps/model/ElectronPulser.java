// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.dischargelamps.model;

import edu.colorado.phet.common.phetcommon.model.ModelElement;

/**
 * ElectronPulser
 * <p/>
 * Puts current into the circuit and fires an electron
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class ElectronPulser implements ModelElement {
    private double elapsedTime;
    private double duration = 100;
    private DischargeLampModel model;

    public ElectronPulser( DischargeLampModel model, double current ) {
        this.model = model;
        model.setCurrent( current );
    }

    public void stepInTime( double dt ) {
        elapsedTime += dt;
        if ( elapsedTime >= duration ) {
            model.setCurrent( 0 );
            model.removeModelElement( this );
            if ( model.getVoltage() > 0 ) {
                model.getLeftHandPlate().produceElectron();
            }
            else if ( model.getVoltage() < 0 ) {
                model.getRightHandPlate().produceElectron();
            }
        }
    }
}
