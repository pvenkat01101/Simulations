// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.bendinglight.modules.prisms;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.colorado.phet.bendinglight.model.BendingLightModel;
import edu.colorado.phet.bendinglight.model.LightRay;
import edu.colorado.phet.bendinglight.model.Medium;
import edu.colorado.phet.bendinglight.model.MediumColorFactory;
import edu.colorado.phet.bendinglight.model.ProtractorModel;
import edu.colorado.phet.bendinglight.view.LaserColor;
import edu.colorado.phet.common.phetcommon.math.MathUtil;
import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.clock.ConstantDtClock;
import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.phetcommon.util.RichSimpleObserver;
import edu.colorado.phet.common.phetcommon.util.SimpleObserver;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.common.phetcommon.view.util.VisibleColor;

import static java.lang.Math.*;

/**
 * Model for the "prism break" tab, in which the user can move the laser and many prisms.
 *
 * @author Sam Reid
 */
public class PrismBreakModel extends BendingLightModel {
    private ArrayList<Prism> prisms = new ArrayList<Prism>();
    public final Property<Boolean> manyRays = new Property<Boolean>( false );//show multiple beams to help show how lenses work
    public final Property<Medium> environment = new Property<Medium>( new Medium( new Rectangle2D.Double( -1, 0, 2, 1 ), AIR, MediumColorFactory.getColor( AIR.getIndexOfRefractionForRedLight() ) ) );//Environment the laser is in
    public final Property<Medium> prismMedium = new Property<Medium>( new Medium( new Rectangle2D.Double( -1, -1, 2, 1 ), GLASS, MediumColorFactory.getColor( GLASS.getIndexOfRefractionForRedLight() ) ) );//Material that comprises the prisms
    public final Property<Boolean> showReflections = new Property<Boolean>( false );//If false, will hide non TIR reflections
    public final ArrayList<Intersection> intersections = new ArrayList<Intersection>();//List of intersections, which can be shown graphically
    public final ArrayList<VoidFunction1<Intersection>> intersectionListeners = new ArrayList<VoidFunction1<Intersection>>();//Listen for creation of intersections to show them
    private final ProtractorModel protractorModel = new ProtractorModel( 0, 0 );//Draggable and rotatable protractor

    //Listener that updates the model when the prism shapes change, keep a reference to it so it can be removed to avoid memory leaks on removePrism()
    private final SimpleObserver updateModel = new SimpleObserver() {
        public void update() {
            updateModel();
        }
    };

    public PrismBreakModel( ConstantDtClock clock ) {
        super( clock, PI, false, DEFAULT_LASER_DISTANCE_FROM_PIVOT * 0.9 );
        //Recompute the model when any dependencies change
        new RichSimpleObserver() {
            public void update() {
                updateModel();
            }
        }.observe( environment, prismMedium, manyRays, laser.color, showReflections );
    }

    @Override
    public void resetAll() {
        super.resetAll();
        while ( prisms.size() > 0 ) {
            removePrism( prisms.get( 0 ) );
        }
        manyRays.reset();
        environment.reset();
        prismMedium.reset();
        showReflections.reset();
        protractorModel.reset();
    }

    //List of prism prototypes that can be created in the sim
    public static ArrayList<Prism> getPrismPrototypes() {
        return new ArrayList<Prism>() {{
            final double a = CHARACTERISTIC_LENGTH * 10;//characteristic length scale
            final double b = a / 4;//characteristic length scale
            //Square
            add( new Prism( 3,//attach at bottom right
                            new Vector2D(),
                            new Vector2D( 0, a ),
                            new Vector2D( a, a ),
                            new Vector2D( a, 0 ) ) );

            //Triangle
            add( new Prism( 1,//attach at bottom right
                            new Vector2D(),
                            new Vector2D( a, 0 ),
                            new Vector2D( a / 2, a * sqrt( 3 ) / 2.0 ) ) );

            //Trapezoid
            add( new Prism( 1,//attach at bottom right
                            new Vector2D(),
                            new Vector2D( a, 0 ),
                            new Vector2D( a / 2 + b, a * sqrt( 3 ) / 2.0 ),
                            new Vector2D( a / 2 - b, a * sqrt( 3 ) / 2.0 )
            ) );

            double radius = a / 2;

            //Continuous Circle
            add( new Prism( new Circle( new Vector2D(), radius ) ) );

            //Continuous Semicircle
            add( new Prism( new ShapeIntersection( new Circle( new Vector2D(), radius ), new Polygon( new Vector2D[] {
                    new Vector2D( 0, radius ),
                    new Vector2D( 0, -radius ),
                    new Vector2D( -radius, -radius ),
                    new Vector2D( -radius, radius )
            }, 1//attach at bottom right
            ) ) ) );

            //Continuous Diverging Lens
            add( new Prism( new ShapeDifference( new Polygon( new Vector2D[] {
                    new Vector2D( 0, -radius ),
                    new Vector2D( radius * ( 0.6 / 0.5 ), -radius ),
                    new Vector2D( radius * ( 0.6 / 0.5 ), radius ),
                    new Vector2D( 0, radius )
            }, 1//attach at bottom right
            ), new Circle( new Vector2D(), radius ) ) ) );
        }};
    }

