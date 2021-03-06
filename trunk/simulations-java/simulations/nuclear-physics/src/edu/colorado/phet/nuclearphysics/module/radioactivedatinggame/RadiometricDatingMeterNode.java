// Copyright 2002-2012, University of Colorado

package edu.colorado.phet.nuclearphysics.module.radioactivedatinggame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;
import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.util.SimpleObserver;
import edu.colorado.phet.common.phetcommon.view.HorizontalLayoutPanel;
import edu.colorado.phet.common.phetcommon.view.VerticalLayoutPanel;
import edu.colorado.phet.common.phetcommon.view.graphics.transforms.ModelViewTransform2D;
import edu.colorado.phet.common.phetcommon.view.util.DoubleGeneralPath;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.common.piccolophet.PhetPNode;
import edu.colorado.phet.common.piccolophet.event.CursorHandler;
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.colorado.phet.nuclearphysics.NuclearPhysicsConstants;
import edu.colorado.phet.nuclearphysics.NuclearPhysicsResources;
import edu.colorado.phet.nuclearphysics.NuclearPhysicsStrings;
import edu.colorado.phet.nuclearphysics.common.NucleusDisplayInfo;
import edu.colorado.phet.nuclearphysics.common.NucleusType;
import edu.colorado.phet.nuclearphysics.model.HalfLifeInfo;
import edu.colorado.phet.nuclearphysics.module.radioactivedatinggame.RadiometricDatingMeter.MeasurementMode;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolox.pswing.PComboBox;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

/**
 * This class represents a node that the user can interact with in order to
 * date various items that are available in the play area.
 *
 * @author John Blanco
 */
public class RadiometricDatingMeterNode extends PNode {

    //------------------------------------------------------------------------
    // Class Data
    //------------------------------------------------------------------------

    private static final Color BODY_COLOR = Color.DARK_GRAY;
    private static final double READOUT_WIDTH_PROPORTION = 0.80;
    private static final double READOUT_HEIGHT_PROPORTION = 0.16;
    private static final double PROBE_SIZE_SCALE_FACTOR = 0.9;  // Adjust in order to change size of probe.
    private static final Font HALF_LIFE_SELECTION_FONT = new PhetFont( 16 );
    private static final double AIR_PROBE_HEIGHT_PROPORTION = 0.5;
    private static final double MAX_AIR_PROBE_EXTENSION_PROPORTION = 0.333;

    // Array that maps values to the strings used for the custom nucleus half
    // life.  THESE MUST BE MANUALLY KEPT IN SYNC WITH THE STRINGS IN THE
    // LOCALIZATION FILE.
    private static final ValueStringPair[] HALF_LIFE_VALUE_STRING_PAIRS = {
            new ValueStringPair( NuclearPhysicsConstants.DEFAULT_CUSTOM_NUCLEUS_HALF_LIFE, NuclearPhysicsStrings.HALF_LIFE_100_THOUSAND_YEARS ),
            new ValueStringPair( HalfLifeInfo.convertYearsToMs( 1E6 ), NuclearPhysicsStrings.HALF_LIFE_1_MILLION_YEARS ),
            new ValueStringPair( HalfLifeInfo.convertYearsToMs( 10E6 ), NuclearPhysicsStrings.HALF_LIFE_10_MILLION_YEARS ),
            new ValueStringPair( HalfLifeInfo.convertYearsToMs( 100E6 ), NuclearPhysicsStrings.HALF_LIFE_100_MILLION_YEARS ),
    };

    //------------------------------------------------------------------------
    // Instance Data
    //------------------------------------------------------------------------

    private RadiometricDatingMeter _meterModel;
    private ModelViewTransform2D _mvt;
    private PNode _airProbeLayer;
    private PNode _meterBody;
    private ProbeNode _objectProbeNode;
    private ProbeCableNode _cableNode;
    private PercentageDisplayNode _percentageDisplay;
    private ElementSelectionPanel _elementSelectionPanel;
    private PSwing _elementSelectionNode;
    private PComboBox _halfLifeComboBox;
    private PSwing _halfLifeComboBoxPSwing = null;
    private PNode _airProbeNode;
    @SuppressWarnings("unused")
    private MeasurementModeToggleAnimationTimer _animationTimer = null;
    private PText _halfLifeSelectionLabel;
    private boolean _showCustom = false;

