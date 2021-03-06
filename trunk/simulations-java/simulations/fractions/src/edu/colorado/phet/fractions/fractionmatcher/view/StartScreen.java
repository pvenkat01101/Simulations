// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.fractionmatcher.view;

import fj.data.List;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import edu.colorado.phet.common.games.GameConstants;
import edu.colorado.phet.common.games.GameSettings;
import edu.colorado.phet.common.phetcommon.model.Resettable;
import edu.colorado.phet.common.phetcommon.model.property.BooleanProperty;
import edu.colorado.phet.common.phetcommon.util.IntegerRange;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction0;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.common.phetcommon.view.PhetColorScheme;
import edu.colorado.phet.common.phetcommon.view.util.BufferedImageUtils;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.common.piccolophet.activities.PActivityDelegateAdapter;
import edu.colorado.phet.common.piccolophet.nodes.PhetPText;
import edu.colorado.phet.common.piccolophet.nodes.ResetAllButtonNode;
import edu.colorado.phet.common.piccolophet.nodes.controlpanel.SettingsOnOffPanel;
import edu.colorado.phet.common.piccolophet.nodes.controlpanel.SettingsOnOffPanel.Feature;
import edu.colorado.phet.common.piccolophet.nodes.kit.ZeroOffsetNode;
import edu.colorado.phet.fractions.common.view.AbstractFractionsCanvas;
import edu.colorado.phet.fractions.fractionmatcher.model.MatchingGameModel;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.nodes.PImage;

import static edu.colorado.phet.common.phetcommon.view.util.BufferedImageUtils.multiScaleToWidth;
import static edu.colorado.phet.fractions.common.view.AbstractFractionsCanvas.*;
import static edu.colorado.phet.fractions.fractionsintro.FractionsIntroSimSharing.Components.soundRadioButton;
import static edu.colorado.phet.fractions.fractionsintro.FractionsIntroSimSharing.Components.timerRadioButton;

/**
 * Shows all the parts of the start screen, including title, level selection buttons, audio+timer buttons
 *
 * @author Sam Reid
 */
class StartScreen extends PNode {
    public StartScreen( final MatchingGameModel model, final String title, final List<PNode> patterns, final BooleanProperty audioEnabled, final Resettable resettable ) {

        //Animation when shown
        model.choosingSettings.addObserver( new VoidFunction1<Boolean>() {
            public void apply( final Boolean showStartScreen ) {
                if ( showStartScreen ) {
                    setVisible( true );
                    setOffset( -AbstractFractionsCanvas.STAGE_SIZE.getWidth(), 0 );
                    animateToPositionScaleRotation( 0, 0, 1, 0, 400 );
                }
                else {
                    animateToPositionScaleRotation( -AbstractFractionsCanvas.STAGE_SIZE.getWidth(), 0, 1, 0, 400 ).
                            setDelegate( new PActivityDelegateAdapter() {
                                public void activityFinished( final PActivity activity ) {
                                    setVisible( false );
                                }
                            } );
                }
            }
        } );
        setOffset( 0, 0 );

        //Game settings
        final GameSettings gameSettings = new GameSettings( new IntegerRange( 1, 8, 1 ), audioEnabled.get(), false );
        audioEnabled.addObserver( new VoidFunction1<Boolean>() {
            public void apply( final Boolean b ) {
                gameSettings.soundEnabled.set( b );
            }
        } );
        gameSettings.soundEnabled.addObserver( new VoidFunction1<Boolean>() {
            public void apply( final Boolean b ) {
                audioEnabled.set( b );
            }
        } );

        //Function invoked when the user pushes a level button to start the game.
        final VoidFunction0 startGame = new VoidFunction0() {
            public void apply() {
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        model.resumeOrStartGame( gameSettings.level.get(), gameSettings.soundEnabled.get(), gameSettings.timerEnabled.get() );
                    }
                } );
            }
        };

        //Dialog for selecting and starting a level
        final PNode levelSelectionDialog = new ZeroOffsetNode( new LevelSelectionNode( startGame, gameSettings, model.gameResults, patterns ) ) {{
            setOffset( STAGE_SIZE.getWidth() / 2 - getFullBounds().getWidth() / 2, STAGE_SIZE.getHeight() / 2 - getFullBounds().getHeight() / 2 );

        }};

        //Title text, only shown when the user is choosing a level
        final PNode titleText = new PNode() {{
            addChild( new PhetPText( title, new PhetFont( 38, true ) ) );

            setOffset( STAGE_SIZE.getWidth() / 2 - getFullBounds().getWidth() / 2, levelSelectionDialog.getFullBounds().getMinY() / 3 - getFullBounds().getHeight() / 2 );
        }};
        addChild( levelSelectionDialog );
        addChild( titleText );

        final int iconWidth = 30;
        final BufferedImage stopwatchIcon = multiScaleToWidth( GameConstants.STOPWATCH_ICON, iconWidth );
        final BufferedImage stopwatchOffIcon = redX( BufferedImageUtils.copyImage( stopwatchIcon ) );
        final BufferedImage soundIcon = multiScaleToWidth( GameConstants.SOUND_ICON, iconWidth );
        final BufferedImage soundOffIcon = multiScaleToWidth( GameConstants.SOUND_OFF_ICON, iconWidth );

        addChild( new SettingsOnOffPanel( List.list( new Feature( new PImage( stopwatchOffIcon ), new PImage( stopwatchIcon ), gameSettings.timerEnabled, timerRadioButton ),
                                                     new Feature( new PImage( soundOffIcon ), new PImage( soundIcon ), gameSettings.soundEnabled, soundRadioButton ) ) ) {{
            setOffset( INSET, STAGE_SIZE.height - getFullBounds().getHeight() - INSET );
        }} );

        addChild( new ResetAllButtonNode( resettable, null, CONTROL_FONT, Color.black, Color.orange ) {{
            setOffset( STAGE_SIZE.width - getFullBounds().getWidth() - INSET, STAGE_SIZE.height - getFullBounds().getHeight() - INSET );
            setConfirmationEnabled( false );
        }} );
    }

    private BufferedImage redX( final BufferedImage icon ) {
        Graphics2D g2 = icon.createGraphics();
        g2.setStroke( new BasicStroke( 2 ) );
        g2.setPaint( PhetColorScheme.RED_COLORBLIND );
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g2.drawLine( 0, 0, icon.getWidth(), icon.getHeight() );
        g2.drawLine( icon.getWidth(), 0, 0, icon.getHeight() );
        g2.dispose();
        return icon;
    }
}