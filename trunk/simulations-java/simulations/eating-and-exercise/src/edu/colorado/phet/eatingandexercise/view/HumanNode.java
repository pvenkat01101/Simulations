// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.eatingandexercise.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.colorado.phet.common.phetcommon.math.vector.MutableVector2D;
import edu.colorado.phet.common.phetcommon.view.VerticalLayoutPanel;
import edu.colorado.phet.common.phetcommon.view.controls.valuecontrol.LinearValueControl;
import edu.colorado.phet.common.phetcommon.view.util.PhetFont;
import edu.colorado.phet.common.piccolophet.BufferedPhetPCanvas;
import edu.colorado.phet.common.piccolophet.PhetPCanvas;
import edu.colorado.phet.common.piccolophet.nodes.HTMLImageButtonNode;
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.colorado.phet.eatingandexercise.EatingAndExerciseResources;
import edu.colorado.phet.eatingandexercise.model.Human;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * Created by: Sam
 * Apr 3, 2008 at 8:43:08 PM
 * Todo: factor out limb class
 */
public class HumanNode extends PNode {
    private Human human;
    private HeadNode head;
    private PImage heartNode;
    private BasicStroke stroke = new BasicStroke( 0.02f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER );
    private PhetPPath areaNode = new PhetPPath( Color.white, stroke, Color.black );
    private HTMLImageButtonNode infoButton;
    private ArrayList listeners = new ArrayList();

    public HumanNode( Human human ) {
        this.human = human;
        head = new HeadNode( human, Color.white, new BasicStroke( 0.02f ), Color.black );
        addChild( areaNode );

        heartNode = new HeartNode( human );
        heartNode.scale( 0.25 / heartNode.getFullBounds().getWidth() );
        addChild( heartNode );
        addChild( head );
        human.addListener( new Human.Adapter() {
            public void heightChanged() {
                update();
            }

            public void weightChanged() {
                update();
            }

            public void fatPercentChanged() {
                update();
            }

            public void musclePercentChanged() {
                update();
            }
        } );

        infoButton = new HTMLImageButtonNode( EatingAndExerciseResources.getString( "question.mark" ), new PhetFont( Font.BOLD, 12 ), Color.red );
        infoButton.setScale( 0.007 );
        infoButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                notifyInfoButtonPressed();
            }
        } );
