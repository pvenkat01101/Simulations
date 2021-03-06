// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.energyformsandchanges.energysystems.model;

import java.util.ArrayList;
import java.util.List;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.Resettable;
import edu.colorado.phet.common.phetcommon.model.clock.ClockAdapter;
import edu.colorado.phet.common.phetcommon.model.clock.ClockEvent;
import edu.colorado.phet.common.phetcommon.model.clock.ConstantDtClock;
import edu.colorado.phet.common.phetcommon.model.property.BooleanProperty;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.energyformsandchanges.common.EFACConstants;

/**
 * Primary model class for the "Energy Systems" tab of the Energy Forms and
 * Changes simulation.
 *
 * @author John Blanco
 */
public class EnergySystemsModel implements Resettable {

    //-------------------------------------------------------------------------
    // Class Data
    //-------------------------------------------------------------------------

    private static final Vector2D OFFSET_BETWEEN_ELEMENTS_ON_CAROUSEL = new Vector2D( 0, -0.4 );

    //-------------------------------------------------------------------------
    // Instance Data
    //-------------------------------------------------------------------------

    // Main model clock.
    private final ConstantDtClock clock = new ConstantDtClock( 30.0 );

    // Boolean property that controls whether the energy chunks are visible to the user.
    public final BooleanProperty energyChunksVisible = new BooleanProperty( false );

    // Carousels that control the positions of the energy sources, converters,
    // and users.
    public final EnergySystemElementCarousel<EnergySource> energySourcesCarousel = new EnergySystemElementCarousel<EnergySource>( new Vector2D( -0.15, 0 ), OFFSET_BETWEEN_ELEMENTS_ON_CAROUSEL );
    public final EnergySystemElementCarousel<EnergyConverter> energyConvertersCarousel = new EnergySystemElementCarousel<EnergyConverter>( new Vector2D( -0.025, 0 ), OFFSET_BETWEEN_ELEMENTS_ON_CAROUSEL );
    public final EnergySystemElementCarousel<EnergyUser> energyUsersCarousel = new EnergySystemElementCarousel<EnergyUser>( new Vector2D( 0.09, 0 ), OFFSET_BETWEEN_ELEMENTS_ON_CAROUSEL );
    private final List<Carousel> carousels = new ArrayList<Carousel>() {{
        add( energySourcesCarousel );
        add( energyConvertersCarousel );
        add( energyUsersCarousel );
    }};

    // Energy Converters.
    public final ElectricalGenerator waterPoweredGenerator = new ElectricalGenerator( energyChunksVisible );
    public final SolarPanel solarPanel = new SolarPanel( getClock(), energyChunksVisible );

    // Energy sources.
    public final FaucetAndWater faucet = new FaucetAndWater( energyChunksVisible, waterPoweredGenerator.getObservableActiveState() );
    public final Sun sun = new Sun( solarPanel, energyChunksVisible );
    public final TeaPot teaPot = new TeaPot( energyChunksVisible, waterPoweredGenerator.getObservableActiveState() );
    public final Biker biker = new Biker( energyChunksVisible, waterPoweredGenerator.getObservableActiveState() );

    // Energy users.
    public final IncandescentLightBulb incandescentLightBulb = new IncandescentLightBulb( energyChunksVisible );
    public final FluorescentLightBulb fluorescentLightBulb = new FluorescentLightBulb( energyChunksVisible );
    public final BeakerHeater beakerHeater = new BeakerHeater( clock, energyChunksVisible );

    // Items that span between energy system elements.
    public final Belt belt;

    //-------------------------------------------------------------------------
    // Constructor(s)
    //-------------------------------------------------------------------------

