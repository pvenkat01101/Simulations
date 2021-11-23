// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.model;

import edu.colorado.phet.mri.util.MriUtil;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Tumor
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class Tumor extends Ellipse2D.Double {
    private List dipoles;

    public Tumor() {
    }

    public Tumor( double x, double y, double w, double h ) {
        super( x, y, w, h );
        List tempDipoles = MriUtil.createDipolesForEllipse( this, 20 );

        // Make them all TumorDipoles
        dipoles = new ArrayList();
        for( int i = 0; i < tempDipoles.size(); i++ ) {
            Dipole dipole = (Dipole)tempDipoles.get( i );
            TumorDipole tumorDipole = new TumorDipole();
            tumorDipole.setPosition( dipole.getPosition() );
            tumorDipole.setSpin( dipole.getSpin() );
            dipoles.add( tumorDipole );
        }
        tempDipoles.clear();
    }

    public List getDipoles() {
        return dipoles;
    }
}
