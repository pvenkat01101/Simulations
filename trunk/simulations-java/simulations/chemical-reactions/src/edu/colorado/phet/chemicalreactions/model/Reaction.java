// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.chemicalreactions.model;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.phetcommon.util.FunctionalUtils;
import edu.colorado.phet.common.phetcommon.util.Pair;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.common.phetcommon.util.function.Function2;
import edu.colorado.phet.jamaphet.RigidMotionLeastSquares;
import edu.colorado.phet.jamaphet.collision.Collidable2D;
import edu.umd.cs.piccolo.util.PBounds;

import static edu.colorado.phet.chemicalreactions.model.Reaction.ReactionTargetViability.OUT_OF_BOUNDS;
import static edu.colorado.phet.chemicalreactions.model.Reaction.ReactionTargetViability.PREDICTED_SELF_COLLISION;
import static edu.colorado.phet.chemicalreactions.model.Reaction.ReactionTargetViability.TOO_MUCH_ACCELERATION;
import static edu.colorado.phet.chemicalreactions.model.Reaction.ReactionTargetViability.VIABLE;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.map;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.reduceLeft;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.repeat;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.sum;
import static edu.colorado.phet.common.phetcommon.util.FunctionalUtils.zip;
import static edu.colorado.phet.jamaphet.RigidMotionLeastSquares.RigidMotionTransformation;

public class Reaction {

    // how close to "reacting" we need to be to react
    public static final double REACTION_ERROR_THRESHOLD = 15;

    public static final double MAX_SEARCH_TIME = 20;

    public static final int INITIAL_ATTEMPTS = 5;

    public static final double MAX_ACCELERATION = 10000;
    public static final double MAX_ANGULAR_ACCELERATION = 500;
//    public static final double MAX_ACCELERATION = 3000;
//    public static final double MAX_ANGULAR_ACCELERATION = 500;

    public final ReactionShape shape;
    public final List<Molecule> reactants;

    // list of positions we need to target, for easy computational reference
    private final List<Vector2D> reactantMoleculeShapePositions = new ArrayList<Vector2D>();
    private final Kit kit;

    private double fitness;
    private double zeroError;
    private ReactionTarget target;

    public Reaction( Kit kit, ReactionShape reactionShape, List<Molecule> reactants ) {
        this.kit = kit;
        this.shape = reactionShape;
        this.reactants = reactants;

        // verify that the ordering of the molecule types are consistent
        for ( int i = 0; i < reactionShape.reactantSpots.size(); i++ ) {
            assert reactionShape.reactantSpots.get( i ).shape == reactants.get( i ).shape;
        }

        for ( ReactionShape.MoleculeSpot spot : reactionShape.reactantSpots ) {
            reactantMoleculeShapePositions.add( spot.position );
        }
    }

    // slightly tweak the paths of the reactants
    public void tweak( double simulationTimeChange ) {
        assert target != null;
        assert fitness > 0;

        if ( zeroError < Reaction.REACTION_ERROR_THRESHOLD ) {
            double averageVelocity = 0;
            for ( Molecule reactant : reactants ) {
                averageVelocity += reactant.getVelocity().magnitude();
            }
            averageVelocity /= reactants.size();

            final double finalAverageVelocity = averageVelocity;
            kit.completeReaction( this, map( shape.productSpots, new Function1<ReactionShape.MoleculeSpot, Molecule>() {
                public Molecule apply( final ReactionShape.MoleculeSpot spot ) {
                    return new Molecule( spot.shape ) {{
                        Vector2D transformedPosition = target.transformation.transformVector2D( spot.position );
                        Vector2D transformedOrigin = target.transformation.transformVector2D( new Vector2D() );
                        setPosition( transformedPosition );
                        setAngle( (float) target.rotation );

                        // approximately keep momentum with random direction of velocity
                        double randomSpeed = Math.random() * finalAverageVelocity * 2;
                        setVelocity( transformedPosition.minus( transformedOrigin ).normalized().times( randomSpeed ) );
                    }};
                }
            } ) );
            return;
        }

        for ( Molecule molecule : reactants ) {
            // how long our acceleration should last
            double effectiveTime = Math.min( simulationTimeChange, target.t );

            // apply the accelerations
            molecule.setVelocity( molecule.getVelocity().plus( target.getTweakAcceleration( molecule ).times( effectiveTime ) ) );
            molecule.setAngularVelocity( (float) ( molecule.getAngularVelocity() + target.getTweakAngularAcceleration( molecule ) * effectiveTime ) );
        }
    }

    // return angle inclusive in [-pi, pi]
    private static double fixAngle( double angle ) {
        double result = angle % ( 2 * Math.PI );
        if ( result < -Math.PI ) {
            return result + 2 * Math.PI;
        }
        else if ( result > Math.PI ) {
            return result - 2 * Math.PI;
        }
        else {
            return result;
        }
    }

