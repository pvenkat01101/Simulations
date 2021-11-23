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

import java.awt.*;

/**
 * DefaultGridBagConstraints
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class DefaultGridBagConstraints extends GridBagConstraints {

    public DefaultGridBagConstraints() {
        super( 0, 0, 1, 1, 1, 1,
               GridBagConstraints.NORTHWEST,
               GridBagConstraints.NONE,
               new Insets( 0, 0, 0, 0 ),
               0, 0 );
    }
}
