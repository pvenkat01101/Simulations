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

import javax.swing.JPanel;

import edu.colorado.phet.mri.MriResources;
import edu.colorado.phet.mri.model.MriModel;
import edu.colorado.phet.mri.util.ControlBorderFactory;

/**
 * BFieldGraphicPanel
 * <p/>
 * Displays a PFieldArrowGraphic in a JPanel
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class BFieldGraphicPanel extends JPanel {

    public BFieldGraphicPanel( MriModel model ) {
        final BFieldArrowGraphic fieldGraphic = new BFieldArrowGraphic( model, 0 );
        setBorder( ControlBorderFactory.createPrimaryBorder( MriResources.getString( "ControlPanel.FieldArrowTitle" ) ) );
        add( fieldGraphic );
    }
}
