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

import java.awt.Dimension;

import edu.colorado.phet.common.piccolophet.util.PImageFactory;
import edu.colorado.phet.mri.MriConfig;
import edu.colorado.phet.mri.MriResources;
import edu.colorado.phet.mri.model.Head;
import edu.umd.cs.piccolo.PNode;

/**
 * HeadGraphic
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class HeadGraphic extends PNode {

    public HeadGraphic() {
//        PNode headGraphic = PImageFactory.createPrimaryBorder( MriConfig.HEAD_IMAGE );
        PNode headGraphic = PImageFactory.create( MriResources.getImage( MriConfig.HEAD_IMAGE ),
                                                  new Dimension( (int)MriConfig.SAMPLE_CHAMBER_WIDTH,
                                                                 (int)MriConfig.SAMPLE_CHAMBER_WIDTH ) );
        addChild( headGraphic );
    }

    public HeadGraphic( Head head ) {
        PNode headGraphic = PImageFactory.create( MriResources.getImage( MriConfig.HEAD_IMAGE ),
                                                  new Dimension( (int)head.getBounds().getWidth(),
                                                                 (int)head.getBounds().getHeight() ) );
        addChild( headGraphic );
    }
}
