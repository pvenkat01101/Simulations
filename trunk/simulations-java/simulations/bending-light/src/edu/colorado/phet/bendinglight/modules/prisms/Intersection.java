// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.bendinglight.modules.prisms;

import java.util.ArrayList;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction0;

/**
 * Models the intersection between a light ray and an interface, needed so we can optionally depict normals at each intersection.
 *
 * @author Sam Reid
 */
public class Intersection {
    private Vector2D unitNormal;//Unit normal at the meeting between two interfaces where the light ray has struck
    private Vector2D point;//The point where the light ray struck
    private ArrayList<VoidFunction0> cleanupListeners = new ArrayList<VoidFunction0>();//Listeners to get a callback when being removed from the model

    public Intersection( Vector2D unitNormal, Vector2D point ) {
        this.unitNormal = unitNormal;
        this.point = point;
    }

    public Vector2D getPoint() {
        return point;
    }

    public Vector2D getUnitNormal() {
        return unitNormal;
    }

    public void addCleanupListener( VoidFunction0 voidFunction0 ) {
        cleanupListeners.add( voidFunction0 );
    }

    public void remove() {
        for ( VoidFunction0 cleanupListener : cleanupListeners ) {
            cleanupListener.apply();
        }
    }
}
