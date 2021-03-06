// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.view;

import edu.colorado.phet.mri.util.RoundGradientPaint;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * DetectorPaint
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class DetectorPaint extends RoundGradientPaint {

    static Color[] grayScale = new Color[256];

    static {
        for( int i = 0; i < grayScale.length; i++ ) {
            grayScale[i] = new Color( i, i, i );
        }
    }

    public DetectorPaint( Point2D location, double width, double density, Color backgroundColor ) {
        super( location.getX(),
               location.getY(),
               grayScale[(int)( ( 1 - density ) * ( grayScale.length - 1 ) )],
               new Point2D.Double( 0, Math.max( 1, width * density / 2 ) ),
//               new Point2D.Double( 0, width * density / 2 ),
backgroundColor );
    }
}
