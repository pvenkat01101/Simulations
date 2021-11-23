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

import java.awt.Insets;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.colorado.phet.mri.MriConfig;
import edu.colorado.phet.mri.MriResources;
import edu.colorado.phet.mri.model.GradientElectromagnet;
import edu.colorado.phet.mri.util.ControlBorderFactory;
import edu.colorado.phet.mri.util.SliderControl;

/**
 * GradientMagnetControl
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class GradientMagnetControl extends SliderControl {
    public final static double VIEW_TO_MODEL_FACTOR = 10;

    public GradientMagnetControl( final GradientElectromagnet horizontalMagnet, String title ) {
        super( 0,
               0,
               MriConfig.MAX_GRADIENT_COIL_FIELD,
               0.02, 2, 2,
               MriResources.getString( "ControlPanel.MagneticField" ) + ":",
               MriResources.getString( "ControlPanel.Tesla" ),
               5,
               new Insets( 0, 0, 0, 0 )
        );

        setTextEditable( true );
        setBorder( ControlBorderFactory.createSecondaryBorder( title ) );
        addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                horizontalMagnet.setFieldStrength( getValue() * VIEW_TO_MODEL_FACTOR );
            }
        } );
        horizontalMagnet.setFieldStrength( 0 );
    }
}
