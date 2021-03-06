// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.movingman;

import edu.colorado.phet.common.phetcommon.audio.PhetAudioClip;

/**
 * @author Sam Reid
 */
public class CrashSound {

    private static final PhetAudioClip clip = MovingManResources.getInstance().getAudioClip( "crash.wav" );

    public static void play() {
        clip.play();
    }
}