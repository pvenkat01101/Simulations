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

import edu.colorado.phet.mri.model.Electromagnet;
import edu.colorado.phet.mri.model.MriModel;
import edu.colorado.phet.mri.model.RadiowaveSource;
import edu.colorado.phet.mri.model.SampleTarget;
import edu.colorado.phet.mri.view.SampleTargetGraphic;

/**
 * ScanModule
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class ScanModule extends HeadModule {

    public ScanModule() {
        super( "Scanner I" );

//        SampleScanner sampleScanner = new SampleScanner( getHead() );
//        model.addModelElement( sampleScanner);
//        SampleTargetGraphic sampleTargetGraphic = new SampleTargetGraphic( sampleScanner.getSampleTarget() );
//        getGraphicsManager().addGraphic( sampleTargetGraphic );
//
//        ComputedImageWindow ciw = new ComputedImageWindow( new Dimension(  (int)getHead().getBounds().getWidth(),
//                                                                           (int)getHead().getBounds().getHeight()));
//        ciw.setVisible( true );
    }

    protected void init() {
        super.init();

        System.out.println( "ScanModule.init" );
        MriModel model = (MriModel)getModel();
        RadiowaveSource radioSource = model.getRadiowaveSource();
        radioSource.setFrequency( 42E6 );
        Electromagnet magnet = model.getLowerMagnet();
        magnet.setFieldStrength( 2200 );
//        magnet.setCurrent( 33 );
        magnet = model.getUpperMagnet();
        magnet.setFieldStrength( 2200 );
//        magnet.setCurrent( 33 );


        SampleTarget sampleTarget = new SampleTarget();
        sampleTarget.setLocation( 300, 300 );
        SampleTargetGraphic sampleTargetGraphic = new SampleTargetGraphic( sampleTarget );
        getGraphicsManager().addGraphic( sampleTargetGraphic );

        sampleTarget.addChangeListener( new SampleTargetModelConfigurator( (MriModel)getModel() ) );
    }
}
