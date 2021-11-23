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

import edu.colorado.phet.common.phetcommon.model.clock.IClock;

import java.awt.geom.Point2D;

/**
 * LinearGradientMagnet
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class LinearGradientMagnet extends GradientElectromagnet {
    public LinearGradientMagnet( Point2D position, double width, double height, IClock clock, GradientElectromagnet.LinearGradient gradient, Orientation orientation ) {
        super( position, width, height, clock, gradient, orientation );
    }
}
