//  Copyright 2002-2011, University of Colorado
package edu.colorado.phet.densityandbuoyancy.view {
import edu.colorado.phet.densityandbuoyancy.DensityAndBuoyancyConstants;
import edu.colorado.phet.densityandbuoyancy.model.DensityAndBuoyancyModel;
import edu.colorado.phet.densityandbuoyancy.test.Box2DDebug;
import edu.colorado.phet.densityandbuoyancy.view.modes.DensityCustomObjectMode;
import edu.colorado.phet.densityandbuoyancy.view.modes.DensityMysteryObjectsMode;
import edu.colorado.phet.densityandbuoyancy.view.modes.DensitySameDensityMode;
import edu.colorado.phet.densityandbuoyancy.view.modes.DensitySameMassMode;
import edu.colorado.phet.densityandbuoyancy.view.modes.DensitySameVolumeMode;
import edu.colorado.phet.densityandbuoyancy.view.modes.Mode;
import edu.colorado.phet.flashcommon.ApplicationLifecycle;

/**
 * Contains the sim play area for Density (not including control panels)
 */
public class DensityPlayAreaComponent extends AbstractDensityAndBuoyancyPlayAreaComponent {

    private var _container: DensityCanvas;

    private var customObjectMode: DensityCustomObjectMode;
    private var sameMassMode: DensitySameMassMode;
    private var sameVolumeMode: DensitySameVolumeMode;
    private var sameDensityMode: DensitySameDensityMode;
    private var mysteryObjectsMode: DensityMysteryObjectsMode;
    private var mode: Mode;

    public function DensityPlayAreaComponent( densityContainer: DensityCanvas ) {
        super( false );
        this._container = densityContainer;
        const thisReference: DensityPlayAreaComponent = this;
        // modes rely on the Stage being accessible for initialization, so we wait until the application has completed loading
        ApplicationLifecycle.addApplicationCompleteListener( function(): void {
            customObjectMode = new DensityCustomObjectMode( thisReference );
            sameMassMode = new DensitySameMassMode( thisReference );
            sameVolumeMode = new DensitySameVolumeMode( thisReference );
            sameDensityMode = new DensitySameDensityMode( thisReference );
            mysteryObjectsMode = new DensityMysteryObjectsMode( thisReference );
            //If other modes are added, you may need to specify a call to the Mode.reset() in resetAll()
            setMode( customObjectMode );

            //Box2D provides a sprite-oriented debugger for visualizing the box2d physics engine.  This can be turned on by uncommenting the addChild() line and will display the box2d shapes
            var box2DDebug: Box2DDebug = new Box2DDebug( model.getWorld() );
            //        _densityCanvas.addChild(box2DDebug.getSprite());
        } );
    }

    override public function resetAll(): void {
        super.resetAll();
        customObjectMode.reset();
        switchToCustomObject();
    }

    private function setMode( mode: Mode ): void {
        if ( this.mode != mode ) {
            if ( this.mode != null ) {
                this.mode.teardown();
            }
            this.mode = mode;
            this.mode.init();
        }
    }

    public function switchToSameMass(): void {
        setMode( sameMassMode );
    }

    public function switchToSameVolume(): void {
        setMode( sameVolumeMode );
    }

    public function switchToSameDensity(): void {
        setMode( sameDensityMode );
    }

    public function switchToCustomObject(): void {
        setMode( customObjectMode );
    }

    public function switchToMysteryObjects(): void {
        setMode( mysteryObjectsMode );
    }

    override public function get container(): AbstractDensityAndBuoyancyCanvas {
        return _container;
    }

    protected override function createModel( showExactLiquidColor: Boolean ): DensityAndBuoyancyModel {
        return new DensityAndBuoyancyModel( DensityAndBuoyancyConstants.litersToMetersCubed( 100.0 ), extendedPool );
    }
}
}

