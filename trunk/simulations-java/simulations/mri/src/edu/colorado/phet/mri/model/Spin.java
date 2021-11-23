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

/**
 * Spin
 * <p/>
 * An enumeration class.
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class Spin {

    // The lower energy state
    public static final Spin DOWN = new Spin();
    // the higher energy state
    public static final Spin UP = new Spin();

    /**
     * No public or protected constructors
     */
    private Spin() {
    }
}
