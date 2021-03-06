// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.reactantsproductsandleftovers;

import edu.colorado.phet.common.phetcommon.simsharing.messages.IModelAction;
import edu.colorado.phet.common.phetcommon.simsharing.messages.IModelComponent;
import edu.colorado.phet.common.phetcommon.simsharing.messages.IParameterKey;
import edu.colorado.phet.common.phetcommon.simsharing.messages.IUserComponent;

/**
 * Sim-sharing enums that are specific to this sim.
 *
 * @author Chris Malley (cmalley@pixelzoom.com)
 */
public class RPALSimSharing {

    public static enum UserComponents implements IUserComponent {
        sandwichShopTab, realReactionTab, gameTab,
        equationSpinner, reactantSpinner, productSpinner, leftoverSpinner,
        sandwichRadioButton, realReactionRadioButton,
        checkButton, nextButton, tryAgainButton, showAnswerButton,
        nothingRadioButton, moleculesRadioButton, numbersRadioButton,
        worksheetColorsMenuItem
    }

    public static enum ModelComponents implements IModelComponent {
        gameChallenge
    }

    public static enum ModelActions implements IModelAction {
        created, aborted
    }

    public static enum ParameterKeys implements IParameterKey {
        formula, quantities
    }
}