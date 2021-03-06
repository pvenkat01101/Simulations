// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.common.model;

import java.awt.Shape;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.dynamics.IUpdateStrategy;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.dynamics.Motionless;

/**
 * A particle is an indivisible object with a position such as Na+ or a sugar molecule.
 *
 * @author Sam Reid
 */
public abstract class Particle {

    //Interface for setting and observing the position
    private final Property<Vector2D> position;

    //Interface for setting and observing the velocity
    public final Property<Vector2D> velocity;

    //Strategy instance for updating the model when time passes
    private IUpdateStrategy updateStrategy = new Motionless();

    //Flag to indicate whether the particle has ever been submerged underwater.  If so, the model update will constrain the particle so it doesn't leave the water again
    //Note this does not mean the particle is currently submerged, since it could get fully submerged once, then the water could evaporate so the particle is only partly submerged
    //In this case it should still be prevented from leaving the water area
    private boolean hasSubmerged = false;

    public Particle( Vector2D position ) {
        this.position = new Property<Vector2D>( position );
        this.velocity = new Property<Vector2D>( new Vector2D() );
    }

    //Given the specified acceleration from external forces (such as gravity), perform an Euler integration step to move the particle forward in time
    public void stepInTime( Vector2D acceleration, double dt ) {
        velocity.set( velocity.get().plus( acceleration.times( dt ) ) );
        setPosition( position.get().plus( velocity.get().times( dt ) ) );
    }

    public void setPosition( Vector2D location ) {
        position.set( location );
    }

    //Convenience method to translate a particle by the specified model delta (in meters)
    public void translate( Vector2D delta ) {
        translate( delta.getX(), delta.getY() );
    }

    public void translate( double dx, double dy ) {
        setPosition( position.get().plus( dx, dy ) );
    }

    //Get a shape for the particle for purposes of collision detection with beaker solution and beaker walls
    public abstract Shape getShape();

    public Vector2D getPosition() {
        return position.get();
    }

    public void addPositionObserver( VoidFunction1<Vector2D> listener ) {
        position.addObserver( listener );
    }

    //Determines whether the particle has ever been submerged, for purposes of updating its location during the physics update.  See field documentation for more
    public boolean hasSubmerged() {
        return hasSubmerged;
    }

    //Sets whether the particle has ever been submerged, for purposes of updating its location during the physics update.  See field documentation for more
    public void setSubmerged() {
        hasSubmerged = true;
    }

    //Sets the strategy this particle uses to move in time
    public void setUpdateStrategy( IUpdateStrategy updateStrategy ) {
        this.updateStrategy = updateStrategy;
    }

    //Updates the particle according to its UpdateStrategy
    public void stepInTime( double dt ) {
        updateStrategy.stepInTime( this, dt );
    }

    //Gets the distance between the particles
    public double getDistance( Particle b ) {
        return getPosition().distance( b.getPosition() );
    }
}