    public static double angleDifference( double a, double b ) {
        return fixAngle( a - b );
    }

    // TODO: move this to phetcommon
    private int samplePoisson( double lambda ) {
        double l = Math.exp( -lambda );
        int k = 0;
        double p = 1;
        do {
            k = k + 1;
            p = p * Math.random();
        } while ( p > l );
        return k - 1;
    }

    // compute the fitness and reaction target
    public void initialize( double rate ) {
        fitness = 0;

        // make some attempts to find a good solution
        repeat( new Runnable() {
            public void run() {
                double t = Math.random() * MAX_SEARCH_TIME;

                // check both types of target finding
                ReactionTarget mainTarget = computeForTime( t );
                ReactionTarget rotationTarget = computeForTimeWithRotation( t, mainTarget.rotation );

                if ( mainTarget.getFitness( kit ) > fitness ) {
                    target = mainTarget;
                    fitness = target.getFitness( kit );
                }

                if ( rotationTarget.getFitness( kit ) > fitness ) {
                    target = rotationTarget;
                    fitness = target.getFitness( kit );
                }
            }
        }, samplePoisson( rate ) );

        // used for "close enough" reactions   TODO: better handling for this
        zeroError = computeForTime( 0 ).error;
    }

    public void tick( double dt ) {
        double t = target.t - dt;

        // used for "close enough" reactions
        zeroError = computeForTime( 0 ).error;

        // bail if we passed time AND we aren't close enough to react
        if ( t < 0 && zeroError > REACTION_ERROR_THRESHOLD ) {
            fitness = 0;
            return;
        }

        target = computeForTime( t );
        ReactionTarget rotationTarget = computeForTimeWithRotation( t, target.rotation );
        if ( rotationTarget.getFitness( kit ) > target.getFitness( kit ) ) {
            target = rotationTarget;
        }
        fitness = target.getFitness( kit );
    }

    private double errorFunction( List<Vector2D> currentDestinations, List<Vector2D> targetDestinations ) {
        // compute a list of differences between the result of the current trajectory and the target destinations
        List<Vector2D> differences = zip( currentDestinations, targetDestinations, new Function2<Vector2D, Vector2D, Vector2D>() {
            public Vector2D apply( Vector2D position, Vector2D target ) {
                return position.minus( target );
            }
        } );

        // return the mean square of the differences (equivalent to the RMS for our minimization case, and faster)
        return reduceLeft( map( differences, new Function1<Vector2D, Double>() {
            public Double apply( Vector2D v ) {
                return v.magnitude();
            }
        } ), new Function2<Double, Double, Double>() {
            public Double apply( Double a, Double b ) {
                return a + b;
            }
        } ) / differences.size();
    }

    private List<Vector2D> getReactantPositionsAfterTime( final double t ) {
        return map( reactants, new Function1<Collidable2D, Vector2D>() {
            public Vector2D apply( Collidable2D object ) {
                return object.getPosition().plus( object.getVelocity().times( t ) );
            }
        } );
    }

    private double computeTimeZeroRotation() {
        return RigidMotionLeastSquares.bestFitMotion2D( reactantMoleculeShapePositions, getReactantPositionsAfterTime( 0 ), false ).getRotation2D();
    }

    public ReactionTarget computeForTime( final double t ) {
        // positions after time T
        List<Vector2D> positions = getReactantPositionsAfterTime( t );

        // rigid motion transformation
        // don't allow reflections for now, since not all molecules can be reflected along their current axis
        final RigidMotionLeastSquares.RigidMotionTransformation transformation = RigidMotionLeastSquares.bestFitMotion2D( reactantMoleculeShapePositions, positions, false );

        // and closest targets at time T, based on the computed positions
        List<Vector2D> transformedTargets = map( reactantMoleculeShapePositions, new Function1<Vector2D, Vector2D>() {
            public Vector2D apply( Vector2D v ) {
                return transformation.transformVector2D( v );
            }
        } );

        double averageError = errorFunction( positions, transformedTargets );
        double rotation = transformation.getRotation2D();

        return new ReactionTarget( this, t, transformation, transformedTargets, averageError, rotation );
    }

    public double computeRotationFromTime( final double t ) {
        // positions after time T
        List<Vector2D> positions = getReactantPositionsAfterTime( t );

        final RigidMotionLeastSquares.RigidMotionTransformation transformation = RigidMotionLeastSquares.bestFitMotion2D( reactantMoleculeShapePositions, positions, false );

        return transformation.getRotation2D();
    }

