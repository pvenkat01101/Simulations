// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.modules;

import edu.colorado.phet.reactionsandrates.MRConfig;
import edu.colorado.phet.reactionsandrates.util.Resetable;

import javax.swing.*;
import java.awt.*;

/**
 * MRControlPanel
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
abstract public class MRControlPanel extends JPanel implements Resetable {

    public MRControlPanel( LayoutManager layout, boolean isDoubleBuffered ) {
        super( layout, isDoubleBuffered );
        init();
    }

    public MRControlPanel( LayoutManager layout ) {
        super( layout );
        init();
    }

    public MRControlPanel( boolean isDoubleBuffered ) {
        super( isDoubleBuffered );
        init();
    }

    public MRControlPanel() {
        super();
        init();
    }

    public boolean isTemperatureBeingAdjusted() {
        return false;
    }

    private void init() {
        Component strut = Box.createHorizontalStrut( MRConfig.CONTROL_PANEL_WIDTH );
        add( strut );
    }
}
