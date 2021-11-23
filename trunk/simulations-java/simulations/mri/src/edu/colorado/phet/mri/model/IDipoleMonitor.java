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

import java.util.List;

/**
 * IDipoleMonitor
 * <p/>
 * Methods for tracking the dipoles in the model
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public interface IDipoleMonitor {
    List getDipoles();

    List getUpDipoles();

    List getDownDipoles();
}
