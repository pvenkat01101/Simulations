// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.fractionsintro.intro.controller;

import fj.F;
import lombok.EqualsAndHashCode;

import edu.colorado.phet.fractions.fractionsintro.intro.model.IntroState;
import edu.colorado.phet.fractions.fractionsintro.intro.view.Representation;

/**
 * Creates a new model by setting the representation set to the specified one
 *
 * @author Sam Reid
 */
public @EqualsAndHashCode(callSuper = false) class SetRepresentation extends F<IntroState, IntroState> {
    public final Representation representation;

    public SetRepresentation( final Representation representation ) {this.representation = representation;}

    public IntroState f( IntroState s ) {
        //Workaround for a bug: when dragging number line quickly, pie set gets out of sync.  So update it when representations change
        return s.representation( representation ).fromContainerSet( s.containerSet, s.factorySet );
    }
}