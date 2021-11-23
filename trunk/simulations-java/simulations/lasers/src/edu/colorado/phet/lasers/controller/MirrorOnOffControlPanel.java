// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.lasers.controller;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import edu.colorado.phet.lasers.LasersResources;
import edu.colorado.phet.lasers.controller.module.BaseLaserModule;

/**
 * MirrorOnOffControlPanel
 * <p/>
 * Provides a check box for enabling and disabling mirror on the cavity
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class MirrorOnOffControlPanel extends JPanel {

    public MirrorOnOffControlPanel( final BaseLaserModule module ) {

        final String addMirrorsStr = LasersResources.getString( "LaserControlPanel.AddMirrorsCheckBox" );
        final JCheckBox mirrorCB = new JCheckBox( addMirrorsStr );
        mirrorCB.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                if ( mirrorCB.isSelected() ) {
                    module.setMirrorsEnabled( true );
                }
                else {
                    module.setMirrorsEnabled( false );
                }
            }
        } );
        this.setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints( 0, GridBagConstraints.RELATIVE,
                                                         1, 1, 1, 1,
                                                         GridBagConstraints.CENTER,
                                                         GridBagConstraints.HORIZONTAL,
                                                         new Insets( 0, 0, 0, 0 ), 0, 0 );
        this.add( mirrorCB, gbc );
    }
}