//        addChild( infoButton );
        update();
    }

    public PImage getHeartNode() {
        return heartNode;
    }

    private void update() {
        double headWidth = human.getHeight() * 0.125;
        double headHeight = human.getHeight() * 0.125;

        double distBetweenShoulders = 0.5;
        double armLength = human.getHeight() * 0.35;

        double hipY = -human.getHeight() * 0.4;
        double neckY = -human.getHeight() + headHeight;
        double shoulderY = neckY + headHeight;

        double m = getScaledMass() / human.getHeight() * 1.75;  //scale by height to make two people of same BMI have same proportions

        Line2D.Double leftLeg = ( new Line2D.Double( 0, hipY, -distBetweenShoulders / 2, 0 ) );
        Line2D.Double rightLeg = ( new Line2D.Double( 0, hipY, +distBetweenShoulders / 2, 0 ) );
        Line2D.Double body = ( new Line2D.Double( 0, hipY, 0, neckY ) );
        Line2D.Double leftArm = ( new Line2D.Double( 0, shoulderY, -armLength, shoulderY ) );
        Line2D.Double rightArm = ( new Line2D.Double( 0, shoulderY, armLength, shoulderY ) );
        Ellipse2D.Double head = ( new Ellipse2D.Double( -headWidth / 2, neckY - headHeight, headWidth, headHeight ) );
        this.head.setPathTo( head );

        BasicStroke limbStroke = new BasicStroke( (float) ( 0.08f * m ), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
        BasicStroke bodyStroke = new BasicStroke( (float) ( 0.08f * m * 1.2 ), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
        Area bodyArea = new Area();
        bodyArea.add( new Area( limbStroke.createStrokedShape( leftLeg ) ) );
        bodyArea.add( new Area( limbStroke.createStrokedShape( rightLeg ) ) );
        Shape bodyShape = bodyStroke.createStrokedShape( body );
        bodyArea.add( new Area( bodyShape ) );
        bodyArea.add( new Area( limbStroke.createStrokedShape( rightArm ) ) );
        bodyArea.add( new Area( limbStroke.createStrokedShape( leftArm ) ) );
        bodyArea.add( new Area( createStomachShape( bodyShape ) ) );

        bodyArea.add( new Area( createMuscle( rightArm, limbStroke ) ) );
        bodyArea.add( new Area( createMuscle( leftArm, limbStroke ) ) );

//        bodyArea.add( new Area( createMuscle( leftLeg, limbStroke ) ) );
//        bodyArea.add( new Area( createMuscle( rightLeg, limbStroke ) ) );

        areaNode.setPathTo( bodyArea );
        areaNode.setStroke( new BasicStroke( (float) ( Math.min( 0.02f * m, 0.025f ) ), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );

        heartNode.setOffset( -heartNode.getFullBounds().getWidth() * 0.15, neckY + heartNode.getFullBounds().getHeight() * 1.25 );
        infoButton.setOffset( heartNode.getFullBounds().getMaxX(), heartNode.getFullBounds().getMinY() );
    }

    private Shape createMuscle( Line2D.Double arm, BasicStroke limbStroke ) {
        //double leanMusclePercent = human.getLeanBodyMass();
        //double leanMuscleFraction = leanMusclePercent / 65.0;
        double h = human.getHeight();
        double m = human.getMass();
        double percentFat = human.getFatMassPercent();
        double leanMuscleFraction = ( m / ( h * h ) / 17.5 - 1 ) * ( 40 / percentFat - 1 );
        double muscleWidthBeyondArm = Math.max( leanMuscleFraction * 0.5, 0 );
        if ( muscleWidthBeyondArm > .8 ) {
            muscleWidthBeyondArm = .8 + ( muscleWidthBeyondArm - .8 ) * 0.25;
        }
        ;
        if ( muscleWidthBeyondArm > 1.25 ) {
            muscleWidthBeyondArm = 1.25;
        }
        ;

        double muscleDiameter = limbStroke.getLineWidth() * ( 1 + muscleWidthBeyondArm );
//        System.out.println( "LMF=" + leanMuscleFraction + ", modifier = " + muscleWidthBeyondArm + ", width=" + muscleDiameter );
        MutableVector2D vector = new MutableVector2D( arm.getP1(), arm.getP2() );
        double distAlongArmToCenter = 0.35;//assumes arm is one segment
        Ellipse2D.Double aDouble = new Ellipse2D.Double();
        Point2D center = vector.times( distAlongArmToCenter ).getDestination( arm.getP1() );
        aDouble.setFrameFromCenter( center, new Point2D.Double( center.getX() + muscleDiameter / 2, center.getY() + muscleDiameter / 2 ) );
        return aDouble;
    }

    //provides a mapping from human mass in KG to the arbitrary-scaled value for showing weight
    //set this scale here as desired
    //todo: could use nonlinear function if necessary
    private double getScaledMass() {

        double leanMuscleFraction = human.getFatFreeMassPercent() / 100.0;

        return human.getMass() / 75 * 1.75 + ( 1 - leanMuscleFraction ) * 0.2;
    }

    private Shape createStomachShape( Shape bodyShape ) {
        Rectangle2D bounds = bodyShape.getBounds2D();

        //double w = Math.max( 0.05 * getScaledMass() - 0.05, 0 );
        double percentFat = human.getFatMassPercent();

        //should be percentFat/12 for male, percentFat/18 for female
        double scaleFactor = human.getGender().equals( Human.Gender.MALE ) ? 13 : 20;
        double w = Math.max( ( percentFat / scaleFactor - 1 ) * 0.1, 0 );

        return new Ellipse2D.Double( bounds.getX() - w / 2, bounds.getCenterY(), bounds.getWidth() + w, bounds.getHeight() / 2 );
    }

    public static void main( String[] args ) {
        JFrame frame = new JFrame( "Test Frame" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 800, 600 );
        PhetPCanvas phetPCanvas = new BufferedPhetPCanvas( new PDimension( 3, 3 ) );
        phetPCanvas.setZoomEventHandler( new PZoomEventHandler() );
        //todo: update layout
        frame.setContentPane( phetPCanvas );

        final Human human = new Human();
        HumanNode humanNode = new HumanNode( human );
        humanNode.setOffset( 1, 2 );
        phetPCanvas.addWorldChild( humanNode );
        frame.setVisible( true );
        JFrame controlFrame = new JFrame();
        JPanel contentPane = new VerticalLayoutPanel();
        controlFrame.setContentPane( contentPane );

        final LinearValueControl control = new LinearValueControl( 0, 500, 75, "mass", "0.00", "kg" );
        control.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                human.setMass( control.getValue() );
            }
        } );
        contentPane.add( control );

        final LinearValueControl control2 = new LinearValueControl( 0, 100, "fat %", "0.0", "%" );
        control2.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                human.setFatMassPercent( control2.getValue() );
            }
        } );
        human.addListener( new Human.Adapter() {
            public void fatPercentChanged() {
                control2.setValue( human.getFatMassPercent() );
            }
        } );
        contentPane.add( control2 );

        final LinearValueControl strength = new LinearValueControl( 0, 1, "heart strength", "0.00", "" );
        strength.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                human.setHeartStrength( strength.getValue() );
            }
        } );
        contentPane.add( strength );

        final LinearValueControl strain = new LinearValueControl( 0, 1, "heart strain", "0.00", "" );
        strain.addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e ) {
                human.setHeartStrain( strain.getValue() );
            }
        } );
        contentPane.add( strain );

        controlFrame.setVisible( true );
        controlFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        controlFrame.pack();
        controlFrame.setLocation( frame.getX() + frame.getWidth(), frame.getY() );
    }

    public static interface Listener {
        void infoButtonPressed();
    }

    public void addListener( Listener listener ) {
        listeners.add( listener );
    }

    public void notifyInfoButtonPressed() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).infoButtonPressed();
        }
    }
}