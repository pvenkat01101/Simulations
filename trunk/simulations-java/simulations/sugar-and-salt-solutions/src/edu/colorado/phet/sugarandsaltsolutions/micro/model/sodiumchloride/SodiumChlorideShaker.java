// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.micro.model.sodiumchloride;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.property.ObservableProperty;
import edu.colorado.phet.sugarandsaltsolutions.common.model.Beaker;
import edu.colorado.phet.sugarandsaltsolutions.common.model.DispenserType;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.MicroModel;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.MicroShaker;

import static edu.colorado.phet.sugarandsaltsolutions.micro.model.RandomUtil.randomAngle;

/**
 * This salt shaker adds salt (NaCl) crystals to the model when shaken
 *
 * @author Sam Reid
 */
public class SodiumChlorideShaker extends MicroShaker {

    public SodiumChlorideShaker( double x, double y, Beaker beaker, ObservableProperty<Boolean> moreAllowed, String name, double distanceScale, ObservableProperty<DispenserType> selectedType, DispenserType type, MicroModel model ) {
        super( x, y, beaker, moreAllowed, name, distanceScale, selectedType, type, model );
    }

    //Create a random salt crystal and add it to the model
    @Override protected void addCrystal( MicroModel model, Vector2D outputPoint ) {

        //Attempt to randomly create a crystal with a correct balance of components
        model.addSodiumChlorideCrystal( new SodiumChlorideCrystal( outputPoint, randomAngle() ) {{ grow( 6 ); }} );
    }
}