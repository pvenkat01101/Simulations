// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.bendinglight.modules.prisms;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.util.Option;

/**
 * @author Sam Reid
 */
public interface IShape {
    //Convert to a java.awt.Shape
    Shape toShape();

    //Create a new Polygon translated by the specified amount
    IShape getTranslatedInstance( double dx, double dy );

    //Compute the intersections of the specified ray with this polygon's edges
    ArrayList<Intersection> getIntersections( Ray ray );

    Rectangle2D getBounds();

    //Gets a rotated copy of this polygon about the rotationPoint
    IShape getRotatedInstance( double angle, Vector2D rotationPoint );

    //Determines the point about which the shape should be rotated
    Vector2D getRotationCenter();

    //Gets a point that will be used to place the rotation drag handle (or None if not rotatable, like for circles)
    Option<Vector2D> getReferencePoint();

    boolean containsPoint( Vector2D point );
}