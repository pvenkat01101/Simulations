package edu.colorado.phet.sugarandsaltsolutions.micro.view;

import edu.colorado.phet.common.phetcommon.model.property.ObservableProperty;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction0;
import edu.colorado.phet.common.phetcommon.util.function.VoidFunction1;
import edu.colorado.phet.common.phetcommon.view.graphics.transforms.ModelViewTransform;
import edu.colorado.phet.sugarandsaltsolutions.common.model.ItemList;
import edu.colorado.phet.sugarandsaltsolutions.common.model.SphericalParticle;
import edu.colorado.phet.sugarandsaltsolutions.common.view.ICanvas;
import edu.colorado.phet.sugarandsaltsolutions.common.view.SphericalParticleNode;

/**
 * This class observes an ItemList and creates a PNode when an item is added to the model list,
 * and removes it when the item is removed from the model list.
 *
 * @author Sam Reid
 */
public class SphericalParticleNodeFactory implements VoidFunction1<SphericalParticle> {
    private final ItemList<SphericalParticle> list;
    private final ModelViewTransform transform;
    private final ICanvas canvas;
    private final ObservableProperty<Boolean> showChargeColor;

    public SphericalParticleNodeFactory( ItemList<SphericalParticle> list, ModelViewTransform transform, ICanvas canvas, ObservableProperty<Boolean> showChargeColor ) {
        this.list = list;
        this.transform = transform;
        this.canvas = canvas;
        this.showChargeColor = showChargeColor;
    }

    //Create the PNode for the particle, and wire it up to be removed when the particle leaves the model
    public void apply( final SphericalParticle particle ) {
        final SphericalParticleNode node = new SphericalParticleNode( transform, particle, showChargeColor );
        canvas.addChild( node );
        list.addElementRemovedObserver( particle, new VoidFunction0() {
            public void apply() {
                list.removeElementRemovedObserver( particle, this );
                canvas.removeChild( node );
            }
        } );
    }
}