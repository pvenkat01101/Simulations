// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.view.factories;

import edu.colorado.phet.common.phetcommon.model.ModelElement;
import edu.colorado.phet.reactionsandrates.model.Bond;
import edu.colorado.phet.reactionsandrates.util.ModelElementGraphicManager;
import edu.colorado.phet.reactionsandrates.view.BondGraphic;
import edu.umd.cs.piccolo.PNode;

/**
 * BondGraphicFactory
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class BondGraphicFactory extends ModelElementGraphicManager.GraphicFactory {

    public BondGraphicFactory( PNode layer ) {
        super( Bond.class, layer );
    }

    public PNode createGraphic( ModelElement modelElement ) {
        BondGraphic bondGraphic = null;
        if( modelElement instanceof Bond ) {
            bondGraphic = new BondGraphic( (Bond)modelElement );
        }
        return bondGraphic;
    }
}
