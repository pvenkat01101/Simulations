// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.photoelectric.model;

import edu.colorado.phet.common.quantum.model.ElementProperties;

/**
 * TargetMaterial
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class TargetMaterial {
    private ElementProperties properties;

    public TargetMaterial( ElementProperties properties ) {
        this.properties = properties;
    }
}
