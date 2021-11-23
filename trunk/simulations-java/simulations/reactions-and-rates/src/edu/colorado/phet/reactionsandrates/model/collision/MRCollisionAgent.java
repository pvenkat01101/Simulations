// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.model.collision;

import edu.colorado.phet.common.mechanics.Body;
import edu.colorado.phet.reactionsandrates.model.MRModel;

/**
 * MRCollisionExpert
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public interface MRCollisionAgent {
    boolean detectAndDoCollision( MRModel model, Body bodyA, Body bodyB );
}
