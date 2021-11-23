// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.sound.coreadditions;

import javax.swing.*;

/**
 * A subclass of JSpinner that moves up and down through orders of magnitude
 *
 * @author ?
 * @version $Revision: 47772 $
 */
public class OrderOfMagnitudeSpinner extends JSpinner {

    public OrderOfMagnitudeSpinner( float minFactor, float maxFactor ) {
        super();
        final SpinnerModel model = new OrderOfMagnitudeListModel( minFactor, maxFactor );
        setModel( model );
    }

    //
    // Inner classes
    //

    /**
     * The model class for the OrderOfMagnitudeSpinner
     */
    private class OrderOfMagnitudeListModel extends SpinnerNumberModel {

        private float maxFactor;
        private float minFactor;

        OrderOfMagnitudeListModel( float minFactor, float maxFactor ) {
            super( 1.0, minFactor, maxFactor, 1 );
            this.maxFactor = maxFactor;
            this.minFactor = minFactor;
        }

        public Object getNextValue() {
            Double currValue = (Double)getValue();
            if( currValue.floatValue() >= maxFactor ) {
                return currValue;
            }
            else {
                return new Double( ( (Double)getValue() ).floatValue() * 10 );
            }
        }

        public Object getPreviousValue() {
            Double currValue = (Double)getValue();
            if( currValue.floatValue() <= minFactor ) {
                return currValue;
            }
            else {
                return new Double( ( (Double)getValue() ).floatValue() / 10 );
            }
        }
    }
}
