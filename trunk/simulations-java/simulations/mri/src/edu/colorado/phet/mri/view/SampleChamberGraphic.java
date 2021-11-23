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

import edu.colorado.phet.mri.model.SampleChamber;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

import java.awt.*;

/**
 * SampleChamberGraphic
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class SampleChamberGraphic extends PNode {
    //    public static final Color BACKGROUND = new Color( 200, 200, 255 );
    public static final Color BACKGROUND = Color.white;

    public SampleChamberGraphic( SampleChamber sampleChamber ) {
        PNode boundsGraphic = new PPath( sampleChamber.getBounds() );
        boundsGraphic.setPaint( BACKGROUND );
        addChild( boundsGraphic );
    }

}
