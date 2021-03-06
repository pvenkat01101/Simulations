// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.fractionsintro.intro.controller;

import fj.F;
import lombok.EqualsAndHashCode;

import edu.colorado.phet.fractions.fractionsintro.intro.model.IntroState;
import edu.colorado.phet.fractions.fractionsintro.intro.model.containerset.ContainerSet;
import edu.colorado.phet.fractions.fractionsintro.intro.model.pieset.PieSet;
import edu.colorado.phet.fractions.fractionsintro.intro.model.pieset.factories.FactorySet;

/**
 * Creates a new model by setting the circular pie set to the specified PieSet
 *
 * @author Sam Reid
 */
public @EqualsAndHashCode(callSuper = false) class SetCircularPieSet extends F<IntroState, IntroState> {
    private final PieSet pieSet;

    public SetCircularPieSet( final PieSet pieSet ) {this.pieSet = pieSet;}

    @Override public IntroState f( final IntroState s ) {
        final ContainerSet c = pieSet.toContainerSet();
        FactorySet factorySet = s.factorySet;
        //Update both the pie set and container state to match the user specified pie set
        return s.pieSet( pieSet ).
                containerSet( c ).
                numerator( c.numerator ).
                horizontalBarSet( factorySet.horizontalSliceFactory.fromContainerSetState( c ) ).
                verticalBarSet( factorySet.verticalSliceFactory.fromContainerSetState( c ) ).
                waterGlassSet( factorySet.waterGlassSetFactory.fromContainerSetState( c ) ).
                cakeSet( factorySet.cakeSliceFactory.fromContainerSetState( c ) );
    }
}