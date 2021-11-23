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

import edu.colorado.phet.common.phetcommon.util.PhysicsUtil;
import edu.colorado.phet.mri.model.MriModel;
import edu.colorado.phet.mri.model.SampleTarget;

/**
 * SampleTargetModelConfigurator
 * <p/>
 * Sets the model parameters to resonate for the sample material at a SampleTarget point
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class SampleTargetModelConfigurator implements SampleTarget.ChangeListener {
    MriModel model;

    public SampleTargetModelConfigurator( MriModel model ) {
        this.model = model;
    }

    private void setRadioFrequency( SampleTarget sampleTarget ) {
        double fieldStrength = model.getTotalFieldStrengthAt( sampleTarget.getLocation() ) * model.getSampleMaterial().getMu();
        double hEnergy = PhysicsUtil.frequencyToEnergy( fieldStrength );
        double frequency = PhysicsUtil.energyToFrequency( hEnergy );
        model.getRadiowaveSource().setFrequency( frequency );
    }

    public void stateChanged( SampleTarget.ChangeEvent event ) {
        setRadioFrequency( event.getSampleTarget() );
    }
}
