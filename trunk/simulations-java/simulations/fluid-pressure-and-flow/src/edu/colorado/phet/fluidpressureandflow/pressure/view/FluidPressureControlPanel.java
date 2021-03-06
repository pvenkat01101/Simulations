// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.fluidpressureandflow.pressure.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.phetcommon.simsharing.SimSharingManager;
import edu.colorado.phet.common.phetcommon.view.PhetTitledPanel;
import edu.colorado.phet.common.phetcommon.view.VerticalLayoutPanel;
import edu.colorado.phet.common.phetcommon.view.controls.PropertyRadioButton;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.common.piccolophet.nodes.RulerNode;
import edu.colorado.phet.fluidpressureandflow.common.FluidPressureAndFlowModule;
import edu.colorado.phet.fluidpressureandflow.common.model.units.UnitSet;
import edu.colorado.phet.fluidpressureandflow.common.view.EnglishMetricControlPanel;
import edu.colorado.phet.fluidpressureandflow.common.view.FPAFCheckBox;
import edu.colorado.phet.fluidpressureandflow.common.view.FPAFRadioButton;
import edu.colorado.phet.fluidpressureandflow.pressure.model.FluidPressureModel;

import static edu.colorado.phet.common.phetcommon.simsharing.messages.ParameterKeys.isSelected;
import static edu.colorado.phet.common.phetcommon.simsharing.messages.ParameterSet.parameterSet;
import static edu.colorado.phet.common.phetcommon.simsharing.messages.UserActions.pressed;
import static edu.colorado.phet.common.phetcommon.simsharing.messages.UserComponentTypes.icon;
import static edu.colorado.phet.fluidpressureandflow.FPAFSimSharing.UserComponents.*;
import static edu.colorado.phet.fluidpressureandflow.FluidPressureAndFlowResources.Strings.*;

/**
 * Control panel for the "pressure" tab
 *
 * @author Sam Reid
 */
public class FluidPressureControlPanel extends VerticalLayoutPanel {
    public static final Color BACKGROUND = new Color( 239, 250, 125 );
    public static final Color FOREGROUND = Color.black;

    public FluidPressureControlPanel( final FluidPressureAndFlowModule<FluidPressureModel> module ) {
        super();

        add( new JPanel( new GridBagLayout() ) {{

            //Ruler check box
            final FPAFCheckBox checkBox = new FPAFCheckBox( rulerCheckBox, RULER, module.rulerVisible );
            add( checkBox, getConstraints( 0, 0 ) );

            //Ruler icon
            add( RulerIcon( module ), getConstraints( 1, 0 ) );

            //Checkbox that shows/hides the grid
            add( new FPAFCheckBox( gridCheckBox, GRID, module.gridVisible ), getConstraints( 0, 1 ) );
        }} );

        //Add Atmosphere on/off control panel.  So it's nice to be able to turn it off and just focus on the water.
        add( new PhetTitledPanel( ATMOSPHERE ) {{
            add( new PropertyRadioButton<Boolean>( atmosphereOnRadioButton, ON, module.model.atmosphere, true ) );
            add( new PropertyRadioButton<Boolean>( atmosphereOffRadioButton, OFF, module.model.atmosphere, false ) );
        }} );

        //Units control panel that allows choice between atmospheres, english and metric
        final Property<UnitSet> units = module.model.units;
        add( new EnglishMetricControlPanel( new FPAFRadioButton<UnitSet>( metricRadioButton, METRIC, units, UnitSet.METRIC ),
                                            new FPAFRadioButton<UnitSet>( atmospheresRadioButton, ATMOSPHERES, units, UnitSet.ATMOSPHERES ),
                                            new FPAFRadioButton<UnitSet>( englishRadioButton, ENGLISH, units, UnitSet.ENGLISH ) ) );
    }

    public static GridBagConstraints getConstraints( final int _gridx, final int _gridy ) {
        return new GridBagConstraints() {{
            gridx = _gridx;
            gridy = _gridy;
            gridwidth = 1;
            gridheight = 1;
            weightx = 1;
            weighty = 1;
            fill = HORIZONTAL;
        }};
    }

    public static JLabel RulerIcon( final FluidPressureAndFlowModule module ) {
        final RulerNode englishRuler = new RulerNode( 35, 5, 25, new String[] { "0", "1", "2" }, new PhetFont( 8 ), "", new PhetFont( 8 ), 4, 6, 3 );
        return new JLabel( new ImageIcon( englishRuler.toImage() ) ) {{
            addMouseListener( new MouseAdapter() {
                @Override public void mousePressed( final MouseEvent e ) {
                    SimSharingManager.sendUserMessage( rulerCheckBoxIcon, icon, pressed, parameterSet( isSelected, !module.rulerVisible.get() ) );
                    module.rulerVisible.toggle();
                }
            } );
        }};
    }
}