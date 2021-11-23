// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47771 $
 * Date modified : $Date: 2011-01-08 00:46:26 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.greenhouse.filter;


/**
 * IrPassFilter
 *
 * @author Ron LeMaster
 * @version $Revision: 47771 $
 */
public class IrFilter extends Filter1D {
    public IrFilter() {
    }

    public boolean passes( double value ) {
        return value < 800E-9 || value > 1500E-9;
    }
}
