// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.circuitconstructionkit;

import java.util.Arrays;

/**
 * User: Sam Reid
 * Date: Jul 7, 2006
 * Time: 9:22:23 AM
 */
public class CCKParameters {
    private final boolean blackBox;
    private String[] args;
    private boolean virtualLab = false;
    private boolean grabBag = true;
    private boolean allowPlainResistors = true;
    private boolean hugeBatteries = true;
    private boolean allowShowReadouts = true;
    private boolean allowSchematicMode = true;
    private boolean useAdvancedControlPanel = true;
    private boolean useNonContactAmmeter = true;
    private boolean hideAllElectrons = false;
    private boolean grabBagMode = false;
    private boolean useVisualControlPanel = true;
    private boolean dynamics = false;
    public static final String VIRTUAL_LAB = "-virtuallab";

    public CCKParameters( CCKModule module, String[] args, boolean ac, boolean virtualLab, boolean blackBox, boolean blackBoxWithElectrons ) {
        this.blackBox = blackBox;
        this.args = args;
        if ( ac ) {
            dynamics = true;
        }
        if ( virtualLab ) {
            virtualLab = true;
        }
        if ( containsArg( "-grabbag" ) ) {
            grabBagMode = true;
        }
        if ( containsArg( "-noElectrons" ) ) {
            module.setElectronsVisible( false );
            hideAllElectrons = true;
        }
        if ( containsArg( "-exp1" ) ) {
            module.setElectronsVisible( true );
            hideAllElectrons = false;
            allowSchematicMode = false;
            useNonContactAmmeter = false;
            grabBag = true;
            hugeBatteries = false;
            allowPlainResistors = true;
            useAdvancedControlPanel = false;
            useVisualControlPanel = false;
        }
        else if ( containsArg( "-exp2" ) ) {
            module.setElectronsVisible( false );
            hideAllElectrons = true;
            allowSchematicMode = false;
            useNonContactAmmeter = false;
            grabBag = true;
            hugeBatteries = false;
            allowPlainResistors = true;
            useAdvancedControlPanel = false;
            useVisualControlPanel = false;
        }
        if ( virtualLab ) {
            allowShowReadouts = false;
            allowSchematicMode = false;
            useNonContactAmmeter = false;
        }
        if ( grabBagMode ) {
            grabBag = true;
            hugeBatteries = true;
            allowPlainResistors = false;
            allowShowReadouts = true;
            allowSchematicMode = false;
            useAdvancedControlPanel = false;
            useNonContactAmmeter = true;
            hideAllElectrons = false;
        }
        if ( blackBox ) {
            allowShowReadouts = false;
            useAdvancedControlPanel = false;
            hideAllElectrons = true;
        }
        if ( blackBoxWithElectrons ) {
            allowShowReadouts = false;
            useAdvancedControlPanel = false;
            hideAllElectrons = false;
        }
    }

    public boolean isUseVisualControlPanel() {
        return useVisualControlPanel;
    }

    private boolean containsArg( String s ) {
        return Arrays.asList( args ).contains( s );
    }

    public boolean hideAllElectrons() {
        return hideAllElectrons;
    }

    public boolean useNonContactAmmeter() {
        return useNonContactAmmeter;
    }

    public boolean showGrabBag() {
        return grabBag;
    }

    public boolean allowSchematicMode() {
        return allowSchematicMode;
    }

    public boolean allowShowReadouts() {
        return allowShowReadouts;
    }

    public boolean hugeRangeOnBatteries() {
        return hugeBatteries;
    }

    public boolean allowPlainResistors() {
        return allowPlainResistors;
    }

    public boolean getUseAdvancedControlPanel() {
        return useAdvancedControlPanel;
    }

    public boolean getAllowDynamics() {
        return dynamics;
    }

    public boolean allowSizeControls() { return !blackBox; }
}
