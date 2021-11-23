// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.model;

import edu.colorado.phet.common.quantum.model.Photon;
import edu.colorado.phet.mri.util.IScalar;

/**
 * MriEmittedPhoton
 * <p/>
 * A photon that carries a plane wave with it
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class MriEmittedPhoton extends Photon implements IScalar {

    public double getValue() {
        return getEnergy();
    }
}
