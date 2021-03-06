// Copyright 2002-2012, University of Colorado

package edu.colorado.phet.common.photonabsorption.model.molecules;

import java.awt.geom.Point2D;

import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;
import edu.colorado.phet.common.photonabsorption.model.Molecule;
import edu.colorado.phet.common.photonabsorption.model.PhotonAbsorptionStrategy;
import edu.colorado.phet.common.photonabsorption.model.WavelengthConstants;
import edu.colorado.phet.common.photonabsorption.model.atoms.AtomicBond;
import edu.colorado.phet.common.photonabsorption.model.atoms.CarbonAtom;
import edu.colorado.phet.common.photonabsorption.model.atoms.OxygenAtom;


/**
 * Class that represents carbon dioxide in the model.
 *
 * @author John Blanco
 */
public class CO2 extends Molecule {

    // ------------------------------------------------------------------------
    // Class Data
    // ------------------------------------------------------------------------

    private static final double INITIAL_CARBON_OXYGEN_DISTANCE = 170; // In picometers.

    // Deflection amounts used for the vibration of the CO2 atoms.  These
    // are calculated such that the actual center of gravity should remain
    // constant.
    private static final double CARBON_MAX_DEFLECTION = 40;
    private static final double OXYGEN_MAX_DEFLECTION =
            new CarbonAtom().getMass() * CARBON_MAX_DEFLECTION / ( 2 * new OxygenAtom().getMass() );

    // ------------------------------------------------------------------------
    // Instance Data
    // ------------------------------------------------------------------------

    private final CarbonAtom carbonAtom = new CarbonAtom();
    private final OxygenAtom oxygenAtom1 = new OxygenAtom();
    private final OxygenAtom oxygenAtom2 = new OxygenAtom();
    private final AtomicBond carbonOxygenBond1 = new AtomicBond( carbonAtom, oxygenAtom1, 2 );
    private final AtomicBond carbonOxygenBond2 = new AtomicBond( carbonAtom, oxygenAtom2, 2 );

    // ------------------------------------------------------------------------
    // Constructor(s)
    // ------------------------------------------------------------------------

    public CO2( Point2D inititialCenterOfGravityPos ) {
        // Configure the base class.  It would be better to do this through
        // nested constructors, but I (jblanco) wasn't sure how to do this.
        addAtom( carbonAtom );
        addAtom( oxygenAtom1 );
        addAtom( oxygenAtom2 );
        addAtomicBond( carbonOxygenBond1 );
        addAtomicBond( carbonOxygenBond2 );

        // Set up the photon wavelengths to absorb.
        setPhotonAbsorptionStrategy( WavelengthConstants.IR_WAVELENGTH, new PhotonAbsorptionStrategy.VibrationStrategy( this ) );

        // Set the initial offsets.
        initializeAtomOffsets();

        // Set the initial COG position.
        setCenterOfGravityPos( inititialCenterOfGravityPos );
    }

    public CO2() {
        this( new Point2D.Double( 0, 0 ) );
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    @Override
    public void setVibration( double vibrationRadians ) {
        super.setVibration( vibrationRadians );
        double multFactor = Math.sin( vibrationRadians );
        addInitialAtomCogOffset( carbonAtom, new MutableVector2D( 0, multFactor * CARBON_MAX_DEFLECTION ) );
        addInitialAtomCogOffset( oxygenAtom1, new MutableVector2D( INITIAL_CARBON_OXYGEN_DISTANCE, -multFactor * OXYGEN_MAX_DEFLECTION ) );
        addInitialAtomCogOffset( oxygenAtom2, new MutableVector2D( -INITIAL_CARBON_OXYGEN_DISTANCE, -multFactor * OXYGEN_MAX_DEFLECTION ) );
        updateAtomPositions();
    }

    /* (non-Javadoc)
     * @see edu.colorado.phet.common.photonabsorption.model.Molecule#initializeCogOffsets()
     */
    @Override
    protected void initializeAtomOffsets() {
        addInitialAtomCogOffset( carbonAtom, new MutableVector2D( 0, 0 ) );
        addInitialAtomCogOffset( oxygenAtom1, new MutableVector2D( INITIAL_CARBON_OXYGEN_DISTANCE, 0 ) );
        addInitialAtomCogOffset( oxygenAtom2, new MutableVector2D( -INITIAL_CARBON_OXYGEN_DISTANCE, 0 ) );

        updateAtomPositions();
    }
}
