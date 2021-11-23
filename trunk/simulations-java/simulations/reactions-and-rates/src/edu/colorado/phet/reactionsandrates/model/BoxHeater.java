// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.model;

import java.awt.geom.Point2D;

/**
 * BoxHeater
 * <p/>
 * An agent that tracks the setting of a TemperatureControl and
 * adjusts the temperature of an MRBox accordingly
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class BoxHeater implements TemperatureControl.ChangeListener {
    private MRBox box;


    public BoxHeater( TemperatureControl temperatureControl, MRBox box ) {
        temperatureControl.addChangeListener( this );
        this.box = box;
    }

    public void settingChanged( double setting ) {
        box.setTemperature( setting );
    }

    public void positionChanged( Point2D newPosition ) {
        // noop
    }
}
