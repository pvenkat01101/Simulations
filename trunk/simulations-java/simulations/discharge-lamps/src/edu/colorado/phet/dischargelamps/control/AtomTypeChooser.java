// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.dischargelamps.control;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import edu.colorado.phet.common.quantum.model.ElementProperties;
import edu.colorado.phet.dischargelamps.DischargeLampsResources;
import edu.colorado.phet.dischargelamps.model.DischargeLampElementProperties;
import edu.colorado.phet.dischargelamps.model.DischargeLampModel;

/**
 * AtomTypeChooser
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class AtomTypeChooser extends JPanel {
    private GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1, 1,
                                                             GridBagConstraints.CENTER,
                                                             GridBagConstraints.NONE,
                                                             new Insets( 0, 10, 0, 10 ), 0, 0 );

    public AtomTypeChooser( final DischargeLampModel model, ElementProperties[] elementProperties ) {
        super( new GridBagLayout() );

        setBorder( new TitledBorder( DischargeLampsResources.getString( "Controls.AtomType" ) ) );
        JComboBox comboBox = new JComboBox( elementProperties );
        comboBox.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JComboBox cb = (JComboBox) e.getSource();
                // Get the selected item and tell it to do its thing
                DischargeLampElementProperties selection = (DischargeLampElementProperties) cb.getSelectedItem();
                model.setElementProperties( selection );
                model.getSpectrometer().reset();
            }
        } );
        DischargeLampElementProperties selection = (DischargeLampElementProperties) comboBox.getSelectedItem();
        model.setElementProperties( selection );
        this.add( comboBox, gbc );
    }
}
