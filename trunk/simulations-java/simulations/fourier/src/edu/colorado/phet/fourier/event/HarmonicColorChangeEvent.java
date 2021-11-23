// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */

package edu.colorado.phet.fourier.event;

import java.awt.Color;
import java.util.EventObject;


/**
 * HarmonicColorChangeEvent indicates that the color associated
 * with a harmonic has changed.
 *
 * @author Chris Malley (cmalley@pixelzoom.com)
 * @version $Revision: 47772 $
 */
public class HarmonicColorChangeEvent extends EventObject {
    
    private int _order;
    private Color _color;
    
    /**
     * Sole constructor.
     * 
     * @param source
     * @param order the order of the harmonic that has been changed
     * @param color
     */
    public HarmonicColorChangeEvent( Object source, int order, Color color ) {
        super( source );
        assert( order >= 0 );
        assert( color != null );
        _order = order;
        _color = color;
    }
    
    /**
     * Gets the order of the harmonic that has been changed.
     * The fundamental harmonic has order zero.
     * 
     * @return the order
     */
    public int getOrder() {
        return _order;
    }
    
    /**
     * Gets the new color for the harmonic.
     * 
     * @return Color
     */
    public Color getColor() {
        return _color;
    }
}
