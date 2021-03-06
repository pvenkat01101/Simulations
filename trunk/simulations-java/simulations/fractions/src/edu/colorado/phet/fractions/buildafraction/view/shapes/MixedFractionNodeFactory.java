// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.buildafraction.view.shapes;

import edu.colorado.phet.fractions.buildafraction.model.MixedFraction;
import edu.colorado.phet.fractions.fractionsintro.intro.view.FractionNode;
import edu.umd.cs.piccolo.PNode;

/**
 * Create a node for the given mixed fraction to show as a target near the collection box.
 *
 * @author Sam Reid
 */
class MixedFractionNodeFactory {
    public static PNode toNode( final MixedFraction target ) {
        if ( target.whole == 0 ) {
            return new FractionNode( target.toFraction(), 0.33 );
        }
        else {
            return new MixedFractionNode( target );
        }
    }
}