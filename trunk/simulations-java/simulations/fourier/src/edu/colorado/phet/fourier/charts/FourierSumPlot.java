// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */

package edu.colorado.phet.fourier.charts;

import java.awt.*;
import java.awt.geom.Point2D;

import edu.colorado.phet.common.charts.Chart;
import edu.colorado.phet.common.charts.DataSet;
import edu.colorado.phet.common.charts.LinePlot;
import edu.colorado.phet.common.charts.Range2D;
import edu.colorado.phet.fourier.FourierConstants;
import edu.colorado.phet.fourier.enums.WaveType;
import edu.colorado.phet.fourier.model.FourierSeries;
import edu.colorado.phet.fourier.model.Harmonic;


/**
 * FourierSumPlot encapsulates the graphics and data set that allow a Chart
 * to draw the sum of a discrete Fourier series.
 *
 * @author Chris Malley (cmalley@pixelzoom.com)
 * @version $Revision: 47772 $
 */
public class FourierSumPlot extends LinePlot {

    //----------------------------------------------------------------------------
    // Class data
    //----------------------------------------------------------------------------

    private static final Stroke DEFAULT_STROKE = new BasicStroke( 1f );
    private static final Color DEFAULT_COLOR = Color.BLACK;
    
    //----------------------------------------------------------------------------
    // Instance data
    //----------------------------------------------------------------------------
    
    private FourierSeries _fourierSeries;  
    private double _period;
    private double _startX;
    private double _pixelsPerPoint;
    private double _maxAmplitude;
    private Point2D[] _points;
    
    //----------------------------------------------------------------------------
    // Constructors
    //----------------------------------------------------------------------------
    
