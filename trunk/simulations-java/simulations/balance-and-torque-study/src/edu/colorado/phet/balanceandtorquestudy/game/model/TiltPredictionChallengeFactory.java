// Copyright 2002-2013, University of Colorado
package edu.colorado.phet.balanceandtorquestudy.game.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.colorado.phet.balanceandtorquestudy.common.model.Plank;
import edu.colorado.phet.balanceandtorquestudy.common.model.masses.BrickStack;
import edu.colorado.phet.common.phetcommon.simsharing.messages.IModelComponentType;

import static edu.colorado.phet.balanceandtorquestudy.BalanceAndTorqueSimSharing.ModelComponentTypes.*;

/**
 * This is a specialized version of a factory class that generates challenges
 * for the game.  This version was created specifically for the Stanford study
 * and only creates tilt prediction challenges, and does so in a way that was
 * specified by the Stanford researchers.
 *
 * @author John Blanco
 */
public class TiltPredictionChallengeFactory {

    //-------------------------------------------------------------------------
    // Class Data
    //-------------------------------------------------------------------------

    private static final Random RAND = new Random();

    // Challenge configurations, supplied by Min Chi of Stanford.
    private static final int[][] DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS = new int[][] {
            { 6, 6, 2, 6 },
            { 3, 5, 5, 5 },
            { 4, 7, 2, 7 },
            { 4, 4, 1, 4 },
            { 5, 4, 6, 4 },
            { 5, 3, 6, 3 },
            { 1, 3, 2, 3 },
            { 3, 4, 5, 4 },
            { 4, 5, 6, 5 },
            { 4, 7, 6, 7 },
            { 1, 2, 5, 2 },
            { 6, 2, 4, 2 },
            { 4, 2, 6, 2 },
            { 1, 3, 3, 3 },
            { 3, 4, 6, 4 },
            { 6, 2, 1, 2 },
            { 3, 6, 1, 6 },
            { 5, 2, 1, 2 },
            { 1, 1, 4, 1 },
            { 2, 5, 6, 5 },
            { 6, 1, 3, 1 },
            { 3, 2, 1, 2 },
            { 1, 6, 6, 6 },
            { 2, 4, 5, 4 },
            { 1, 2, 4, 2 },
            { 3, 8, 4, 8 },
            { 3, 4, 1, 4 },
            { 3, 6, 2, 6 },
            { 1, 4, 5, 4 },
            { 1, 8, 5, 8 },
            { 6, 1, 1, 1 },
            { 6, 4, 4, 4 },
            { 2, 2, 3, 2 },
            { 5, 8, 6, 8 },
            { 3, 2, 5, 2 },
            { 1, 8, 2, 8 },
            { 2, 3, 4, 3 },
            { 5, 5, 4, 5 },
            { 2, 5, 5, 5 },
            { 1, 7, 3, 7 },
            { 5, 1, 3, 1 },
            { 5, 5, 6, 5 },
            { 6, 7, 4, 7 },
            { 6, 8, 3, 8 },
            { 2, 3, 5, 3 },
            { 5, 6, 2, 6 },
            { 6, 8, 4, 8 },
            { 5, 1, 2, 1 },
            { 2, 8, 4, 8 },
            { 6, 2, 5, 2 },
            { 2, 5, 3, 5 },
            { 3, 8, 5, 8 },
            { 4, 7, 3, 7 },
            { 3, 1, 5, 1 },
            { 5, 1, 6, 1 },
            { 6, 3, 5, 3 },
            { 1, 6, 2, 6 },
            { 3, 5, 4, 5 },
            { 4, 4, 3, 4 },
            { 3, 6, 6, 6 },
            { 5, 5, 3, 5 },
            { 6, 8, 1, 8 },
            { 5, 7, 3, 7 },
            { 4, 5, 2, 5 },
            { 4, 8, 3, 8 },
            { 3, 3, 2, 3 },
            { 6, 8, 5, 8 },
            { 1, 3, 4, 3 },
            { 1, 4, 2, 4 },
            { 4, 2, 3, 2 },
            { 3, 1, 6, 1 },
            { 6, 5, 1, 5 },
            { 6, 7, 5, 7 },
            { 3, 6, 4, 6 },
            { 3, 3, 1, 3 },
            { 1, 1, 5, 1 },
            { 1, 8, 6, 8 },
            { 3, 5, 6, 5 },
            { 1, 5, 6, 5 },
            { 3, 3, 5, 3 },
            { 4, 4, 6, 4 },
            { 5, 2, 4, 2 },
            { 6, 2, 3, 2 },
            { 3, 8, 6, 8 },
            { 6, 3, 1, 3 },
            { 5, 1, 1, 1 },
            { 4, 3, 5, 3 },
            { 6, 8, 2, 8 },
            { 6, 1, 2, 1 },
            { 5, 5, 1, 5 },
            { 4, 2, 2, 2 },
            { 4, 6, 3, 6 },
            { 6, 7, 3, 7 },
            { 6, 5, 5, 5 },
            { 4, 8, 2, 8 },
            { 2, 5, 1, 5 },
            { 5, 4, 1, 4 },
            { 2, 7, 4, 7 },
            { 6, 4, 1, 4 },
            { 4, 2, 1, 2 },
            { 3, 3, 6, 3 },
            { 4, 3, 3, 3 },
            { 1, 5, 5, 5 },
            { 4, 7, 1, 7 },
            { 4, 6, 2, 6 },
            { 4, 6, 6, 6 },
            { 1, 3, 5, 3 },
            { 1, 8, 4, 8 },
            { 1, 5, 2, 5 },
            { 2, 7, 5, 7 },
            { 6, 5, 2, 5 },
            { 2, 8, 5, 8 },
            { 2, 4, 6, 4 },
            { 5, 8, 4, 8 },
            { 4, 4, 2, 4 },
            { 5, 8, 1, 8 },
            { 5, 2, 3, 2 },
            { 2, 1, 4, 1 },
            { 5, 3, 3, 3 },
            { 5, 6, 3, 6 },
            { 7, 6, 5, 6 }
    };