    public EnergySystemsModel() {

        clock.addClockListener( new ClockAdapter() {
            @Override public void clockTicked( ClockEvent clockEvent ) {
                stepInTime( clockEvent.getSimulationTimeChange() );
            }
        } );

        energySourcesCarousel.add( faucet );
        energySourcesCarousel.add( sun );
        energySourcesCarousel.add( teaPot );
        energySourcesCarousel.add( biker );
        energyConvertersCarousel.add( waterPoweredGenerator );
        energyConvertersCarousel.add( solarPanel );
        energyUsersCarousel.add( beakerHeater );
        energyUsersCarousel.add( incandescentLightBulb );
        energyUsersCarousel.add( fluorescentLightBulb );

        // Create the belt that interconnects the biker and the generator.
        // Some position tweaking was needed in order to get this to show up
        // in the right place.  Not entirely sure why.
        belt = new Belt( Biker.REAR_WHEEL_RADIUS,
                         energySourcesCarousel.getSelectedElementPosition().plus( Biker.CENTER_OF_BACK_WHEEL_OFFSET ).plus( 0.005, 0 ),
                         ElectricalGenerator.WHEEL_RADIUS,
                         energyConvertersCarousel.getSelectedElementPosition().plus( ElectricalGenerator.WHEEL_CENTER_OFFSET ) );

        // Add the functionality to show/hide the belt that interconnects the
        // biker and the generator.
        VoidFunction1<Boolean> beltVisibilityUpdated = new VoidFunction1<Boolean>() {
            public void apply( Boolean isAnimating ) {
                boolean bikerAndGeneratorSelected = !isAnimating && biker.isActive() && waterPoweredGenerator.isActive();
                belt.isVisible.set( bikerAndGeneratorSelected );
                waterPoweredGenerator.directCouplingMode.set( bikerAndGeneratorSelected );
            }
        };
        energySourcesCarousel.getAnimationInProgressProperty().addObserver( beltVisibilityUpdated );
        energyConvertersCarousel.getAnimationInProgressProperty().addObserver( beltVisibilityUpdated );

        // Remove the energy chunks in the energy user when the energy
        // converter is swapped out.  Otherwise it can look odd as the energy
        // chunks continue to move through the user.
        energyConvertersCarousel.getAnimationInProgressProperty().addObserver( new VoidFunction1<Boolean>() {
            public void apply( Boolean animating ) {
                energyUsersCarousel.getSelectedElement().clearEnergyChunks();
            }
        } );

        // The carousel animation is stepped by a separate clock so that they
        // can still operate when the sim is paused.
        ConstantDtClock carouselClock = new ConstantDtClock( EFACConstants.FRAMES_PER_SECOND );
        carouselClock.start();
        carouselClock.addClockListener( new ClockAdapter() {
            @Override public void clockTicked( ClockEvent clockEvent ) {
                // Step the animation for the carousels.
                for ( Carousel carousel : carousels ) {
                    carousel.stepInTime( clockEvent.getSimulationTimeChange() );
                }
            }
        } );
    }

    //-------------------------------------------------------------------------
    // Methods
    //-------------------------------------------------------------------------

    public void reset() {
        clock.resetSimulationTime();
        energyChunksVisible.reset();
        energySourcesCarousel.getSelectedElement().deactivate();
        energyConvertersCarousel.getSelectedElement().deactivate();
        energyUsersCarousel.getSelectedElement().deactivate();
        energySourcesCarousel.targetIndex.set( 0 );
        energyConvertersCarousel.targetIndex.set( 0 );
        energyUsersCarousel.targetIndex.set( 0 );
        energySourcesCarousel.getSelectedElement().activate();
        energyConvertersCarousel.getSelectedElement().activate();
        energyUsersCarousel.getSelectedElement().activate();
    }

    public ConstantDtClock getClock() {
        return clock;
    }

    private void stepInTime( double dt ) {

        // Step the active elements in time to produce, convert, and use energy.
        Energy energyFromSource = energySourcesCarousel.getSelectedElement().stepInTime( dt );
        Energy energyFromConverter = energyConvertersCarousel.getSelectedElement().stepInTime( dt, energyFromSource );
        energyUsersCarousel.getSelectedElement().stepInTime( dt, energyFromConverter );

        // Transfer energy chunks between the elements.
        energyConvertersCarousel.getSelectedElement().injectEnergyChunks( energySourcesCarousel.getSelectedElement().extractOutgoingEnergyChunks() );
        energyUsersCarousel.getSelectedElement().injectEnergyChunks( energyConvertersCarousel.getSelectedElement().extractOutgoingEnergyChunks() );
    }

    /**
     * Pre-load the currently active energy system elements with energy
     * chunks so that the energy chunks are fully propagated into the
     * elements.
     */
    public void preLoadEnergyChunks() {
        energySourcesCarousel.getSelectedElement().preLoadEnergyChunks();
        energyConvertersCarousel.getSelectedElement().preLoadEnergyChunks( energySourcesCarousel.getSelectedElement().getEnergyOutputRate() );
        energyUsersCarousel.getSelectedElement().preLoadEnergyChunks( energyConvertersCarousel.getSelectedElement().getEnergyOutputRate() );
    }
}
