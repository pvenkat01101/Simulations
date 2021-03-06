// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.common.motion.charts;

import java.util.ArrayList;
import java.util.Arrays;

import edu.colorado.phet.common.motion.model.TimeData;

/**
 * @author Sam Reid
 */
public class TemporalDataSeries {
    private ArrayList<TimeData> data = new ArrayList<TimeData>();
    private ArrayList<Listener> listeners = new ArrayList<Listener>();
    private boolean visible = true;

    public TimeData[] getData() {
        return data.toArray( new TimeData[data.size()] );
    }

    public void addPoint( double value, double time ) {
        addPoint( new TimeData( value, time ) );
    }

    public void addPoint( TimeData point ) {
        data.add( point );
        for ( Listener listener : listeners ) {
            listener.dataPointAdded( point );
        }
    }

    private void notifyEntrireSeriesChanged() {
        for ( Listener listener : listeners ) {
            listener.entireSeriesChanged();
        }
    }

    public int getNumPoints() {
        return data.size();
    }

    public TimeData getDataPoint( int i ) {
        return data.get( i );
    }

    public void setData( TimeData[] point2Ds ) {
        data.clear();
        data.addAll( Arrays.asList( point2Ds ) );
        notifyEntrireSeriesChanged();
    }

    public TimeData[] getPointsInRange( int min, int max ) {
        ArrayList<TimeData> points = new ArrayList<TimeData>();
        int size = getNumPoints();//moved here for performance improvements, was taking 15% of application in inner loop
        for ( int i = min; i <= max; i++ ) {
            if ( i >= 0 && i < size ) {
                points.add( data.get( i ) );
            }
        }
        return points.toArray( new TimeData[points.size()] );
    }

    public void clear() {
        data.clear();
        notifyEntrireSeriesChanged();
    }

    public TimeData getLastPoint() {
        return data.get( getNumPoints() - 1 );
    }

    public TimeData getMidPoint() {
        return data.get( getNumPoints() / 2 );
    }

    public void clearPointsAfter( double time ) {
        ArrayList<TimeData> newPoints = new ArrayList<TimeData>();
        for ( TimeData timeData : data ) {
            if ( timeData.getTime() < time ) {
                newPoints.add( timeData );
            }
        }
        data.clear();
        data.addAll( newPoints );
        notifyEntrireSeriesChanged();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible( Boolean visible ) {
        this.visible = visible;
        for ( Listener listener : listeners ) {
            listener.visibilityChanged();
        }
    }

    public static interface Listener {
        void entireSeriesChanged();

        void dataPointAdded( TimeData point );

        void visibilityChanged();
    }

    public static class Adapter implements Listener {
        public void entireSeriesChanged() {
        }

        public void dataPointAdded( TimeData point ) {
        }

        public void visibilityChanged() {
        }
    }

    public void addListener( Listener listener ) {
        listeners.add( listener );
    }

    /**
     * The TemporalDataSeries.Limited ignores any data points that fall outside its maximum time range, while still keeping enough points
     * to estimate derivatives properly.  The point is to avoid memory and processor leaks while still being able to estimate derivatives.
     */
    public static class LimitedTime extends TemporalDataSeries {
        private double maxTime;

        public LimitedTime( double maxTime ) {
            this.maxTime = maxTime;
        }

        public void addPoint( double value, double time ) {
            super.addPoint( value, time );
            if ( time <= maxTime ) {
                super.addPoint( value, time );
            }
        }
    }

    /**
     * The TemporalDataSeries.Limited ignores any data points that fall outside its maximum time range, while still keeping enough points
     * to estimate derivatives properly.  The point is to avoid memory and processor leaks while still being able to estimate derivatives.
     */
    public static class LimitedSize extends TemporalDataSeries {
        private int maxSize;

        public LimitedSize( int maxSize ) {
            this.maxSize = maxSize;
        }

        public void addPoint( double value, double time ) {
            super.addPoint( value, time );
            while ( getNumPoints() > maxSize ) {
                super.removePoint( 0 );
            }
        }
    }

    private void removePoint( int i ) {
        data.remove( i );
        notifyEntrireSeriesChanged();
    }
}
