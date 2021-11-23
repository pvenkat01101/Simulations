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

import edu.colorado.phet.common.phetcommon.view.ControlPanel;
import edu.colorado.phet.mri.model.GradientElectromagnet;
import edu.colorado.phet.mri.model.MriModel;
import edu.colorado.phet.mri.view.GradientMagnetControlPanel;
import edu.colorado.phet.mri.view.MriLegend;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * MriControlPanel
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class HeadModuleControlPanel extends ControlPanel {

    /**
     * Constructor
     *
     * @param module
     */
    public HeadModuleControlPanel( HeadModule module,
                                   GradientElectromagnet horizontalGradientMagnet,
                                   GradientElectromagnet verticalGradientMagnet ) {
        MriModel model = (MriModel)module.getModel();

        addControlFullWidth( new MriLegend() );
        addControlFullWidth( new FadingMagnetControl( model ) );
        addControlFullWidth( new GradientMagnetControlPanel( horizontalGradientMagnet, verticalGradientMagnet ) );
        addControlFullWidth( new HeadControl( module ) );

        addComponentListener( new ComponentAdapter() {
            public void componentResized( ComponentEvent e ) {
//                getLayout().layoutContainer( PhetUtilities.getPhetFrame().getContentPane() );
            }
        } );
    }
}
