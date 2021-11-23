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

import javax.swing.ImageIcon;

import edu.colorado.phet.mri.MriConfig;
import edu.colorado.phet.mri.MriResources;

/**
 * DipoleIcon
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class DipoleIcon extends ImageIcon {

    public DipoleIcon() {
        super( MriResources.getImage( MriConfig.DIPOLE_IMAGE ) );
    }
}
