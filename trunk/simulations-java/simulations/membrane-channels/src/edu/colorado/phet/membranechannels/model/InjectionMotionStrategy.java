// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.membranechannels.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;

/**
 * A motion strategy for injecting particles into the particle chambers.  They
 * start out moving linearly with some randomness, and eventually become a
 * random walk.
 *
 * @author John Blanco
 */
public class InjectionMotionStrategy extends MotionStrategy {

    private static final Random RAND = new Random();

    // Parameters that control the way that injected particles change course
    // over time.  These can be adjusted to achieve the desired appearance.
    private static final double MIN_TIME_TO_FIRST_VELOCITY_UPDATE = 0.2;  // In seconds of sim time, not wall time.
    private static final double MAX_TIME_TO_FIRST_VELOCITY_UPDATE = 1.0;  // In seconds of sim time, not wall time.
    private static final double MIN_TIME_TO_SUBSEQUENT_VELOCITY_UPDATES = 0.2;  // In seconds of sim time, not wall time.
    private static final double MAX_TIME_TO_SUBSEQUENT_VELOCITY_UPDATES = 1.0;  // In seconds of sim time, not wall time.
    private static final double MIN_VELOCITY = 10;
    private static final double MAX_VELOCITY = 14;
    private static final int NUM_UPDATES_BEFORE_ANY_DIRECTION_ALLOWED = 10;

    private final Point2D initialLocation = new Point2D.Double();
    private double timeUntilNextVelocityChange; // In seconds of sim time.
    private MutableVector2D velocityVector = new MutableVector2D();
    private Point2D currentLocation = new Point2D.Double();
    private Rectangle2D motionBounds = new Rectangle2D.Double();
    private int velocityChangeCounter = 0;

    /**
     * Constructor.
     */
    public InjectionMotionStrategy( Point2D initialLocation, Rectangle2D bounds, double angle ) {
        this.initialLocation.setLocation( initialLocation );
        this.motionBounds.setFrame( bounds );
        timeUntilNextVelocityChange = generateNewVelocityChangeTime();
        velocityVector.setMagnitudeAndAngle( MAX_VELOCITY, angle );
        currentLocation.setLocation( initialLocation );
    }

    @Override
    public void move( IMovable movableModelElement, double dt ) {

        double radius = movableModelElement.getRadius();

        // Bounce back toward the inside if we are outside of the motion bounds.
        if ( ( currentLocation.getX() + radius > motionBounds.getMaxX() && velocityVector.getX() > 0 ) ||
             ( currentLocation.getX() - radius < motionBounds.getMinX() && velocityVector.getX() < 0 ) ) {
            // Reverse direction in the X direction.
            velocityVector.setComponents( -velocityVector.getX(), velocityVector.getY() );
        }
        if ( ( currentLocation.getY() + radius > motionBounds.getMaxY() && velocityVector.getY() > 0 ) ||
             ( currentLocation.getY() - radius < motionBounds.getMinY() && velocityVector.getY() < 0 ) ) {
            // Reverse direction in the Y direction.
            velocityVector.setComponents( velocityVector.getX(), -velocityVector.getY() );
        }

        // Update the position of the model element.
        currentLocation.setLocation( currentLocation.getX() + velocityVector.getX() * dt,
                                     currentLocation.getY() + velocityVector.getY() * dt );
        movableModelElement.setPosition( currentLocation );

        // Is it time to change direction?
        timeUntilNextVelocityChange -= dt;
        if ( timeUntilNextVelocityChange <= 0 ) {
            // Yes it is, so change the velocity vector.
            changeVelocityVector();

            // Reset the countdown.
            timeUntilNextVelocityChange = generateNewVelocityChangeTime();
        }
    }

    @Override
    public MutableVector2D getInstantaneousVelocity() {
        return new MutableVector2D( velocityVector.getX(), velocityVector.getY() );
    }

    private double generateNewVelocityChangeTime() {
        if ( velocityChangeCounter == 0 ) {
            return MIN_TIME_TO_FIRST_VELOCITY_UPDATE + RAND.nextDouble() * ( MAX_TIME_TO_FIRST_VELOCITY_UPDATE - MIN_TIME_TO_FIRST_VELOCITY_UPDATE );
        }
        else {
            return MIN_TIME_TO_SUBSEQUENT_VELOCITY_UPDATES + RAND.nextDouble() * ( MAX_TIME_TO_SUBSEQUENT_VELOCITY_UPDATES - MIN_TIME_TO_SUBSEQUENT_VELOCITY_UPDATES );
        }
    }

    /**
     * Change the velocity vector in a random way in order to simulate
     * collisions with other particles.  In order to enhance spreading out
     * through the container, the angle of change is limited for the first
     * several changes.
     */
    private void changeVelocityVector() {
        double angularRange = Math.PI * 2;  // Full range of possible changes.
        if ( velocityChangeCounter < NUM_UPDATES_BEFORE_ANY_DIRECTION_ALLOWED ) {
            // Limit the range of possible angles so as not to cause too
            // radical of a change in direction.
            angularRange = angularRange * ( (double) ( velocityChangeCounter + 1 ) / (double) NUM_UPDATES_BEFORE_ANY_DIRECTION_ALLOWED );
        }
        double rotationAngle = ( RAND.nextDouble() - 0.5 ) * angularRange;
        velocityVector.scale( generateNewVelocityScalar() / velocityVector.magnitude() );
        velocityVector.rotate( rotationAngle );
        velocityChangeCounter++;
    }

    private double generateNewVelocityScalar() {
        return MIN_VELOCITY + RAND.nextDouble() * ( MAX_VELOCITY - MIN_VELOCITY );
    }
}
