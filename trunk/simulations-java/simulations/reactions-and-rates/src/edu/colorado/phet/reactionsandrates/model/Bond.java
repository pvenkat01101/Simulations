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

import edu.colorado.phet.common.phetcommon.model.ModelElement;
import edu.colorado.phet.common.phetcommon.util.SimpleObservable;

/**
 * Bond
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class Bond extends SimpleObservable implements ModelElement {
    private SimpleMolecule[] participants;

    public Bond( SimpleMolecule m1, SimpleMolecule m2 ) {
        participants = new SimpleMolecule[]{m1, m2};
    }

    public SimpleMolecule[] getParticipants() {
        return participants;
    }

    /**
     * No time-dependent behavior
     *
     * @param dt
     */
    public void stepInTime( double dt ) {
        notifyObservers();
    }

}
