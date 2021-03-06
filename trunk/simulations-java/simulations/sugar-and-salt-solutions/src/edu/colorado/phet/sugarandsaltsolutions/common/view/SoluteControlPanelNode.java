// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.common.view;

import java.awt.Color;
import java.awt.Rectangle;

import edu.colorado.phet.common.piccolophet.nodes.PhetPPath;
import edu.colorado.phet.common.piccolophet.nodes.layout.VBox;
import edu.colorado.phet.sugarandsaltsolutions.SugarAndSaltSolutionsResources;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * Base class for controls that allow the user to select from different solutes.
 * This general part of the code provides layout and a title, and relies on constructor parameter for the tab-specific control
 *
 * @author John Blanco
 * @author Sam Reid
 */
public class SoluteControlPanelNode extends WhiteControlPanelNode {
    public SoluteControlPanelNode( PNode soluteSelector ) {
        super( new VBox(

                //Use a smaller spacing so that the content doesn't get too far away from the title in the Micro tab
                5,
                createTitle(),
                new PhetPPath( new Rectangle( 0, 0, 0, 0 ), new Color( 0, 0, 0, 0 ) ),//spacer
                soluteSelector
        ) );
    }

    public static PText createTitle() {
        return new PText( SugarAndSaltSolutionsResources.Strings.SOLUTE ) {{setFont( BeakerAndShakerCanvas.TITLE_FONT );}};
    }
}
