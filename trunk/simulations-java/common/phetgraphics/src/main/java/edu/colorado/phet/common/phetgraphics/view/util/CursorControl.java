// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 54200 $
 * Date modified : $Date: 2011-07-19 06:15:40 +0530 (Tue, 19 Jul 2011) $
 */
package edu.colorado.phet.common.phetgraphics.view.util;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

/**
 * CursorControl
 *
 * @author ?
 * @version $Revision: 54200 $
 */
public class CursorControl implements MouseInputListener {
    private Cursor cursor;
    private Cursor exitCursor;

    public CursorControl() {
        this( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
    }

    public CursorControl( Cursor cursor ) {
        this( cursor, Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
    }

    public CursorControl( Cursor cursor, Cursor exitCursor ) {
        this.cursor = cursor;
        this.exitCursor = exitCursor;
    }

    public void mouseClicked( MouseEvent e ) {
    }

    public void mousePressed( MouseEvent e ) {
    }

    public void mouseReleased( MouseEvent e ) {
    }

    public void mouseEntered( MouseEvent e ) {
        e.getComponent().setCursor( cursor );
    }

    public void mouseExited( MouseEvent e ) {
        e.getComponent().setCursor( exitCursor );
    }

    public void mouseDragged( MouseEvent e ) {
    }

    public void mouseMoved( MouseEvent e ) {
    }

}
