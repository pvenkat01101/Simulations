import flash.display.BitmapData
import flash.geom.Rectangle

class VoltageMosaic extends MovieClip implements Observer {

    private var bitmapData:BitmapData;
    private var model:ChargeGroup;

    public var movingStep:Number = 10;

    public var sWidth:Number = 640;
    public var sHeight:Number = 480;

    public function VoltageMosaic() {
        trace( "created" );
    }

    public function init( model:ChargeGroup ) {
        this.model = model;
        bitmapData = new BitmapData( sWidth, sHeight, false, 0xffffff );
        attachBitmap( bitmapData, getNextHighestDepth(), "always" );
        //model.addObserver( this );
        _visible = false;
    }

    public function start() {
        model.addObserver( this );
        _visible = true;
    }

    public function stop() {
        model.removeObserver( this );
        _visible = false;
    }

    public function update( model:ChargeGroup ):Void {
        draw( movingStep );
    }

    public function drawTile( x:Number, y:Number, width:Number, height:Number ):Void {
        var rect:Rectangle = new Rectangle( x, y, width, height );
        bitmapData.fillRect( rect, model.getColor( x + width / 2, y + height / 2 ) )
    }

    public function clear():Void {
        bitmapData.fillRect( new Rectangle( 0, 0, sWidth, sHeight ), 0xFFFFFF );
    }

    public function draw( step:Number ):Void {
        if ( _visible ) {
            var rect:Rectangle = new Rectangle( 0, 0, step, step );
            var halfStep:Number = step / 2;
            for ( var i:Number = 0; i < sWidth; i += step ) {
                var iPos = i + halfStep;
                rect.x = i;
                for ( var j:Number = 0; j < sHeight; j += step ) {
                    rect.y = j;
                    bitmapData.fillRect( rect, model.getColor( iPos, j + halfStep ) );
                }
            }
        }
    }
}