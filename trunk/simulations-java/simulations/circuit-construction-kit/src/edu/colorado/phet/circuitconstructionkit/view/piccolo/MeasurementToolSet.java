// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 52720 $
 * Date modified : $Date: 2011-06-07 18:13:00 +0530 (Tue, 07 Jun 2011) $
 */
package edu.colorado.phet.circuitconstructionkit.view.piccolo;

import edu.colorado.phet.circuitconstructionkit.model.CCKModel;

/**
 * MeasurementToolSet
 *
 * @author Ron LeMaster
 * @version $Revision: 52720 $
 */
public class MeasurementToolSet {
    private VoltmeterModel voltmeterModel;

    public MeasurementToolSet( CCKModel model ) {
        this.voltmeterModel = new VoltmeterModel( model, model.getCircuit() );
    }

    public VoltmeterModel getVoltmeterModel() {
        return voltmeterModel;
    }

}
