// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.fractions.buildafraction.model.numbers;

import fj.data.List;

import java.awt.Color;

import static edu.colorado.phet.fractions.common.view.Colors.*;
import static fj.data.List.list;

/**
 * Provides support for choosing 4 random colors that are different for making levels.
 *
 * @author Sam Reid
 */
class RandomColors4 implements IRandomColors {
    private final List<Color> colors = NumberLevelList.shuffle( list( LIGHT_RED, LIGHT_GREEN, LIGHT_BLUE, Color.orange ) );
    private int index = 0;

    //Get the next color in an enumeration style
    public Color next() {
        final Color color = colors.index( index );
        index++;
        return color;
    }
}