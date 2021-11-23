// Copyright 2002-2012, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: olsonj $
 * Revision : $Revision: 66113 $
 * Date modified : $Date: 2012-07-21 14:57:33 +0530 (Sat, 21 Jul 2012) $
 */
package edu.colorado.phet.reactionsandrates.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import edu.colorado.phet.common.phetcommon.math.MathUtil;
import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;

/**
 * RandomMoleculeParamGenerator
 * <p/>
 * Generates a random position and velocity within specified rectangular bounds
 *
 * @author Ron LeMaster
 * @version $Revision: 66113 $
 */
public class RandomMoleculeParamGenerator implements MoleculeParamGenerator {
    private static Random random = new Random();

    private Rectangle2D bounds;
    private double maxSpeed;
    private double minTheta;
    private double maxTheta;
    private double maxAlpha;

    /**
     * Generates a Params object for a molecule.
     *
     * @param bounds   The bounds within which the molecule's CM lies
     * @param maxSpeed Max initial speed
     * @param maxAlpha Max angular velocity
     * @param minTheta Min angle of the molecule's initial velocity
     * @param maxTheta Max angle of the molecule's initial velocity
     */
    public RandomMoleculeParamGenerator( Rectangle2D bounds,
                                         double maxSpeed,
                                         double maxAlpha,
                                         double minTheta,
                                         double maxTheta ) {
        this.bounds = bounds;
        this.maxSpeed = maxSpeed;
        this.minTheta = minTheta;
        this.maxTheta = maxTheta;
        this.maxAlpha = maxAlpha;
    }

    public Params generate() {
        // Generate position
        double x = bounds.getMinX() + random.nextDouble() * bounds.getWidth();
        double y = bounds.getMinY() + random.nextDouble() * bounds.getHeight();
        Point2D p = new Point2D.Double( x, y );

        // Generate velocity
        double phi = ( maxTheta - minTheta ) * random.nextDouble() + minTheta;
        double speed = maxSpeed * random.nextDouble();
        MutableVector2D v = new MutableVector2D( speed, 0 ).rotate( phi );

        // Generate angular velocity
        double a = maxAlpha * random.nextDouble() * MathUtil.nextRandomSign();

        return new Params( p, v, a );
    }
}
