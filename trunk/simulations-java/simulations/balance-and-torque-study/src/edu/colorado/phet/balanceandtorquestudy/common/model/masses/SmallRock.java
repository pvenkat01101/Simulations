// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.balanceandtorquestudy.common.model.masses;

import java.awt.geom.Point2D;

import edu.colorado.phet.balanceandtorquestudy.BalanceAndTorqueStudyResources.Images;
import edu.colorado.phet.common.phetcommon.simsharing.messages.UserComponentChain;

import static edu.colorado.phet.balanceandtorquestudy.BalanceAndTorqueSimSharing.UserComponents.smallRock;


/**
 * Model class that represents a small rock.
 *
 * @author John Blanco
 */
public class SmallRock extends ImageMass {

    private static final double MASS = 30; // in kg
    private static final double HEIGHT = 0.25; // In meters.

    // For sim sharing - tracks the number of instances created, used in the
    // component ID for each instance.
    private static int instanceCount = 0;

    public SmallRock( boolean isMystery ) {
        super( UserComponentChain.chain( smallRock, instanceCount++ ), MASS, Images.ROCK_4, HEIGHT, new Point2D.Double( 0, 0 ), isMystery );
    }
}