    private static final int[][] EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS = new int[][] {
            { 4, 3, 4, 3 },
            { 1, 1, 1, 1 },
            { 6, 6, 6, 6 },
            { 1, 5, 1, 5 },
            { 4, 5, 4, 5 },
            { 5, 3, 5, 3 },
            { 1, 3, 1, 3 },
            { 6, 7, 6, 7 },
            { 3, 7, 3, 7 },
            { 3, 4, 3, 4 },
            { 2, 3, 2, 3 },
            { 3, 1, 3, 1 },
            { 4, 4, 4, 4 },
            { 1, 6, 1, 6 },
            { 2, 1, 2, 1 },
            { 5, 2, 5, 2 },
            { 4, 8, 4, 8 },
            { 5, 8, 5, 8 },
            { 3, 2, 3, 2 },
            { 6, 1, 6, 1 },
            { 5, 7, 5, 7 },
            { 2, 7, 2, 7 },
            { 1, 4, 1, 4 },
            { 6, 4, 6, 4 },
            { 2, 6, 2, 6 },
            { 5, 5, 5, 5 },
            { 3, 3, 3, 3 },
            { 1, 8, 1, 8 },
            { 6, 5, 6, 5 },
            { 6, 8, 6, 8 },
            { 4, 1, 4, 1 },
            { 3, 8, 3, 8 },
            { 3, 5, 3, 5 },
            { 4, 7, 4, 7 },
            { 1, 7, 1, 7 },
            { 2, 4, 2, 4 },
            { 6, 2, 6, 2 },
            { 6, 3, 6, 3 },
            { 5, 6, 5, 6 },
            { 5, 4, 5, 4 },
            { 1, 2, 1, 2 },
            { 2, 2, 2, 2 },
            { 3, 6, 3, 6 },
            { 2, 5, 2, 5 },
            { 5, 1, 5, 1 },
            { 4, 6, 4, 6 },
            { 4, 2, 4, 2 },
            { 2, 8, 2, 8 }
    };

