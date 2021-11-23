// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.reactionsandrates.test;/* Copyright 2003-2004, University of Colorado */

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */

import javax.swing.*;
import java.awt.*;

/**
 * ScrollBarTest
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public class ScrollBarTest {

    public static void main( String[] args ) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 300 );

        JScrollBar jsb = new JScrollBar( JScrollBar.HORIZONTAL, 200, 20, 0, 800 );
        jsb.setPreferredSize( new Dimension( 400, 15 ) );

        frame.setContentPane( new JPanel() );
        frame.getContentPane().add( jsb );

        frame.setVisible( true );


    }
}
