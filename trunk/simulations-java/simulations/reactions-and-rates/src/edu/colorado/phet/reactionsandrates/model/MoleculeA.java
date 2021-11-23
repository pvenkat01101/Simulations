// Copyright 2002-2012, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: olsonj $
 * Revision : $Revision: 66113 $
 * Date modified : $Date: 2012-07-21 14:57:33 +0530 (Sat, 21 Jul 2012) $
 */
package edu.colorado.phet.reactionsandrates.model;

import java.awt.geom.Point2D;

import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;

/**
 * MoleculeA
 *
 * @author Ron LeMaster
 * @version $Revision: 66113 $
 */
public class MoleculeA extends SimpleMolecule {
    private static double RADIUS = 12;

    public static double getRADIUS() {
        return RADIUS;
    }

    public MoleculeA() {
        super( RADIUS );
    }

    public MoleculeA( Point2D location, MutableVector2D velocity, MutableVector2D acceleration, double mass, double charge ) {
        super( RADIUS, location, velocity, acceleration, mass, charge );
    }


    public Object clone() {
        return super.clone();
    }
}