    private static final int[][] SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS = new int[][] {
            { 1, 8, 1, 4 },
            { 2, 8, 2, 3 },
            { 5, 8, 5, 6 },
            { 1, 8, 1, 7 },
            { 3, 1, 3, 5 },
            { 2, 7, 2, 5 },
            { 5, 3, 5, 1 },
            { 6, 7, 6, 3 },
            { 3, 4, 3, 1 },
            { 6, 5, 6, 8 },
            { 1, 7, 1, 5 },
            { 5, 6, 5, 4 },
            { 6, 8, 6, 7 },
            { 1, 8, 1, 2 },
            { 2, 7, 2, 2 },
            { 3, 5, 3, 4 },
            { 6, 6, 6, 8 },
            { 1, 3, 1, 6 },
            { 5, 4, 5, 1 },
            { 3, 5, 3, 8 },
            { 4, 1, 4, 5 },
            { 5, 2, 5, 1 },
            { 5, 2, 5, 4 },
            { 5, 6, 5, 8 },
            { 5, 8, 5, 7 },
            { 6, 6, 6, 4 },
            { 1, 5, 1, 4 },
            { 3, 4, 3, 7 },
            { 6, 7, 6, 1 },
            { 4, 1, 4, 7 },
            { 2, 4, 2, 1 },
            { 1, 1, 1, 2 },
            { 4, 2, 4, 7 },
            { 5, 3, 5, 2 },
            { 1, 1, 1, 3 },
            { 1, 4, 1, 7 },
            { 3, 6, 3, 5 },
            { 3, 3, 3, 5 },
            { 2, 6, 2, 5 },
            { 2, 3, 2, 1 },
            { 6, 1, 6, 3 },
            { 6, 5, 6, 3 },
            { 4, 3, 4, 2 },
            { 6, 6, 6, 7 },
            { 4, 7, 4, 5 },
            { 5, 7, 5, 6 },
            { 5, 3, 5, 8 },
            { 4, 6, 4, 4 },
            { 6, 2, 6, 4 },
            { 1, 2, 1, 8 },
            { 1, 7, 1, 1 },
            { 2, 3, 2, 5 },
            { 6, 4, 6, 3 },
            { 1, 7, 1, 6 },
            { 1, 7, 1, 3 },
            { 4, 5, 4, 4 },
            { 2, 1, 2, 5 },
            { 5, 4, 5, 3 },
            { 1, 1, 1, 7 },
            { 1, 2, 1, 5 },
            { 2, 2, 2, 1 },
            { 6, 7, 6, 5 },
            { 6, 3, 6, 6 },
            { 2, 8, 2, 2 },
            { 3, 5, 3, 6 },
            { 5, 6, 5, 5 },
            { 1, 6, 1, 4 },
            { 1, 3, 1, 7 },
            { 5, 5, 5, 6 },
            { 2, 2, 2, 5 },
            { 5, 6, 5, 1 },
            { 1, 1, 1, 5 },
            { 3, 7, 3, 1 },
            { 5, 1, 5, 6 },
            { 4, 2, 4, 6 },
            { 1, 7, 1, 8 },
            { 1, 4, 1, 2 },
            { 3, 7, 3, 8 },
            { 4, 2, 4, 5 },
            { 3, 4, 3, 6 },
            { 4, 7, 4, 4 },
            { 5, 1, 5, 4 },
            { 4, 1, 4, 3 },
            { 4, 8, 4, 6 },
            { 6, 3, 6, 1 },
            { 1, 5, 1, 8 },
            { 2, 6, 2, 1 },
            { 3, 8, 3, 5 },
            { 2, 2, 2, 7 },
            { 3, 8, 3, 6 },
            { 6, 8, 6, 5 },
            { 4, 7, 4, 3 },
            { 6, 1, 6, 8 },
            { 1, 3, 1, 1 },
            { 2, 8, 2, 5 },
            { 2, 8, 2, 6 },
            { 2, 1, 2, 2 },
            { 2, 7, 2, 4 },
            { 1, 5, 1, 6 },
            { 1, 2, 1, 6 },
            { 1, 4, 1, 3 },
            { 5, 5, 5, 1 },
            { 4, 6, 4, 7 },
            { 4, 6, 4, 1 },
            { 3, 6, 3, 3 },
            { 5, 2, 5, 8 },
            { 4, 5, 4, 7 },
            { 4, 1, 4, 4 },
            { 6, 5, 6, 1 },
            { 1, 6, 1, 1 },
            { 1, 7, 1, 4 },
            { 4, 8, 4, 1 },
            { 1, 3, 1, 8 },
            { 4, 2, 4, 1 },
            { 2, 3, 2, 7 },
            { 4, 8, 4, 3 },
            { 1, 6, 1, 8 },
            { 1, 6, 1, 7 },
            { 2, 6, 2, 3 },
            { 3, 2, 3, 8 },
            { 3, 7, 3, 6 },
            { 3, 6, 3, 2 },
            { 3, 2, 3, 5 },
            { 1, 8, 1, 5 },
            { 4, 7, 4, 1 },
            { 2, 6, 2, 4 },
            { 1, 1, 1, 6 },
            { 4, 8, 4, 2 },
            { 4, 5, 4, 2 },
            { 3, 3, 3, 6 },
            { 1, 6, 1, 5 },
            { 4, 1, 4, 6 },
            { 4, 2, 4, 3 },
            { 3, 8, 3, 3 },
            { 5, 3, 5, 7 },
            { 1, 3, 1, 4 },
            { 2, 6, 2, 8 },
            { 1, 2, 1, 7 },
            { 2, 5, 2, 8 },
            { 4, 8, 4, 7 },
            { 3, 3, 3, 2 },
            { 1, 2, 1, 3 },
            { 2, 4, 2, 7 },
            { 1, 4, 1, 1 },
            { 2, 5, 2, 3 },
            { 6, 1, 6, 6 },
            { 5, 8, 5, 3 },
            { 2, 1, 2, 4 },
            { 4, 3, 4, 8 },
            { 6, 8, 6, 1 },
            { 4, 5, 4, 8 },
            { 3, 3, 3, 8 },
            { 3, 6, 3, 8 },
            { 3, 5, 3, 1 },
            { 4, 7, 4, 6 },
            { 5, 5, 5, 2 },
            { 3, 8, 3, 1 },
            { 5, 5, 5, 7 },
            { 6, 2, 6, 7 },
            { 2, 4, 2, 3 },
            { 5, 7, 5, 2 },
            { 6, 3, 6, 5 },
            { 4, 8, 4, 5 },
            { 1, 4, 1, 5 },
            { 3, 7, 3, 2 },
            { 6, 2, 6, 8 },
            { 6, 1, 6, 4 }
    };
    private static final int[][] CONFLICT_DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS = new int[][] {
            { 2, 5, 1, 7 },
            { 5, 6, 4, 7 },
            { 3, 2, 1, 4 },
            { 1, 7, 3, 5 },
            { 3, 4, 1, 6 },
            { 5, 3, 2, 6 },
            { 4, 7, 3, 8 },
            { 6, 4, 2, 8 },
            { 6, 3, 2, 8 },
            { 1, 8, 6, 2 },
            { 2, 5, 4, 3 },
            { 1, 6, 5, 2 },
            { 3, 7, 6, 4 },
            { 1, 6, 4, 2 },
            { 3, 5, 1, 7 },
            { 1, 8, 3, 3 },
            { 2, 8, 4, 5 },
            { 5, 3, 1, 7 },
            { 5, 3, 2, 7 },
            { 4, 3, 1, 8 },
            { 1, 4, 3, 2 },
            { 5, 8, 6, 7 },
            { 1, 8, 5, 4 },
            { 4, 4, 2, 7 },
            { 1, 6, 2, 5 },
            { 1, 7, 2, 4 },
            { 2, 6, 5, 3 },
            { 3, 8, 5, 5 },
            { 3, 6, 5, 4 },
            { 3, 5, 4, 4 },
            { 5, 4, 3, 6 },
            { 2, 7, 4, 5 },
            { 4, 5, 2, 7 },
            { 4, 6, 2, 8 },
            { 2, 5, 1, 6 },
            { 1, 8, 2, 5 },
            { 3, 3, 1, 6 },
            { 2, 4, 1, 7 },
            { 6, 6, 5, 7 },
            { 1, 5, 3, 3 },
            { 4, 2, 1, 5 },
            { 1, 4, 2, 3 },
            { 5, 7, 4, 8 },
            { 2, 7, 5, 3 },
            { 4, 3, 1, 7 },
            { 3, 6, 4, 5 },
            { 6, 2, 1, 8 },
            { 4, 7, 6, 5 },
            { 3, 3, 1, 5 },
            { 2, 8, 4, 6 },
            { 3, 5, 2, 7 },
            { 2, 4, 1, 5 },
            { 2, 8, 3, 7 },
            { 2, 8, 6, 3 },
            { 1, 7, 5, 3 },
            { 4, 2, 1, 7 },
            { 2, 7, 4, 4 },
            { 3, 6, 2, 7 },
            { 2, 3, 1, 4 },
            { 2, 6, 1, 7 },
            { 3, 8, 5, 6 },
            { 1, 7, 4, 4 },
            { 4, 8, 5, 7 },
            { 6, 5, 3, 8 },
            { 5, 2, 1, 6 },
            { 3, 5, 1, 8 },
            { 3, 7, 5, 5 },
            { 2, 7, 5, 4 },
            { 5, 2, 1, 7 },
            { 1, 8, 2, 6 },
            { 3, 3, 1, 8 },
            { 1, 8, 6, 3 },
            { 6, 6, 4, 8 },
            { 6, 5, 4, 7 },
            { 1, 8, 3, 6 },
            { 5, 5, 3, 8 },
            { 2, 8, 3, 6 },
            { 1, 8, 3, 5 },
    };

