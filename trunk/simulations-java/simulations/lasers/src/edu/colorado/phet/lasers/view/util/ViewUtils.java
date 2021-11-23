// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.lasers.view.util;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * ViewUtils
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class ViewUtils {

    /**
     * Add an etched, titled border to a JPanel
     *
     * @param panel
     * @param title
     */
    public static void setBorder( JPanel panel, String title ) {
        EtchedBorder etchedBorder = new EtchedBorder();
        panel.setBorder( BorderFactory.createTitledBorder( etchedBorder, title ) );
    }
}
