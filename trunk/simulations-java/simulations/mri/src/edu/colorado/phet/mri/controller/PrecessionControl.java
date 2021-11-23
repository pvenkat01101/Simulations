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
import edu.colorado.phet.mri.model.Dipole;
import edu.colorado.phet.mri.model.MriModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;

/**
 * PrecessionControl
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class PrecessionControl extends ModelSlider {
    public PrecessionControl( final MriModel model ) {
        super( "Max Precession", "rad", 0, Math.PI, MriConfig.InitialConditions.DIPOLE_PRECESSION );

        addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                List dipoles = model.getDipoles();
                for( int i = 0; i < dipoles.size(); i++ ) {
                    Dipole dipole = (Dipole)dipoles.get( i );
                    dipole.setPrecession( getValue() );
                }
            }
        } );
    }
}