    private static final int[][] CONFLICT_EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS = new int[][] {
            { 3, 8, 4, 6 },
            { 3, 4, 6, 2 },
            { 1, 8, 4, 2 },
            { 6, 1, 3, 2 },
            { 2, 2, 4, 1 },
            { 3, 4, 2, 6 },
            { 2, 8, 4, 4 },
            { 4, 6, 3, 8 },
            { 2, 6, 4, 3 },
            { 2, 6, 3, 4 },
            { 1, 6, 2, 3 },
            { 3, 2, 6, 1 },
            { 6, 4, 3, 8 },
            { 6, 2, 4, 3 },
            { 4, 3, 6, 2 },
            { 1, 4, 2, 2 },
            { 3, 8, 6, 4 },
            { 6, 1, 2, 3 },
            { 6, 2, 3, 4 },
            { 2, 3, 1, 6 },
            { 2, 3, 6, 1 },
            { 1, 8, 2, 4 },
            { 4, 1, 2, 2 },
            { 1, 6, 3, 2 },
            { 4, 2, 1, 8 },
            { 4, 3, 2, 6 },
            { 4, 4, 2, 8 },
            { 2, 2, 1, 4 },
            { 2, 4, 1, 8 },
            { 3, 2, 1, 6 }
    };

    private static final int[][] CONFLICT_SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS = new int[][] {
            { 3, 4, 5, 2 },
            { 6, 1, 5, 2 },
            { 5, 1, 3, 3 },
            { 3, 4, 6, 1 },
            { 5, 2, 3, 4 },
            { 6, 1, 3, 4 },
            { 2, 4, 6, 1 },
            { 6, 3, 5, 4 },
            { 6, 4, 5, 5 },
            { 6, 3, 4, 5 },
            { 4, 4, 5, 3 },
            { 3, 3, 5, 1 },
            { 2, 2, 3, 1 },
            { 3, 1, 2, 2 },
            { 3, 3, 6, 1 },
            { 5, 1, 2, 4 },
            { 5, 1, 2, 3 },
            { 4, 2, 3, 3 },
            { 6, 2, 3, 5 },
            { 5, 3, 4, 4 },
            { 4, 5, 6, 3 },
            { 4, 2, 5, 1 },
            { 4, 4, 6, 2 },
            { 3, 5, 6, 2 },
            { 3, 2, 4, 1 },
            { 5, 2, 6, 1 },
            { 3, 2, 5, 1 },
            { 6, 1, 4, 2 },
            { 5, 2, 4, 3 },
            { 5, 4, 6, 3 },
            { 6, 1, 4, 3 },
            { 4, 1, 3, 2 },
            { 3, 3, 4, 2 },
            { 5, 1, 4, 2 },
            { 5, 5, 6, 4 },
            { 4, 1, 2, 3 },
            { 6, 2, 4, 4 },
            { 2, 3, 5, 1 },
            { 6, 2, 5, 3 },
            { 2, 4, 5, 1 },
            { 5, 1, 3, 2 },
            { 6, 1, 2, 5 },
            { 4, 2, 6, 1 },
            { 6, 1, 2, 4 },
            { 6, 1, 3, 3 },
            { 5, 3, 6, 2 },
            { 4, 3, 6, 1 },
            { 2, 5, 6, 1 },
            { 4, 3, 5, 2 },
            { 2, 3, 4, 1 },
    };

