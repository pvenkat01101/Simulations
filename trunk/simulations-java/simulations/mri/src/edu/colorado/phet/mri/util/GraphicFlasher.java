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

import edu.umd.cs.piccolo.PNode;

import javax.swing.*;

/**
 * GraphicFlasher
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */

public class GraphicFlasher extends Thread {
    private int numFlashes = 5;
    private PNode graphic;

    public GraphicFlasher( PNode graphic ) {
        this.graphic = graphic;
    }

    public void run() {
        try {
            for( int i = 0; i < numFlashes; i++ ) {
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        graphic.setVisible( false );
                    }
                } );
                Thread.sleep( 100 );
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        graphic.setVisible( true );
                    }
                } );
                Thread.sleep( 100 );
            }
        }
        catch( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}

