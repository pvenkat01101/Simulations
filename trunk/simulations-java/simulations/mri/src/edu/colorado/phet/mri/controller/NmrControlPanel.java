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
import edu.colorado.phet.mri.model.MriModel;
import edu.colorado.phet.mri.view.MonitorPanel;
import edu.colorado.phet.mri.view.MriLegend;

import javax.swing.*;
import java.awt.*;

/**
 * MriControlPanel
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class NmrControlPanel extends ControlPanel {

    /**
     * Constructor
     *
     * @param module
     */
    public NmrControlPanel( AbstractMriModule module ) {
        MriModel model = (MriModel)module.getModel();

        MonitorPanel monitorPanel = new MonitorPanel( model );
        monitorPanel.setPreferredSize( new Dimension( 200, 200 ) );
        JPanel monitorPanelWrapper = new JPanel();
        monitorPanelWrapper.add( monitorPanel );

        addControlFullWidth( new MriLegend() );
        addControlFullWidth( monitorPanelWrapper );
//        addControlFullWidth( monitorPanel );
        addControlFullWidth( new FadingMagnetControl( model ) );
        addControlFullWidth( new SampleMaterialSelector( model ) );
    }
}
