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

import java.awt.*;

import javax.swing.*;

/**
 * CurrentSlider
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class CurrentSlider extends JSlider {
    //public class CurrentSlider extends ModelSlider {
    private double maxCurrent;
    JSlider slider;

    public CurrentSlider( double maxCurrent ) {
        super( 0, (int) maxCurrent, 0 );
        this.maxCurrent = maxCurrent;

        setMajorTickSpacing( 25 );
//        setNumMinorTicksPerMajorTick( 1 );
        setPaintLabels( true );
//        setPreferredSliderWidth( 150 );

//        setTextFieldVisible( false );
        setPaintLabels( false );
        setPaintTicks( false );
        setBorder( null );
        setPreferredSize( new Dimension( 150, 30 ) );
    }

    public void setMaxCurrent( double maxCurrent ) {
        this.maxCurrent = maxCurrent;
    }

    public double getPctMax() {
        return getValue() / maxCurrent;
    }
}