    public ReactionTarget computeForTimeWithRotation( final double t, final double rotation ) {
        // positions after time T
        List<Vector2D> destinations = getReactantPositionsAfterTime( t );

        // rigid motion transformation
        // don't allow reflections for now, since not all molecules can be reflected along their current axis
        final RigidMotionLeastSquares.RigidMotionTransformation transformationWithTranslation = RigidMotionLeastSquares.bestFitMotion2D( reactantMoleculeShapePositions, destinations, false );
        final RigidMotionLeastSquares.RigidMotionTransformation transformation = new RigidMotionTransformation(
                // 2D rotation matrix from the angle
                new Matrix( 2, 2 ) {{
                    double cos = Math.cos( rotation );
                    double sin = Math.sin( rotation );
                    set( 0, 0, cos );
                    set( 0, 1, -sin );
                    set( 1, 0, sin );
                    set( 1, 1, cos );
                }}
                , transformationWithTranslation.translation
        );

        // and closest targets at time T, based on the computed positions
        List<Vector2D> transformedTargets = map( reactantMoleculeShapePositions, new Function1<Vector2D, Vector2D>() {
            public Vector2D apply( Vector2D v ) {
                return transformation.transformVector2D( v );
            }
        } );

        double averageError = errorFunction( destinations, transformedTargets );

        return new ReactionTarget( this, t, transformation, transformedTargets, averageError, rotation );
    }

    public ReactionShape getShape() {
        return shape;
    }

    public List<Molecule> getReactants() {
        return reactants;
    }

    public List<Vector2D> getReactantMoleculeShapePositions() {
        return reactantMoleculeShapePositions;
    }

    public double getFitness() {
        return fitness;
    }

    public ReactionTarget getTarget() {
        return target;
    }

    public static enum ReactionTargetViability {
        VIABLE,
        TOO_MUCH_ACCELERATION,
        PREDICTED_SELF_COLLISION,
        OUT_OF_BOUNDS
    }

    public static class ReactionTarget {
        public final Reaction reaction;
        public final double t;
        public final RigidMotionTransformation transformation;
        public final List<Vector2D> transformedTargets;
        public final double error;
        public final double rotation;

        public ReactionTarget( Reaction reaction, double t, RigidMotionTransformation transformation, List<Vector2D> transformedTargets, double error, double rotation ) {
            this.reaction = reaction;
            this.t = t;
            this.transformation = transformation;
            this.transformedTargets = transformedTargets;
            this.error = error;
            this.rotation = rotation;
        }

        public double getFitness( Kit kit ) {
            if ( isValidReactionTarget( kit ) != VIABLE ) {
                return 0;
            }
            return Math.exp( -getApproximateAccelerationMagnitude() * Math.pow( t, 1.3 ) );
        }

        public double getApproximateAccelerationMagnitude() {
            double accelerationFactor = Math.sqrt( sum( map( reaction.getReactants(), new Function1<Molecule, Double>() {
                public Double apply( Molecule molecule ) {
                    double magnitude = getTweakAcceleration( molecule ).magnitude();
                    return magnitude * magnitude;
                }
            } ) ) );

            double angularFactor = Math.sqrt( sum( map( reaction.getReactants(), new Function1<Molecule, Double>() {
                public Double apply( Molecule molecule ) {
                    double magnitude = getTweakAngularAcceleration( molecule );
                    return magnitude * magnitude;
                }
            } ) ) );

            return accelerationFactor + angularFactor * MAX_ACCELERATION / MAX_ANGULAR_ACCELERATION;
        }

        public Vector2D getTweakAcceleration( Molecule molecule ) {
            int index = reaction.reactants.indexOf( molecule );

            Vector2D targetPosition = transformedTargets.get( index );

            // compute the necessary (constant) acceleration to reach the target precisely on time
            Vector2D currentTrajectoryDestination = molecule.getPosition().plus( molecule.getVelocity().times( t ) );

            return targetPosition.minus( currentTrajectoryDestination ).times( 2 / ( t * t ) );
        }

        public double getTweakAngularAcceleration( Molecule molecule ) {
            int index = reaction.reactants.indexOf( molecule );

            double targetAngle = fixAngle( rotation + reaction.shape.reactantSpots.get( index ).rotation ); // both the reaction AND molecule rotations
            double destinationAngle = fixAngle( molecule.getAngle() + molecule.getAngularVelocity() * t );

            // find the "closest" angle difference that we can use
            double closestDelta = Double.POSITIVE_INFINITY;
            for ( double symmetryAngle : molecule.shape.symmetryAngles ) {
                double delta = angleDifference( targetAngle + symmetryAngle, destinationAngle );
                if ( Math.abs( delta ) < Math.abs( closestDelta ) ) {
                    closestDelta = delta;
                }
            }

            return closestDelta * 2 / ( t * t );
        }

