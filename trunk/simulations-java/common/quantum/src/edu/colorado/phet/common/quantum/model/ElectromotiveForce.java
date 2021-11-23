// Copyright 2002-2012, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: olsonj $
 * Revision : $Revision: 66113 $
 * Date modified : $Date: 2012-07-21 14:57:33 +0530 (Sat, 21 Jul 2012) $
 */
package edu.colorado.phet.common.quantum.model;

import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;

/**
 * ElectromotiveForce
 *
 * @author Ron LeMaster
 * @version $Revision: 66113 $
 */
public interface ElectromotiveForce {
    MutableVector2D getElectronAcceleration();
}
