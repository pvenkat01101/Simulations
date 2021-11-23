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

import java.util.EventListener;


/**
 * HarmonicColorChangeListener is the listener interface for receiving
 * events related to the changing of harmonic colors.
 *
 * @author Chris Malley (cmalley@pixelzoom.com)
 * @version $Revision: 47772 $
 */
public interface HarmonicColorChangeListener extends EventListener {

    /**
     * Invoked when a harmonic's color is changed.
     * 
     * @param event
     */
    public void harmonicColorChanged( HarmonicColorChangeEvent event );
}