        // ensure that the reaction target will not cause its own molecules to collide before they reach the target area, AND that we don't go over our max accelerations
        public ReactionTargetViability isValidReactionTarget( Kit kit ) {
            List<Molecule> molecules = reaction.getReactants();

            // invalidate the reaction if part of the target is outside of the play area bounds
            PBounds availablePlayAreaModelBounds = kit.getLayoutBounds().getAvailablePlayAreaModelBounds();
            for ( int i = 0; i < transformedTargets.size(); i++ ) {
                Vector2D transformedTarget = transformedTargets.get( i );
                double radius = reaction.reactants.get( i ).shape.getBoundingCircleRadius();

                if ( isPointOutsideRadiusBounds( transformedTarget, radius, availablePlayAreaModelBounds ) ) {
                    return OUT_OF_BOUNDS;
                }
            }

            final Property<Boolean> isOverAccelerationLimit = new Property<Boolean>( false );

            // compute final velocities so we can calculate whether collisions are likely
            List<Vector2D> finalVelocities = FunctionalUtils.map( molecules, new Function1<Molecule, Vector2D>() {
                public Vector2D apply( Molecule molecule ) {
                    Vector2D acceleration = getTweakAcceleration( molecule );

                    if ( acceleration.magnitude() > MAX_ACCELERATION
                         || Math.abs( getTweakAngularAcceleration( molecule ) ) > MAX_ANGULAR_ACCELERATION ) {
                        isOverAccelerationLimit.set( true );
                    }

                    return molecule.getVelocity().plus( acceleration.times( t ) );
                }
            } );

            // bail out of the accelerations are too much
            if ( isOverAccelerationLimit.get() ) {
                return TOO_MUCH_ACCELERATION;
            }

            // between each pair of molecules, reject cases where they are touching at the final collision AND would be moving away from each other
            // this would mean that to successfully collide at the right time, they would be "passing through" each other, and thus would have
            // collided earlier
            for ( Pair<Integer, Integer> indexPair : FunctionalUtils.pairs( FunctionalUtils.rangeInclusive( 0, molecules.size() - 1 ) ) ) {
                Molecule moleculeA = molecules.get( indexPair._1 );
                Molecule moleculeB = molecules.get( indexPair._2 );
                Vector2D finalPositionA = transformedTargets.get( indexPair._1 );
                Vector2D finalPositionB = transformedTargets.get( indexPair._2 );
                Vector2D finalVelocityA = finalVelocities.get( indexPair._1 );
                Vector2D finalVelocityB = finalVelocities.get( indexPair._2 );

                Vector2D positionDifference = finalPositionB.minus( finalPositionA );
                Vector2D velocityDifference = finalVelocityB.minus( finalVelocityA );

                if ( positionDifference.magnitude() > 1.05 * moleculeA.shape.getBoundingCircleRadius() + moleculeB.shape.getBoundingCircleRadius() ) {
                    // if the molecules aren't touching in their collision positions (and we approximate this by checking the bounding circles),
                    // then we don't bother to run the check
                    continue;
                }

                if ( positionDifference.dot( velocityDifference ) > 0 ) {
                    // our molecules would keep "drifting", so they must have had a collision before our planned collision
                    // do not allow this reaction target
                    return PREDICTED_SELF_COLLISION;
                }
            }

            // double-check that our molecules won't go outside of the bounds DURING animation
            for ( Molecule molecule : molecules ) {
                Vector2D acceleration = getTweakAcceleration( molecule );

                // to go outside of starting / ending bounds, the maximal point would need to be where the velocity component is zero.
                // just solve for that here:
                double tXMax = -molecule.getVelocity().x / acceleration.x;
                double tYMax = -molecule.getVelocity().y / acceleration.y;
                double radius = molecule.shape.getBoundingCircleRadius();

                if ( tXMax > 0 && tXMax < t ) {
                    double x = molecule.getPosition().x + molecule.getVelocity().x * tXMax + 0.5 * acceleration.x * tXMax * tXMax;
                    if ( x - radius < availablePlayAreaModelBounds.getMinX() || x + radius > availablePlayAreaModelBounds.getMaxX() ) {
                        return OUT_OF_BOUNDS;
                    }
                }

                if ( tYMax > 0 && tYMax < t ) {
                    double y = molecule.getPosition().y + molecule.getVelocity().y * tYMax + 0.5 * acceleration.y * tYMax * tYMax;
                    if ( y - radius < availablePlayAreaModelBounds.getMinY() || y + radius > availablePlayAreaModelBounds.getMaxY() ) {
                        return OUT_OF_BOUNDS;
                    }
                }
            }

            // didn't fail any tests, probably viable
            return VIABLE;
        }
    }

    // circle inside rectangle checking is easily accomplished by checking the four maximal points:
    private static boolean isPointOutsideRadiusBounds( Vector2D point, double radius, PBounds bounds ) {
        return point.x - radius < bounds.getMinX()
               || point.x + radius > bounds.getMaxX()
               || point.y - radius < bounds.getMinY()
               || point.y + radius > bounds.getMaxY();
    }
}