    // Indexes into the various challenge sets.
    private static final Map<TiltPredictionChallengeType, Integer> NEXT_CHALLENGE_INDEXES = new HashMap<TiltPredictionChallengeType, Integer>() {{
        put( TiltPredictionChallengeType.DOMINATE, RAND.nextInt( DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length ) );
        put( TiltPredictionChallengeType.EQUAL, RAND.nextInt( EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS.length ) );
        put( TiltPredictionChallengeType.SUBORDINATE, RAND.nextInt( SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length ) );
        put( TiltPredictionChallengeType.CONFLICT_DOMINATE, RAND.nextInt( CONFLICT_DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length ) );
        put( TiltPredictionChallengeType.CONFLICT_EQUAL, RAND.nextInt( CONFLICT_EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS.length ) );
        put( TiltPredictionChallengeType.CONFLICT_SUBORDINATE, RAND.nextInt( CONFLICT_SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length ) );
    }};


    //-------------------------------------------------------------------------
    // Constructor(s)
    //-------------------------------------------------------------------------

    private TiltPredictionChallengeFactory() {
        // Not meant to be instantiated.
    }

    //-------------------------------------------------------------------------
    // Methods
    //-------------------------------------------------------------------------

    public static TiltPredictionChallenge getNextChallenge( TiltPredictionChallengeType challengeType ) {
        int[] challengeConfig;
        IModelComponentType modelComponentType;
        int index = NEXT_CHALLENGE_INDEXES.get( challengeType );
        String challengeID = challengeType.toString() + "-" + index;

        switch( challengeType ) {

            case DOMINATE:
                modelComponentType = tiltPredictionDominateChallenge;
                challengeConfig = DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS[index];
                NEXT_CHALLENGE_INDEXES.put( challengeType, ( index + 1 ) % DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length );
                break;

            case EQUAL:
                modelComponentType = tiltPredictionEqualChallenge;
                challengeConfig = EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS[index];
                NEXT_CHALLENGE_INDEXES.put( challengeType, ( index + 1 ) % EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS.length );
                break;

            case SUBORDINATE:
                modelComponentType = tiltPredictionSubordinateChallenge;
                challengeConfig = SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS[index];
                NEXT_CHALLENGE_INDEXES.put( challengeType, ( index + 1 ) % SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length );
                break;

            case CONFLICT_DOMINATE:
                modelComponentType = tiltPredictionConflictDominateChallenge;
                challengeConfig = CONFLICT_DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS[index];
                NEXT_CHALLENGE_INDEXES.put( challengeType, ( index + 1 ) % CONFLICT_DOMINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length );
                break;

            case CONFLICT_EQUAL:
                modelComponentType = tiltPredictionConflictEqualChallenge;
                challengeConfig = CONFLICT_EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS[index];
                NEXT_CHALLENGE_INDEXES.put( challengeType, ( index + 1 ) % CONFLICT_EQUAL_TILT_PREDICTION_CHALLENGE_CONFIGS.length );
                break;

            case CONFLICT_SUBORDINATE:
                modelComponentType = tiltPredictionConflictSubordinateChallenge;
                challengeConfig = CONFLICT_SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS[index];
                NEXT_CHALLENGE_INDEXES.put( challengeType, ( index + 1 ) % CONFLICT_SUBORDINATE_TILT_PREDICTION_CHALLENGE_CONFIGS.length );
                break;

            default:
                System.out.println( "Error: Unhandled challenge type" );
                modelComponentType = null;
                challengeConfig = null;
                break;
        }

        return generateChallengeFromConfigArray( challengeConfig, modelComponentType, challengeID );
    }

    private static TiltPredictionChallenge generateChallengeFromConfigArray( int[] config, IModelComponentType challengeType, String challengeID ) {
        return TiltPredictionChallenge.create( new BrickStack( config[0] ),
                                               config[1] * Plank.INTER_SNAP_TO_MARKER_DISTANCE,
                                               new BrickStack( config[2] ),
                                               -config[3] * Plank.INTER_SNAP_TO_MARKER_DISTANCE,
                                               challengeType,
                                               challengeID );
    }
}