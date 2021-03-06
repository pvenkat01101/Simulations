package edu.colorado.phet.circuitconstructionkit.view.piccolo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Send sim sharing messages, but not too often and don't forget the last one
 * (it is important)
 */
public class DelayedRunner {

    private static final int MINIMUM_INTER_MESSAGE_DELAY = 500; // In milliseconds.

    private Runnable runnable;
    private Timer timer = new Timer( MINIMUM_INTER_MESSAGE_DELAY, new ActionListener() {
        public void actionPerformed( ActionEvent e ) {
            if ( runnable != null ) {
                runnable.run();
                runnable = null;
            }
            timer.stop();
        }
    } );

    public DelayedRunner() {
        timer.setRepeats( false );
    }

    public void set( Runnable runnable ) {
        this.runnable = runnable;
        if ( !timer.isRunning() ) {
            timer.start();
        }
    }

    //Dragging ended, so don't send any more messages (even if one stored up)
    public void terminate() {
        timer.stop();
        runnable = null;
    }
}
