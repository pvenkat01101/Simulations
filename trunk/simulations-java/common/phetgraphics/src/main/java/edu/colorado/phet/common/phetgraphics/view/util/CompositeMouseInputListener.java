// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47761 $
 * Date modified : $Date: 2011-01-07 23:19:12 +0530 (Fri, 07 Jan 2011) $
 */
package edu.colorado.phet.common.phetgraphics.view.util;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.MouseInputListener;

/**
 * CompositeMouseInputListener
 *
 * @author ?
 * @version $Revision: 47761 $
 */
public class CompositeMouseInputListener implements MouseInputListener {
    private ArrayList listeners = new ArrayList();

    public void addMouseInputListener( MouseInputListener mil ) {
        listeners.add( mil );
    }

    public void removeMouseInputListener( MouseInputListener mil ) {
        listeners.remove( mil );
    }

    public void removeAllMouseInputListeners() {
        listeners.clear();
    }

    public int numMouseInputListeners() {
        return listeners.size();
    }

    public void mouseClicked( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mouseClicked( e );
        }
    }

    public void mousePressed( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mousePressed( e );
        }
    }

    public void mouseReleased( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mouseReleased( e );
        }
    }

    public void mouseEntered( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mouseEntered( e );
        }
    }

    public void mouseExited( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mouseExited( e );
        }
    }

    public void mouseDragged( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mouseDragged( e );
        }
    }

    public void mouseMoved( MouseEvent e ) {
        for ( int i = 0; i < listeners.size(); i++ ) {
            MouseInputListener mouseInputListener = (MouseInputListener) listeners.get( i );
            mouseInputListener.mouseMoved( e );
        }
    }

    //////////////////////////////////////////////////
    // For persistence
    //
    public ArrayList getListeners() {
        return listeners;
    }

    public void setListeners( ArrayList listeners ) {
        this.listeners = listeners;
    }
}
