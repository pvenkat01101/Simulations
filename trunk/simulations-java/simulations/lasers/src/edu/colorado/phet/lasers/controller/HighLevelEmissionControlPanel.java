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
 * HighLevelEmissionControlPanel
 * <p/>
 * Provide user control over whether photons are shown when an atom in the high energy state
 * drops to the middle energy state
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class HighLevelEmissionControlPanel extends JPanel {

    public HighLevelEmissionControlPanel( final BaseLaserModule module ) {
        final JCheckBox displayHighLevelEmissionsCB = new JCheckBox( LasersResources.getString( "OptionsControlPanel.DisplayUpperStatePhotons" ) );
        displayHighLevelEmissionsCB.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                module.setDisplayHighLevelEmissions( displayHighLevelEmissionsCB.isSelected() );
            }
        } );
        this.setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints( 0, GridBagConstraints.RELATIVE,
                                                         1, 1, 1, 1,
                                                         GridBagConstraints.NORTHWEST,
                                                         GridBagConstraints.NONE,
                                                         new Insets( 0, 0, 0, 0 ), 0, 0 );
        this.add( displayHighLevelEmissionsCB, gbc );
    }
}
