// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.common.phetcommon.simsharing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sends messages to a log file, see #3215
 *
 * @author Sam Reid
 */
public class SimSharingFileLogger implements Log {
    private final String machineCookie;
    private final String sessionId;
    private boolean nearJAR;
    private BufferedWriter logWriter;
    private File file;

    public SimSharingFileLogger( String machineCookie, String sessionId, boolean nearJAR ) {
        this.machineCookie = machineCookie;
        this.sessionId = sessionId;
        this.nearJAR = nearJAR;
    }

    private void createLogWriter() {
        if ( nearJAR ) {
            File base;
            try {
                base = new File( SimSharingFileLogger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() ).getParentFile();
            }
            catch( URISyntaxException e ) {
                throw new RuntimeException( e );
            }
            file = new File( base, "phet-logs/" + new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" ).format( new Date() ) + "_" + machineCookie + "_" + sessionId + ".txt" );
        }
        else {
            file = new File( System.getProperty( "user.home" ), "phet-logs/" +
                                                                new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" ).format( new Date() ) + "_" + machineCookie + "_" + sessionId + ".txt" );
        }

        file.getParentFile().mkdirs();
        System.out.println( "Logging sim-sharing messages to file: " + file );//TODO this should use logger
        try {
            file.createNewFile();
            logWriter = new BufferedWriter( new FileWriter( file, true ) );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public void addMessage( SimSharingMessage message ) throws IOException {
        if ( logWriter == null ) {
            createLogWriter();
        }
        if ( logWriter != null ) {
            logWriter.write( message.toString() );
            logWriter.newLine();
            logWriter.flush();
        }
    }

    public String getName() {
        return "file: " + ( file == null ? "null" : file.getAbsolutePath() );
    }

    public void shutdown() {
        try {
            logWriter.close();
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }
}