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
import edu.colorado.phet.reactionsandrates.model.EnergyProfile;
import edu.colorado.phet.reactionsandrates.model.MRModel;
import edu.colorado.phet.reactionsandrates.model.SimpleMolecule;
import edu.colorado.phet.reactionsandrates.util.ModelElementGraphicManager;
import edu.colorado.phet.reactionsandrates.view.SpatialSimpleMoleculeGraphic;
import edu.umd.cs.piccolo.PNode;

/**
 * SimpleMoleculeGraphicFactory
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class SimpleMoleculeGraphicFactory extends ModelElementGraphicManager.GraphicFactory
        implements MRModel.ModelListener {

    private EnergyProfile profile;

    public SimpleMoleculeGraphicFactory( MRModel model, PNode moleculeLayer ) {
        super( SimpleMolecule.class, moleculeLayer );
        model.addListener( this );
        profile = model.getEnergyProfile();
    }

    public PNode createGraphic( ModelElement modelElement ) {
        return new SpatialSimpleMoleculeGraphic( (SimpleMolecule)modelElement,
                                                 profile );
    }

    public void notifyEnergyProfileChanged( EnergyProfile profile ) {
        this.profile = profile;
    }


    public void notifyDefaultTemperatureChanged( double newInitialTemperature ) {

    }
}
