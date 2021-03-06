// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.common.photonabsorption.model.atoms;

import java.awt.Color;
import java.awt.geom.Point2D;


/**
 * Class that represents an atom of Carbon in the model.
 *
 * @author John Blanco
 */
public class CarbonAtom extends Atom {

    //------------------------------------------------------------------------
    // Class Data
    //------------------------------------------------------------------------

    private static final Color REPRESENTATION_COLOR = Color.GRAY;
    public static final double MASS = 12.011;   // In atomic mass units (AMU).
    private static final double RADIUS = 77;     // In picometers.

    //------------------------------------------------------------------------
    // Constructor(s)
    //------------------------------------------------------------------------

    public CarbonAtom( Point2D position ) {
        super( REPRESENTATION_COLOR, RADIUS, MASS, position );
    }

    public CarbonAtom() {
        this( new Point2D.Double( 0, 0 ) );
    }
}
