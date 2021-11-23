// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.modules;

import edu.colorado.phet.reactionsandrates.MRConfig;

/**
 * RateExperimentsModule
 * <p/>
 * This module has controls for running experiments
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class RateExperimentsModule extends ComplexModule {
    private RateExperimentsMRControlPanel controlPanel;
    private boolean isExperimentRunning = false;

    /**
     *
     */
    public RateExperimentsModule() {
        super( MRConfig.RESOURCES.getLocalizedString( "module.rate-experiments" ) );
    }

    protected MRControlPanel createControlPanel() {
        controlPanel = new RateExperimentsMRControlPanel( this );
        return controlPanel;
    }

    /**
     * Tells the module if an experiment is running or not. This allows the module to enable
     * or disable appropriate controls.
     *
     * @param isRunning
     */
    public void setExperimentRunning( boolean isRunning ) {
        isExperimentRunning = isRunning;
        if( controlPanel != null ) {
            controlPanel.setExperimentRunning( isRunning );
            setStripChartRecording( isRunning );
        }
    }
    
    public boolean isExperimentRunning() {
        return isExperimentRunning;
    }

    public void resetStripChart() {
        super.resetStripChart();
        setFirstTimeStripChartVisible( true );
    }

    public void reset() {
        super.reset();
        controlPanel.reset();
    }

    public void emptyBox() {
        getMRModel().removeAllMolecules();
    }
    
    public void clearExperiment() {
        super.clearExperiment();
        controlPanel.clearExperiment();
    }
}