    //------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------

    public RadiometricDatingMeterNode( RadiometricDatingMeter meterModel, double width, double height,
                                       ModelViewTransform2D mvt, PSwingCanvas canvas, boolean showCustom, PNode probeDragBounds ) {

        _meterModel = meterModel;
        _mvt = mvt;
        _showCustom = showCustom;

        // Register with the model to find out when something new is being touched.
        _meterModel.addListener( new RadiometricDatingMeter.Adapter() {
            @Override
            public void datingElementChanged() {
                handleDatingElementChanged();
            }

            @Override
            public void touchedStateChanged() {
                updateMeterReading();
            }

            @Override
            public void readingChanged() {
                updateMeterReading();
            }

            @Override
            public void measurementModeChanged() {
                handleMeasurementModeChanged();
            }
        } );

        _airProbeLayer = new PNode();
        addChild( _airProbeLayer );     // Add first so that the air probe appears behind the meter body.
        _meterBody = new PNode();
        addChild( _meterBody );

        // Create the main body of the meter.
        PPath mainBody = new PPath( new RoundRectangle2D.Double( 0, 0, width, height, width / 5, width / 5 ) );
        mainBody.setPaint( BODY_COLOR );
        _meterBody.addChild( mainBody );

        // Add the display.
        _percentageDisplay = new PercentageDisplayNode( width * READOUT_WIDTH_PROPORTION,
                                                        height * READOUT_HEIGHT_PROPORTION );
        _percentageDisplay.setOffset( mainBody.getFullBounds().width / 2 - _percentageDisplay.getFullBounds().width / 2,
                                      mainBody.getHeight() * 0.06 );
        _meterBody.addChild( _percentageDisplay );
        _percentageDisplay.setPercentage( 100 );

        // Add the selection panel.
        _elementSelectionPanel = new ElementSelectionPanel( meterModel, _showCustom );
        _elementSelectionNode = new PSwing( _elementSelectionPanel );
        double scale = Math.min(
                _meterBody.getFullBoundsReference().width * READOUT_WIDTH_PROPORTION /
                _elementSelectionNode.getFullBoundsReference().width,
                _meterBody.getFullBoundsReference().height * 0.5 /
                _elementSelectionNode.getFullBoundsReference().height );
        _elementSelectionNode.setScale( scale );
        _elementSelectionNode.setOffset(
                _meterBody.getFullBounds().width / 2 - _elementSelectionNode.getFullBounds().width / 2,
                _percentageDisplay.getFullBounds().getMaxY() );
        _meterBody.addChild( _elementSelectionNode );

        // Add the combo box that allows the user to specify the half life of
        // the custom element.  NOTE THAT THIS IS PART OF A HACK THIS IS
        // NECESSARY DUE TO PROBLEMS WITH COMBO BOXES ON PICCOLO CANVASES.
        // Ideally, this combo box would be part of the element selection
        // panel, but problems with that approach necessitated its extraction
        // into a separate PSwing.
        if ( _showCustom ) {
            _halfLifeComboBox = new PComboBox( HALF_LIFE_VALUE_STRING_PAIRS );
            _halfLifeComboBox.setFont( HALF_LIFE_SELECTION_FONT );
            _halfLifeComboBoxPSwing = new PSwing( _halfLifeComboBox );
            _halfLifeComboBox.setEnvironment( _halfLifeComboBoxPSwing, canvas );
            _meterBody.addChild( _halfLifeComboBoxPSwing );
            _halfLifeComboBoxPSwing.setOffset(
                    _elementSelectionNode.getFullBoundsReference().getMaxX()
                    - _halfLifeComboBoxPSwing.getFullBoundsReference().width,
                    _elementSelectionNode.getFullBoundsReference().getMaxY() );
            _halfLifeSelectionLabel = new PText( NuclearPhysicsStrings.METER_HALF_LIFE_EQUALS );
            _halfLifeSelectionLabel.setFont( HALF_LIFE_SELECTION_FONT );
            _halfLifeSelectionLabel.setTextPaint( Color.WHITE );
            _halfLifeSelectionLabel.setOffset(
                    _halfLifeComboBoxPSwing.getXOffset() - _halfLifeSelectionLabel.getFullBoundsReference().width,
                    _elementSelectionNode.getFullBoundsReference().getMaxY() );
            _meterBody.addChild( _halfLifeSelectionLabel );

            // Hook up the handler for changes to the custom half life setting.
            _halfLifeComboBox.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    handleUserChangedHalfLife();
                }
            } );
            _halfLifeComboBox.setEnabled( false );
        }

        // Add the panel that will allow the user to switch between measuring
        // the air or measuring the datable items.
        MeasurementSelectionPanel measurementSelectionPanel = new MeasurementSelectionPanel( meterModel, this );
        PSwing measurementSelectionNode = new PSwing( measurementSelectionPanel );
        // Scale the panel to be the same size as the element selection panel.
        // For some reason, this needs a bit of a tweak factor to look correct.
        measurementSelectionNode.setScale( _meterBody.getFullBoundsReference().width * READOUT_WIDTH_PROPORTION /
                                           measurementSelectionNode.getFullBoundsReference().width );
        // Position the panel.
        measurementSelectionNode.setOffset(
                _meterBody.getFullBoundsReference().width / 2 -
                measurementSelectionNode.getFullBoundsReference().width / 2,
                _meterBody.getFullBoundsReference().getMaxY() -
                measurementSelectionNode.getFullBoundsReference().height * 1.1 );

        _meterBody.addChild( measurementSelectionNode );

        // Add the object probe.
        _objectProbeNode = new ProbeNode( _meterModel.getProbeModel(), _mvt, probeDragBounds );
        addChild( _objectProbeNode );

        // Create the cable that visually attaches the object probe to the meter.
        _cableNode = new ProbeCableNode( this );
        addChild( _cableNode );

        // Add the air probe.
        _airProbeNode = new PImage( NuclearPhysicsResources.getImage( "air_probe.png" ) );
        double airProbeScale = ( _meterBody.getFullBoundsReference().getHeight() * AIR_PROBE_HEIGHT_PROPORTION )
                               / _airProbeNode.getFullBoundsReference().height;
        _airProbeNode.setScale( airProbeScale );
        setAirProbeExtensionAmount( 0 );
        _airProbeLayer.addChild( _airProbeNode );

        // Do initial state updates.
        updateProbeVisibility();
        updateMeterReading();
        updateHalfLifeSelectorVisibility();
    }

    //------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------

    public Point2D getProbeTailLocation() {
        return ( _meterBody.localToParent( _objectProbeNode.getTailLocation() ) );
    }

    /**
     * Handle the event generated when the user changes the half life of the
     * custom nucleus through the combo box by setting the half life in the
     * meter model.
     */
    private void handleUserChangedHalfLife() {
        _meterModel.setHalfLifeForCustomNucleus( HALF_LIFE_VALUE_STRING_PAIRS[_halfLifeComboBox.getSelectedIndex()].value );
    }

    /**
     * Handle a notification from the meter model that the dating element has
     * changed.
     */
    private void handleDatingElementChanged() {
        if ( _halfLifeComboBox != null ) {
            if ( _meterModel.getNucleusTypeUsedForDating() == NucleusType.HEAVY_CUSTOM ) {
                _halfLifeComboBox.setEnabled( true );
                if ( _halfLifeComboBox.getSelectedIndex() == -1 ) {
                    _halfLifeComboBox.setSelectedIndex( 0 );
                }
            }
            else {
                _halfLifeComboBox.setEnabled( false );
            }
        }

        updateMeterReading();
        updateHalfLifeSelectorVisibility();
    }

    private void updateHalfLifeSelectorVisibility() {
        if ( _showCustom ) {
            _halfLifeComboBoxPSwing.setVisible( _meterModel.getNucleusTypeUsedForDating() == NucleusType.HEAVY_CUSTOM );
            _halfLifeSelectionLabel.setVisible( _meterModel.getNucleusTypeUsedForDating() == NucleusType.HEAVY_CUSTOM );
        }
    }

    private void handleMeasurementModeChanged() {
        updateProbeVisibility();
    }

    private void updateProbeVisibility() {
        switch( _meterModel.getMeasurementMode() ) {
            case AIR:
                _airProbeNode.setVisible( true );
                _objectProbeNode.setVisible( false );
                _cableNode.setVisible( false );
                break;
            case OBJECTS:
                _airProbeNode.setVisible( false );
                _objectProbeNode.setVisible( true );
                _cableNode.setVisible( true );
                break;
        }
    }

    private void setAirProbeExtensionAmount( double extension ) {
        // Check for valid usage.
        if ( extension < 0 || extension > 1 ) {
            System.err.println( getClass().getName() + " - Error: Invalid extension amount" );
            assert false;
            return;
        }

        // Set the position.
        PBounds meterBodyBounds = _meterBody.getFullBounds();
        PBounds airProbeBounds = _airProbeNode.getFullBounds();
        double maxExtension = _airProbeNode.getFullBoundsReference().height * MAX_AIR_PROBE_EXTENSION_PROPORTION;
        _airProbeNode.setOffset( meterBodyBounds.getWidth() / 2 - airProbeBounds.width / 2,
                                 meterBodyBounds.height - airProbeBounds.height + ( extension * maxExtension ) );
    }

    private ProbeNode getProbeNode() {
        return _objectProbeNode;
    }

    private void updateMeterReading() {
        DatableItem datableItem = _meterModel.getItemBeingTouched();
        if ( datableItem == null ) {
            _percentageDisplay.setBlank();
        }
        else {
            _percentageDisplay.setPercentage( _meterModel.getPercentageOfDatingElementRemaining() );
        }
    }

    private void startProbeChangeAnimation() {
        MeasurementMode targetMode;
        if ( _meterModel.getMeasurementMode() == MeasurementMode.AIR ) {
            targetMode = MeasurementMode.OBJECTS;
        }
        else {
            targetMode = MeasurementMode.AIR;
        }
        _animationTimer = new MeasurementModeToggleAnimationTimer( this, targetMode );
    }

    /**
     * Class that represents the percentage display readout.
     */
    private static class PercentageDisplayNode extends PNode {

        private final Color BACKGROUND_COLOR = new Color( 255, 255, 255 );
        private final Font TIME_FONT = new PhetFont( Font.BOLD, 26 );

        private PPath _background;
        private RoundRectangle2D _backgroundShape;
        private double _currentPercentage;
        private PText _percentageText;
        private DecimalFormat _percentageFormatterOneDecimal = new DecimalFormat( "##0.0" );

        PercentageDisplayNode( double width, double height ) {
            _backgroundShape = new RoundRectangle2D.Double( 0, 0, width, height, 8, 8 );
            _background = new PPath( _backgroundShape );
            _background.setPaint( BACKGROUND_COLOR );
            addChild( _background );
            _percentageText = new PText();
            _percentageText.setFont( TIME_FONT );
            addChild( _percentageText );
        }

        public void setPercentage( double percentage ) {

            _currentPercentage = percentage;

            _percentageText.setText( _percentageFormatterOneDecimal.format( _currentPercentage ) + " %" );

            scaleAndCenterText();
        }

        /**
         * Set the display to indicate that the meter is not probing anything.
         */
        public void setBlank() {
            _percentageText.setText( "-----" );
            scaleAndCenterText();
        }

        private void scaleAndCenterText() {
            if ( _percentageText.getFullBoundsReference().width == 0 ||
                 _percentageText.getFullBoundsReference().height == 0 ) {

                // Avoid divide by 0 errors.
                return;
            }

            _percentageText.setScale( 1 );
            double newScale = Math.min(
                    _backgroundShape.getWidth() * 0.9 / _percentageText.getFullBoundsReference().width,
                    _backgroundShape.getHeight() * 0.9 / _percentageText.getFullBoundsReference().height );
            _percentageText.setScale( newScale );
            _percentageText.setOffset(
                    _background.getFullBoundsReference().width / 2 - _percentageText.getFullBoundsReference().width / 2,
                    _background.getFullBoundsReference().height / 2 - _percentageText.getFullBoundsReference().height / 2 );
        }
    }

    /**
     * Node that represents the probe portion of the meter.
     */
    static class ProbeNode extends PhetPNode {
        private final RadiometricDatingMeter.ObjectProbe _probeModel;
        private final PImage _imageNode;
        private final PhetPPath _tipPath;
        private final ModelViewTransform2D _mvt;
        private final PNode _dragBounds;

        public ProbeNode( RadiometricDatingMeter.ObjectProbe probeModel, ModelViewTransform2D mvt,
                          PNode probeDragBounds ) {

            _probeModel = probeModel;
            _mvt = mvt;
            _dragBounds = probeDragBounds;

            _imageNode = NuclearPhysicsResources.getImageNode( "geiger_probe_6.png" );
            _imageNode.rotateAboutPoint( probeModel.getAngle(), 0.1, 0.1 );
            _imageNode.scale( PROBE_SIZE_SCALE_FACTOR );
            addChild( _imageNode );
            probeModel.addObserver( new SimpleObserver() {
                public void update() {
                    updateProbe();
                }
            } );

            addInputEventListener( new PBasicInputEventHandler() {
                public void mouseDragged( PInputEvent event ) {
                    handleMouseDragged( event );
                }
            } );
            addInputEventListener( new CursorHandler() );

            _tipPath = new PhetPPath( Color.lightGray );
            addChild( _tipPath );

            updateProbe();
        }

        private void handleMouseDragged( PInputEvent event ) {
            PDimension pt = event.getDeltaRelativeTo( ProbeNode.this.getParent() );
            boolean movementAllowed = true;
            Rectangle2D boundaryRect = localToGlobal( _dragBounds.getFullBounds() );
            Rectangle2D probeRect = localToGlobal( _imageNode.getFullBounds() );

            // Only allow the probe to be moved if it is within the bounding
            // rectangle.  This prevents the probe from being dragged off the
            // canvas where the user can't recover it.
            if ( pt.width > 0 && probeRect.getMaxX() >= boundaryRect.getMaxX() ) {
                movementAllowed = false;
            }
            else if ( pt.width < 0 && probeRect.getMinX() <= boundaryRect.getMinX() ) {
                movementAllowed = false;
            }
            if ( pt.height > 0 && probeRect.getMaxY() >= boundaryRect.getMaxY() ) {
                movementAllowed = false;
            }
            else if ( pt.height < 0 && probeRect.getMinY() <= boundaryRect.getMinY() ) {
                movementAllowed = false;
            }
            if ( movementAllowed ) {
                // Move the probe in the model based on the user's dragging.
                _probeModel.translate( _mvt.viewToModelDifferentialX( pt.width ),
                                       _mvt.viewToModelDifferentialY( pt.height ) );
            }
        }

        private void updateProbe() {
            double dx = _imageNode.getImage().getWidth( null ) * PROBE_SIZE_SCALE_FACTOR * Math.cos( _probeModel.getAngle() ) / 2;
            double dy = _imageNode.getImage().getWidth( null ) * PROBE_SIZE_SCALE_FACTOR * Math.sin( _probeModel.getAngle() ) / 2;
            _imageNode.setOffset( _mvt.modelToViewXDouble( _probeModel.getTipLocation().getX() ) - dx,
                                  _mvt.modelToViewYDouble( _probeModel.getTipLocation().getY() ) - dy );

            _tipPath.setPathTo( _probeModel.getTipShape() );
        }

        /**
         * Get the location of the tail of the probe.  Note that this WILL NOT
         * WORK for all orientations of the probe, so if a more general
         * implementation is needed, feel free to expand on this.
         *
         * @return
         */
        public Point2D getTailLocation() {
            Point2D pt = new Point2D.Double( _imageNode.getWidth() / 2, _imageNode.getHeight() );
            _imageNode.localToParent( pt );
            localToParent( pt );
            return new Point2D.Double( pt.getX(), pt.getY() );
        }
    }

    public void setMeterBodyOffset( double x, double y ) {
        _meterBody.setOffset( x, y );
        _airProbeLayer.setOffset( x, y );
    }

    public Point2D getMeterBodyOffset() {
        return _meterBody.getOffset();
    }

    private PNode getMeterBodyNode() {
        return _meterBody;
    }

    /**
     * Get the size of the meter node excluding the probe and cable.  This is
     * useful when positioning things on a canvas relative to the meter.
     */
    public Dimension2D getMeterBodySize() {
        return ( new PDimension( _meterBody.getFullBoundsReference().width,
                                 _meterBody.getFullBoundsReference().height ) );
    }

    //------------------------------------------------------------------------
    // Inner Classes
    //------------------------------------------------------------------------

    /**
     * This class represents a node that visually depicts the cable or cord that
     * connects the body of the meter to the probe.  This is highly leveraged
     * from a class that was found in the Wave Interference sim.
     */
    private static class ProbeCableNode extends PhetPNode {
        private final ProbeNode _probeNode;
        private final PNode _meterBodyNode;
        private PPath _cable;

        public ProbeCableNode( RadiometricDatingMeterNode meterNode ) {
            _probeNode = meterNode.getProbeNode();
            _meterBodyNode = meterNode.getMeterBodyNode();

            // Create the path that represents the cord.
            _cable = new PPath();
            _cable.setStroke( new BasicStroke( 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
            addChild( _cable );

            // Register for changes in location or size of the two nodes that
            // the cable connects together.
            _probeNode.addPropertyChangeListener( PNode.PROPERTY_FULL_BOUNDS, new PropertyChangeListener() {
                public void propertyChange( PropertyChangeEvent evt ) {
                    update();
                }
            } );
            _meterBodyNode.addPropertyChangeListener( PNode.PROPERTY_FULL_BOUNDS, new PropertyChangeListener() {
                public void propertyChange( PropertyChangeEvent evt ) {
                    update();
                }
            } );
            update();
        }

        private void update() {
            Point2D dstLoc = new Point2D.Double( _probeNode.getTailLocation().getX(),
                                                 _probeNode.getTailLocation().getY() );
            Point2D srcLoc = new Point2D.Double( _meterBodyNode.getFullBoundsReference().getCenterX(),
                                                 _meterBodyNode.getFullBoundsReference().getMaxY() );
            double dist = srcLoc.distance( dstLoc );
            if ( dist > 0 ) {
                DoubleGeneralPath path = new DoubleGeneralPath( srcLoc );
                Vector2D parallel = new MutableVector2D( srcLoc, dstLoc ).normalized();
                Vector2D perp = parallel.getRotatedInstance( Math.PI / 2 ).normalized();
                lineToDst( path, parallel, perp, dist / 5 );
                curveToDst( path, parallel, perp, dist / 5 );
                lineToDst( path, parallel, perp, dist / 5 );
                curveToDst( path, parallel, perp, dist / 5 );
                path.lineTo( dstLoc );
                _cable.setPathTo( path.getGeneralPath() );
            }
            else {
                _cable.setPathTo( new Rectangle() );
            }
        }

        private void curveToDst( DoubleGeneralPath path, Vector2D par, Vector2D perp, double segmentLength ) {
            double pegDist = segmentLength;
            if ( pegDist < 7 ) {
                pegDist = 7;
            }
            if ( pegDist > 20 ) {
                pegDist = 20;
            }
            double width = 15;
            Point2D peg1 = path.getCurrentPoint();
            Point2D peg2 = par.getInstanceOfMagnitude( pegDist ).getDestination( peg1 );
            Point2D dst = par.getInstanceOfMagnitude( pegDist / 2 ).plus( perp.getInstanceOfMagnitude( width ) ).getDestination( peg1 );
            path.quadTo( peg2.getX(), peg2.getY(), dst.getX(), dst.getY() );
            path.quadTo( peg1.getX(), peg1.getY(), peg2.getX(), peg2.getY() );
        }

        private void lineToDst( DoubleGeneralPath path, Vector2D a, Vector2D b, double segmentLength ) {
            path.lineToRelative( a.getInstanceOfMagnitude( segmentLength ) );
        }
    }

    /**
     * Panel that allows the user to select the element to user for performing
     * the dating.
     */
    private class ElementSelectionPanel extends VerticalLayoutPanel {

        private final Font LABEL_FONT = new PhetFont( 16, true );

        public ElementSelectionPanel( final RadiometricDatingMeter meterModel, boolean showCustom ) {

            setBackground( BODY_COLOR );

            // Add the border around the panel.
            BevelBorder baseBorder = (BevelBorder) BorderFactory.createRaisedBevelBorder();
            TitledBorder titledBorder = BorderFactory.createTitledBorder( baseBorder,
                                                                          NuclearPhysicsStrings.METER_PROBE_TYPE,
                                                                          TitledBorder.CENTER,
                                                                          TitledBorder.TOP,
                                                                          LABEL_FONT,
                                                                          Color.WHITE );

            setBorder( titledBorder );

            // Create the radio buttons, one for each possible radiometric
            // dating element.

            final JRadioButton carbon14Button = new JRadioButton(
                    NucleusDisplayInfo.getDisplayInfoForNucleusType( NucleusType.CARBON_14 ).getName(), true );
            carbon14Button.setBackground( BODY_COLOR );
            carbon14Button.setForeground( Color.WHITE );
            carbon14Button.setFont( LABEL_FONT );
            carbon14Button.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    meterModel.setNucleusTypeUsedForDating( NucleusType.CARBON_14 );
                }
            } );
            add( carbon14Button );
            meterModel.addListener( new RadiometricDatingMeter.Adapter() {
                public void datingElementChanged() {
                    carbon14Button.setSelected( meterModel.getNucleusTypeUsedForDating() ==
                                                NucleusType.CARBON_14 );
                }

                ;
            } );
            carbon14Button.setSelected( meterModel.getNucleusTypeUsedForDating() == NucleusType.CARBON_14 );

            final JRadioButton uranium238RadioButton = new JRadioButton(
                    NucleusDisplayInfo.getDisplayInfoForNucleusType( NucleusType.URANIUM_238 ).getName(), true );
            uranium238RadioButton.setBackground( BODY_COLOR );
            uranium238RadioButton.setForeground( Color.WHITE );
            uranium238RadioButton.setFont( LABEL_FONT );
            uranium238RadioButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    meterModel.setNucleusTypeUsedForDating( NucleusType.URANIUM_238 );
                }
            } );
            add( uranium238RadioButton );
            meterModel.addListener( new RadiometricDatingMeter.Adapter() {
                public void datingElementChanged() {
                    uranium238RadioButton.setSelected( meterModel.getNucleusTypeUsedForDating() ==
                                                       NucleusType.URANIUM_238 );
                }

                ;
            } );
            uranium238RadioButton.setSelected( meterModel.getNucleusTypeUsedForDating() == NucleusType.URANIUM_238 );

            if ( showCustom ) {
                final JRadioButton customNucleusRadioButton = new JRadioButton(
                        NucleusDisplayInfo.getDisplayInfoForNucleusType( NucleusType.HEAVY_CUSTOM ).getName(), true );
                customNucleusRadioButton.setBackground( BODY_COLOR );
                customNucleusRadioButton.setForeground( Color.WHITE );
                customNucleusRadioButton.setFont( LABEL_FONT );
                customNucleusRadioButton.addActionListener( new ActionListener() {
                    public void actionPerformed( ActionEvent e ) {
                        meterModel.setNucleusTypeUsedForDating( NucleusType.HEAVY_CUSTOM );
                    }
                } );
                add( customNucleusRadioButton );
                meterModel.addListener( new RadiometricDatingMeter.Adapter() {
                    public void datingElementChanged() {
                        customNucleusRadioButton.setSelected( meterModel.getNucleusTypeUsedForDating() ==
                                                              NucleusType.HEAVY_CUSTOM );
                    }

                    ;
                } );
                customNucleusRadioButton.setSelected( meterModel.getNucleusTypeUsedForDating() == NucleusType.HEAVY_CUSTOM );
            }
        }
    }

    /**
     * Panel that allows selection between measuring air or objects.
     */
    private static class MeasurementSelectionPanel extends HorizontalLayoutPanel {

        static private final Font LABEL_FONT = new PhetFont( 18, true );
        private final RadiometricDatingMeter _meterModel;
        private final JRadioButton _measureObjectsButton;
        private final JRadioButton _measureAirButton;

        public MeasurementSelectionPanel( final RadiometricDatingMeter meterModel,
                                          final RadiometricDatingMeterNode meterNode ) {

            _meterModel = meterModel;
            setBackground( BODY_COLOR );

            // Add the border around the panel.
            BevelBorder border = (BevelBorder) BorderFactory.createRaisedBevelBorder();

            setBorder( border );

            _measureObjectsButton = new JRadioButton( NuclearPhysicsStrings.MEASURE_OBJECTS, true );
            _measureObjectsButton.setBackground( BODY_COLOR );
            _measureObjectsButton.setForeground( Color.WHITE );
            _measureObjectsButton.setFont( LABEL_FONT );
            _measureObjectsButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    if ( _meterModel.getMeasurementMode() != MeasurementMode.OBJECTS ) {
                        meterNode.startProbeChangeAnimation();
                    }
                }
            } );
            add( _measureObjectsButton );

            _measureAirButton = new JRadioButton( NuclearPhysicsStrings.MEASURE_AIR, true );
            _measureAirButton.setBackground( BODY_COLOR );
            _measureAirButton.setForeground( Color.WHITE );
            _measureAirButton.setFont( LABEL_FONT );
            _measureAirButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    if ( _meterModel.getMeasurementMode() != MeasurementMode.AIR ) {
                        meterNode.startProbeChangeAnimation();
                    }
                    updateButtonState();
                }
            } );
            add( _measureAirButton );

            // Put these in a button group.
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add( _measureAirButton );
            buttonGroup.add( _measureObjectsButton );

            meterModel.addListener( new RadiometricDatingMeter.Adapter() {
                @Override
                public void measurementModeChanged() {
                    updateButtonState();
                }
            } );

            updateButtonState();
        }

        private void updateButtonState() {
            _measureAirButton.setSelected( _meterModel.getMeasurementMode() == MeasurementMode.AIR );
            _measureObjectsButton.setSelected( _meterModel.getMeasurementMode() == MeasurementMode.OBJECTS );
        }
    }

    private static class ValueStringPair {
        public double value;
        public String string;

        public ValueStringPair( double value, String string ) {
            this.value = value;
            this.string = string;
        }

        public String toString() {
            return this.string;
        }
    }

    /**
     * Timer used to animate the toggle between the measurement modes.
     */
    private class MeasurementModeToggleAnimationTimer extends Timer {

        private static final int TIMER_DELAY = 30;            // Milliseconds between each animation step.
        private static final int TOTAL_ANIMATION_COUNT = 12;  // Total number of animation steps.

        private RadiometricDatingMeterNode _meterNode;
        private RadiometricDatingMeter.MeasurementMode _targetMode;
        private int _animationCount = TOTAL_ANIMATION_COUNT;

        public MeasurementModeToggleAnimationTimer( RadiometricDatingMeterNode meterNode,
                                                    RadiometricDatingMeter.MeasurementMode targetMode ) {

            super( TIMER_DELAY, null );

            _meterNode = meterNode;
            _targetMode = targetMode;

            // Set the initial position and visibility of the probes based on
            // the change that is occurring.
            if ( _targetMode == MeasurementMode.AIR ) {
                // Start with probe retracted but visible.
                _meterNode.setAirProbeExtensionAmount( 0 );
                _meterNode._airProbeNode.setVisible( true );

                // Hide the object probe.
                _meterNode._objectProbeNode.setVisible( false );
                _meterNode._cableNode.setVisible( false );
            }
            else {
                // Probe should already be fully extended, but make sure.
                _meterNode.setAirProbeExtensionAmount( 1 );
            }

            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    if ( _animationCount <= 0 ) {
                        // Stop the timer.
                        stop();

                        // Set the model to the target mode.
                        _meterNode._meterModel.setMeasurementMode( _targetMode );

                        // Remove this timer from the meter node.
                        _meterNode._animationTimer = null;
                    }
                    else {
                        // Perform the next step in the animation.
                        stepAnimation( TOTAL_ANIMATION_COUNT - _animationCount );
                        _animationCount--;
                    }
                }
            } );

            // Start the timer running.
            start();
        }

        private void stepAnimation( int count ) {

            if ( _targetMode == MeasurementMode.AIR ) {
                _meterNode.setAirProbeExtensionAmount( ( (double) count + 1 ) / (double) TOTAL_ANIMATION_COUNT );
            }
            else {
                _meterNode.setAirProbeExtensionAmount( 1 - ( (double) count + 1 ) / (double) TOTAL_ANIMATION_COUNT );
            }
        }
    }
}
