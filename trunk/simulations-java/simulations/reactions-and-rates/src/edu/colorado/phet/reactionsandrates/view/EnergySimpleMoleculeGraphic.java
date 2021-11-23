// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.view;

import edu.colorado.phet.reactionsandrates.model.EnergyProfile;
import edu.colorado.phet.reactionsandrates.model.SimpleMolecule;

/**
 * ObservingMoleculeGraphic
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class EnergySimpleMoleculeGraphic extends ObservingMoleculeGraphic {

    private final SimpleMolecule molecule;
    private final EnergyProfile profile;

    public EnergySimpleMoleculeGraphic( SimpleMolecule molecule, EnergyProfile profile ) {
        super( molecule, profile );

        this.molecule = molecule;
        this.profile = profile;
    }

    public void update() {
        super.update();
    }

    public Object clone() {
        return new EnergySimpleMoleculeGraphic( (SimpleMolecule)molecule.clone(), profile );
    }
}
