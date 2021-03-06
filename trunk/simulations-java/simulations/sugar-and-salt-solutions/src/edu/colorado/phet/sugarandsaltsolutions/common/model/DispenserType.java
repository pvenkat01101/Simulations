// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.common.model;

/**
 * Enum pattern for Salt and Sugar dispensers, to keep track of which one the user is using.
 *
 * @author Sam Reid
 */
public class DispenserType {

    //List of elements comprising the solute
    private final Integer[] elementAtomicMasses;

    public static final DispenserType SALT = new DispenserType( 11, 17 );
    public static final DispenserType SUGAR = new DispenserType( 6, 1, 8 );
    public static final DispenserType GLUCOSE = new DispenserType( 6, 1, 8 );
    public static final DispenserType SODIUM_NITRATE = new DispenserType( 11, 7, 8 );
    public static final DispenserType CALCIUM_CHLORIDE = new DispenserType( 20, 17 );

    //Enum pattern, so no other instances should be created
    private DispenserType( Integer... elementAtomicMasses ) {
        this.elementAtomicMasses = elementAtomicMasses;
    }

    public Integer[] getElementAtomicMasses() {
        return elementAtomicMasses;
    }
}