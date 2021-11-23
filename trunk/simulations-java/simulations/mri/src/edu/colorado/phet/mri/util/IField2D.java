// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.mri.util;

import java.awt.geom.Point2D;

/**
 * IField2D
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public interface IField2D {
    double getValueAt( Point2D p );
}