    /**
     * Sole constructor
     * 
     * @param component
     * @param chart
     * @param fourierSeries
     */
    public FourierSumPlot( Component component, Chart chart, FourierSeries fourierSeries ) {
        super( component, chart );

        // Enable antialiasing
        setRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ) );

        _fourierSeries = fourierSeries;
 
        _period = 1;
        _startX = 0;
        _pixelsPerPoint = 1.0;
        _maxAmplitude = FourierConstants.MAX_HARMONIC_AMPLITUDE;
        _points = null;
        
        setDataSet( new DataSet() );
        setBorderColor( DEFAULT_COLOR );
        setStroke( DEFAULT_STROKE );
        
        updateDataSet();
    }
    
    //----------------------------------------------------------------------------
    // Accessors
    //----------------------------------------------------------------------------
    
    /**
     * Sets the length of one period of the wave, in model coordinates.
     * 
     * @param period
     */
    public void setPeriod( double period ) {
        assert( period >= 0 );
        if ( period < 0 ) {
            throw new IllegalArgumentException( "period must be >= 0" );
        }
        if ( period != _period ) {
            _period = period;
            updateDataSet();
        }
    }

    /**
     * Gets the length of one period of the wave, in model coordinates.
     * 
     * @return the period
     */
    public double getPeriod() {
        return _period;
    }
    
    /**
     * Sets the starting point of some cycle of the wave.
     *
     * @param startX
     */
    public void setStartX( double startX ) {
        if ( startX != _startX ) {
            _startX = startX;
            updateDataSet();
        }
    }
    
    /**
     * Gets the starting point of some cycle of the wave.
     * 
     * @return the starting point
     */
    public double getStartX() {
        return _startX;
    }
    
    /**
     * Sets the number of pixels per data point.
     * 
     * @param pixelsPerPoint
     */
    public void setPixelsPerPoint( double pixelsPerPoint ) {
        assert( pixelsPerPoint > 0 );
        if ( pixelsPerPoint != _pixelsPerPoint ) {
            _pixelsPerPoint = pixelsPerPoint;
            updateDataSet();
        }
    }
    
    /**
     * Gets the number of pixels per data point.
     * 
     * @return the number of pixels per data point
     */
    public double getPixelsPerPoint() {
        return _pixelsPerPoint;
    }
    
    public double getMaxAmplitude() {
        return _maxAmplitude;
    }
    
    //----------------------------------------------------------------------------
    // Data set update
    //----------------------------------------------------------------------------
    
    public void updateDataSet() {
        
        Chart chart = getChart();
        Range2D range = chart.getRange();
        DataSet dataSet = getDataSet();
        int numberOfHarmonics = _fourierSeries.getNumberOfHarmonics();
        WaveType waveType = _fourierSeries.getWaveType();
        _maxAmplitude = 0;
        
        // Clear the data set.
        dataSet.clear();
        
        // Number of pixels between the min and max X range.
        double minPixel = modelToViewX( range.getMinX() );
        double maxPixel = modelToViewX( range.getMaxX() );
        double numberOfPixels = maxPixel - minPixel + 1;
        
        // Number of points between the min and max X range.
        int numberOfPoints = (int) ( numberOfPixels / _pixelsPerPoint );

        // Change in X per pixel.
        double extent = Math.abs( range.getMaxX() - range.getMinX() );
        double deltaX = extent / numberOfPoints;

        // Pixels between the beginning of the cycle and the range min.
        double startPixel = modelToViewX( _startX );
        
        // Reuse the points if the count hasn't changed.
        if ( _points == null || numberOfPoints + 1 != _points.length ) {
            _points = new Point2D.Double[ numberOfPoints + 1 ];
        }
        
        // For each harmonic in the series...          
        for ( int harmonicIndex = 0; harmonicIndex < numberOfHarmonics; harmonicIndex++ ) {
            
            Harmonic harmonic = (Harmonic) _fourierSeries.getHarmonic( harmonicIndex );
            final double amplitude = harmonic.getAmplitude();

            // Change in angle per pixel.
            final double numberOfCycles = ( extent / _period ) * ( harmonic.getOrder() + 1 );
            final double deltaAngle = numberOfCycles * ( 2 * Math.PI ) / numberOfPoints;

            // Starting angle at the range min.
            double startAngle = Math.abs( ( startPixel - minPixel ) / _pixelsPerPoint ) * deltaAngle;
            if ( startPixel > minPixel ) {
                startAngle = -startAngle;
            }

            // Add the harmonic's contribution to the sum.
            for ( int pointIndex = 0; pointIndex < _points.length; pointIndex++ ) {

                // Clear the points the first time through.
                if ( harmonicIndex == 0 ) {
                    final double x = range.getMinX() + ( pointIndex * deltaX );
                    final double y = 0;
                    if ( _points[ pointIndex ] == null ) {
                        _points[ pointIndex ] = new Point2D.Double( x, y );
                    }
                    else {
                        _points[ pointIndex ].setLocation( x, y );
                    }
                }

                // Add the Y contribution for harmonics with non-zero amplitude.
                if ( amplitude != 0 ) {
                    final double angle = startAngle + ( pointIndex * deltaAngle );
                    double radians;
                    if ( waveType == WaveType.SINES ) {
                        radians = FourierConstants.TRIG_CACHE.sin( angle );
                    }
                    else {
                        radians = FourierConstants.TRIG_CACHE.cos( angle );
                    }

                    final double x = _points[ pointIndex ].getX();
                    final double y = _points[ pointIndex ].getY() + ( amplitude * radians );
                    _points[ pointIndex ].setLocation( x, y );
                }

                // Determine the max Y value the last time through.
                if ( harmonicIndex == numberOfHarmonics - 1 ) {
                    final double absoluteY = Math.abs( _points[ pointIndex ].getY() );
                    if ( absoluteY > _maxAmplitude ) {
                        _maxAmplitude = absoluteY;
                    }
                }
            }
        }

        // Update the data set.
        dataSet.addAllPoints( _points );
    }
    
    /*
     * Encapsulates the algorithm used to convert from
     * model coordinates to view coordinates.
     * 
     * @param x the x model coordinate to be converted
     * @return the corresponding view x coordinate
     */
    private double modelToViewX( double x ) {
        return getChart().transformXDouble( x );
    }
    
    //----------------------------------------------------------------------------
    // LinePlot overrides
    //----------------------------------------------------------------------------
    
    /**
     * Updates whenever the chart's size or range changes.
     */
    public void transformChanged() {
        updateDataSet(); // update the data set
        super.transformChanged(); // let LinePlot handle the rendering
    }
}