    //Adds a prism to the model; doesn't signal a "prism added event", adding graphics must be handled by the client that added the prism.
    //This gives the client fine-grained control over creation of model elements and associated nodes, but future TODOs could investigate using standard
    //Model creation/notification scheme
    public void addPrism( Prism prism ) {
        prism.shape.addObserver( updateModel );
        prisms.add( prism );
    }

    public void removePrism( Prism prism ) {
        prisms.remove( prism );
        prism.shape.removeObserver( updateModel );
        updateModel();
    }

    public Iterable<? extends Prism> getPrisms() {
        return prisms;
    }

    private void propagate( Vector2D tail, Vector2D directionUnitVector, double power, boolean laserInPrism ) {
        //Determines whether to use white light or single color light
        if ( laser.color.get() == LaserColor.WHITE_LIGHT ) {
            final double min = VisibleColor.MIN_WAVELENGTH / 1E9;
            final double max = VisibleColor.MAX_WAVELENGTH / 1E9;
            double dw = ( max - min ) / 16;//This number sets the number of (equally spaced wavelength) rays to show in a white beam.  More rays looks better but is more computationally intensive.
            for ( double wavelength = min; wavelength <= max; wavelength += dw ) {
                double mediumIndexOfRefraction = laserInPrism ? prismMedium.get().getIndexOfRefraction( wavelength ) : environment.get().getIndexOfRefraction( wavelength );
                propagate( new Ray( tail, directionUnitVector, power, wavelength, mediumIndexOfRefraction, SPEED_OF_LIGHT / wavelength ), 0 );
            }
        }
        else {
            double mediumIndexOfRefraction = laserInPrism ? prismMedium.get().getIndexOfRefraction( laser.getWavelength() ) : environment.get().getIndexOfRefraction( laser.getWavelength() );
            propagate( new Ray( tail, directionUnitVector, power, laser.getWavelength(), mediumIndexOfRefraction, laser.getFrequency() ), 0 );
        }
    }

    //Algorithm that computes the trajectories of the rays throughout the system
    @Override protected void propagateRays() {
        if ( laser.on.get() ) {
            final Vector2D tail = new Vector2D( laser.emissionPoint.get() );
            final boolean laserInPrism = isLaserInPrism();
            final Vector2D directionUnitVector = laser.getDirectionUnitVector();
            if ( !manyRays.get() ) {
                //This can be used to show the main central ray
                propagate( tail, directionUnitVector, 1.0, laserInPrism );
            }
            else {
                //Many parallel rays
                for ( double x = -WAVELENGTH_RED; x <= WAVELENGTH_RED * 1.1; x += WAVELENGTH_RED / 2 ) {
                    Vector2D offset = directionUnitVector.getRotatedInstance( Math.PI / 2 ).times( x );
                    propagate( tail.plus( offset ), directionUnitVector, 1.0, laserInPrism );
                }
            }
        }
    }

    //Determine if the laser beam originates within a prism for purpose of determining what index of refraction to use initially
    private boolean isLaserInPrism() {
        for ( Prism prism : prisms ) {
            if ( prism.contains( laser.emissionPoint.get() ) ) { return true; }
        }
        return false;
    }

