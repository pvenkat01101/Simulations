// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.dischargelamps;

import edu.colorado.phet.common.phetcommon.model.clock.IClock;

/**
 * MultipleAtomModule
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class MultipleAtomModule extends DischargeLampModule {

    /**
     * Constructor
     *
     * @param clock
     */
    protected MultipleAtomModule( String name, IClock clock, int numAtoms, double maxAtomSpeed ) {
        super( name, clock );
        setLogoPanel( null );
        addAtoms( getTube(), numAtoms, maxAtomSpeed );
    }
}
