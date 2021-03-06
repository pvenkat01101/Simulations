// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.common.model.sucrose;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.sugarandsaltsolutions.common.model.SucrosePositions;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.SugarMolecule;

import static edu.colorado.phet.common.phetcommon.math.vector.Vector2D.ZERO;

/**
 * A single sucrose molecule, which is used in lattice creation
 *
 * @author Sam Reid
 */
public class Sucrose extends SugarMolecule {
    public Sucrose() {
        this( ZERO, Math.random() * 2 * Math.PI );
    }

    public Sucrose( Vector2D relativePosition ) {
        this( relativePosition, Math.random() * 2 * Math.PI );
    }

    public Sucrose( Vector2D relativePosition, double angle ) {
        super( relativePosition, angle, new SucrosePositions() );
    }
}