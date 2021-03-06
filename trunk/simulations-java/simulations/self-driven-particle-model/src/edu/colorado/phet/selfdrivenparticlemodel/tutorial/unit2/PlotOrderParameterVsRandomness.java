// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.selfdrivenparticlemodel.tutorial.unit2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.*;

import edu.colorado.phet.common.phetcommon.view.HorizontalLayoutPanel;
import edu.colorado.phet.selfdrivenparticlemodel.model.ParticleModel;
import edu.colorado.phet.selfdrivenparticlemodel.tutorial.BasicTutorialCanvas;
import edu.colorado.phet.selfdrivenparticlemodel.tutorial.TutorialChartFrame;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.pswing.PSwing;

public class PlotOrderParameterVsRandomness extends OrderParameter90 {
    private OrderParameterVsRandomnessChart orderParameterVsRandomnessChart;
    private TutorialChartFrame tutorialChartFrame;
    private ParticleModel.Adapter listener;
    public static final long MOD = 4;
    private PNode orderPlotPanel;

    public PlotOrderParameterVsRandomness( BasicTutorialCanvas page ) {
        super( page );
        setText( "Here we measure order parameter as a function of the randomness value.  " +
                 "Open the plot to " +
                 "start taking readings.  " +
                 "When there is sufficient data at one randomness value, " +
                 "the mean value is marked with a red square.  " +
                 "Take readings at several randomness values, and I'll connect the means." +
                 "" );
        orderPlotPanel = new PSwing( new OrderPlotPanel() );
        orderParameterVsRandomnessChart = new OrderParameterVsRandomnessChart( (int) ( 100000 / MOD ) );
        tutorialChartFrame = new TutorialChartFrame( "Plot",
                                                     orderParameterVsRandomnessChart.getChart(),
                                                     getBasePage().getTutorialApplication().getTutorialFrame() );
        listener = new ParticleModel.Adapter() {
            public void steppedInTime() {
                if ( getParticleModel().getTime() % MOD == 0 ) {
                    super.steppedInTime();
                    sampleData();
                }
            }
        };
    }

    class OrderPlotPanel extends HorizontalLayoutPanel {
        public OrderPlotPanel() {
            setBorder( BorderFactory.createTitledBorder( "Order Plot" ) );
            JButton plotButton = new JButton( "Show" );
            plotButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    showPlot();
                    startDataTaking();
                    if ( advanceWhenStartData() ) {
                        advance();
                    }
                }
            } );
            JButton resetButton = new JButton( "Reset" );
            resetButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    orderParameterVsRandomnessChart.reset();
                }
            } );
            add( plotButton );
            add( resetButton );
        }
    }

    Point2D.Double sampleDataValue() {
        double randomness = getParticleModel().getRandomness();
        double orderParameter = getParticleModel().getOrderParameter();
        return new Point2D.Double( randomness, orderParameter );
    }

    private void sampleData() {
        Point2D sample = sampleDataValue();
        if ( isLegal( sample ) ) {
            orderParameterVsRandomnessChart.addDataPoint( sample.getX(), sample.getY() );
        }
    }

    private boolean isLegal( Point2D sample ) {

        return isLegal( sample.getX() ) && isLegal( sample.getY() );
    }

    private boolean isLegal( double value ) {
        return !( Double.isNaN( value ) || Double.isInfinite( value ) );
    }

    private void startDataTaking() {
        getParticleModel().resetTime();
        getParticleModel().addListener( listener );
    }

    private void stopTakingData() {
        getParticleModel().removeListener( listener );
    }

    protected boolean isOrderParamaterAwesome() {
        return false;
    }

    public void init() {
        super.init();
        orderPlotPanel.setOffset( getLocationBeneath( getOrderParamText() ) );
        addChild( orderPlotPanel );
    }

    public void teardown() {
        super.teardown();
        removeChild( orderPlotPanel );
        tutorialChartFrame.setVisible( false );
        stopTakingData();
    }

    private void showPlot() {
        tutorialChartFrame.setVisible( true );
    }

    protected boolean advanceWhenStartData() {
        return true;
    }

    public PNode getBottomComponent() {
        return orderPlotPanel;
    }
}