    //Recursive algorithm to compute the pattern of rays in the system.  This is the main computation of this model, rays are cleared beforehand and this algorithm adds them as it goes
    private void propagate( Ray incidentRay, int count ) {
        double waveWidth = CHARACTERISTIC_LENGTH * 5;

        //Termination condition of we have reached too many iterations or if the ray is very weak
        if ( count > 50 || incidentRay.power < 0.001 ) {
            return;
        }

        //Check for an intersection
        Intersection intersection = getIntersection( incidentRay, prisms );
        Vector2D L = incidentRay.directionUnitVector;
        final double n1 = incidentRay.mediumIndexOfRefraction;
        final double wavelengthInN1 = incidentRay.wavelength / n1;
        if ( intersection != null ) {
            //There was an intersection, so reflect and refract the light

            //List the intersection in the model
            addIntersection( intersection );

            Vector2D pointOnOtherSide = new Vector2D( intersection.getPoint() ).plus( incidentRay.directionUnitVector.getInstanceOfMagnitude( 1E-12 ) );
            boolean outputInsidePrism = false;
            for ( Prism prism : prisms ) {
                if ( prism.contains( pointOnOtherSide ) ) {
                    outputInsidePrism = true;
                }
            }
            //Index of refraction of the other medium
            double n2 = outputInsidePrism ? prismMedium.get().getIndexOfRefraction( incidentRay.getBaseWavelength() ) : environment.get().getIndexOfRefraction( incidentRay.getBaseWavelength() );

            //Precompute for readability
            Vector2D point = intersection.getPoint();
            Vector2D n = intersection.getUnitNormal();

            //Compute the output rays, see http://en.wikipedia.org/wiki/Snell's_law#Vector_form
            double cosTheta1 = n.dot( L.times( -1 ) );
            final double cosTheta2Radicand = 1 - pow( n1 / n2, 2 ) * ( 1 - pow( cosTheta1, 2 ) );
            double cosTheta2 = sqrt( cosTheta2Radicand );
            boolean totalInternalReflection = cosTheta2Radicand < 0;
            Vector2D vReflect = L.plus( n.times( 2 * cosTheta1 ) );
            Vector2D vRefract = cosTheta1 > 0 ?
                                L.times( n1 / n2 ).plus( n.times( n1 / n2 * cosTheta1 - cosTheta2 ) ) :
                                L.times( n1 / n2 ).plus( n.times( n1 / n2 * cosTheta1 + cosTheta2 ) );

            final double reflectedPower = totalInternalReflection ? 1 : MathUtil.clamp( 0, getReflectedPower( n1, n2, cosTheta1, cosTheta2 ), 1 );
            final double transmittedPower = totalInternalReflection ? 0 : MathUtil.clamp( 0, getTransmittedPower( n1, n2, cosTheta1, cosTheta2 ), 1 );

            //Create the new rays and propagate them recursively
            Ray reflected = new Ray( point.plus( incidentRay.directionUnitVector.times( -1E-12 ) ), vReflect, incidentRay.power * reflectedPower, incidentRay.wavelength, incidentRay.mediumIndexOfRefraction, incidentRay.frequency );
            Ray refracted = new Ray( point.plus( incidentRay.directionUnitVector.times( +1E-12 ) ), vRefract, incidentRay.power * transmittedPower, incidentRay.wavelength, n2, incidentRay.frequency );
            if ( showReflections.get() || totalInternalReflection ) {
                propagate( reflected, count + 1 );
            }
            propagate( refracted, count + 1 );

            //Add the incident ray itself
            addRay( new LightRay( incidentRay.tail, intersection.getPoint(), n1, wavelengthInN1, incidentRay.power, new VisibleColor( incidentRay.wavelength * 1E9 ), waveWidth, 0, null, true, false ) );
        }
        else {
            //No intersection, so the light ray should just keep going
            addRay( new LightRay( incidentRay.tail, incidentRay.tail.plus( incidentRay.directionUnitVector.times( 1 ) )//1 meter long ray (long enough to seem like infinity for the sim which is at nm scale)
                    , n1, wavelengthInN1, incidentRay.power, new VisibleColor( incidentRay.wavelength * 1E9 ), waveWidth, 0, null, true, false ) );
        }
    }

    //Signify that another ray/interface collision occurred
    private void addIntersection( Intersection intersection ) {
        intersections.add( intersection );
        for ( VoidFunction1<Intersection> intersectionListener : intersectionListeners ) {
            intersectionListener.apply( intersection );
        }
    }

    //Add a listener that will be notified when light hits an interface
    public void addIntersectionListener( VoidFunction1<Intersection> listener ) {
        intersectionListeners.add( listener );
    }

    //Find the nearest intersection between a light ray and the set of prisms in the play area
    private static Intersection getIntersection( final Ray incidentRay, ArrayList<Prism> prisms ) {
        ArrayList<Intersection> allIntersections = new ArrayList<Intersection>();
        for ( Prism prism : prisms ) {
            allIntersections.addAll( prism.getIntersections( incidentRay ) );
        }

        //Get the closest one (which would be hit first)
        Collections.sort( allIntersections, new Comparator<Intersection>() {
            public int compare( Intersection o1, Intersection o2 ) {
                return Double.compare( o1.getPoint().distance( incidentRay.tail ),
                                       o2.getPoint().distance( incidentRay.tail ) );
            }
        } );
        return allIntersections.size() == 0 ? null : allIntersections.get( 0 );
    }

    @Override protected void clearModel() {
        super.clearModel();
        if ( intersections != null ) {
            for ( Intersection intersection : intersections ) {
                intersection.remove();
            }
            intersections.clear();
        }
    }

    public ProtractorModel getProtractorModel() {
        return protractorModel;
    }
}