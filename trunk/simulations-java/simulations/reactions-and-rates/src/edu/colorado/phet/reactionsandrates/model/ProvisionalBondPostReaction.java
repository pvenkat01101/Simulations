// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.model;

/**
 * ProvisionalBondPostReaction
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class ProvisionalBondPostReaction extends ProvisionalBond {

    /**
     * @param sm1
     * @param sm2
     * @param maxBondLength
     * @param model
     * @param pe            Potential energy to be in the spring when its compressed
     */
    public ProvisionalBondPostReaction( SimpleMolecule sm1, SimpleMolecule sm2, double maxBondLength, MRModel model, double pe ) {
        super( sm1, sm2, maxBondLength, model, pe, true );
    }
}
