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

import java.util.EventObject;

import edu.colorado.phet.fourier.model.Harmonic;


/**
 * HarmonicFocusEvent indicates that a Harmonic has gained or lost focus.
 *
 * @author Chris Malley (cmalley@pixelzoom.com)
 * @version $Revision: 47772 $
 */
public class HarmonicFocusEvent extends EventObject {

    private Harmonic _harmonic;
    private boolean _hasFocus;
    
    /**
     * Sole constructor
     * 
     * @param source
     * @param harmonic
     * @param hasFocus
     */
    public HarmonicFocusEvent( Object source, Harmonic harmonic, boolean hasFocus ) {
        super( source );
        assert( harmonic != null );
        _harmonic = harmonic;
        _hasFocus = hasFocus;
    }
    
    /**
     * Gets the Harmonic that has gained or lost focus.
     * 
     * @return Harmonic
     */
    public Harmonic getHarmonic() {
        return _harmonic;
    }
    
    /**
     * Indicates whether the Harmonic has focus.
     * 
     * @return true or false
     */
    public boolean hasFocus() {
        return _hasFocus;
    }
}
