// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import edu.colorado.phet.mri.MriResources;
import edu.colorado.phet.mri.controller.GradientMagnetControl;
import edu.colorado.phet.mri.model.GradientElectromagnet;
import edu.colorado.phet.mri.util.ControlBorderFactory;

/**
 * GradientMagnetControlPanel
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class GradientMagnetControlPanel extends JPanel {

    public GradientMagnetControlPanel( GradientElectromagnet horizontalMagnet, GradientElectromagnet verticalMagnet ) {
        super( new GridLayout( 2, 1 ) );
        setBorder( ControlBorderFactory.createPrimaryBorder( MriResources.getString( "ControlPanel.GradientMagnets" ) ) );
        GradientMagnetControl horizontalControl = new GradientMagnetControl( horizontalMagnet,
                                                                             MriResources.getString( "ControlPanel.Horizontal" ) );
        GradientMagnetControl verticalControl = new GradientMagnetControl( verticalMagnet,
                                                                           MriResources.getString( "ControlPanel.Vertical" ) );
        add( horizontalControl );
        add( verticalControl );
    }
}
