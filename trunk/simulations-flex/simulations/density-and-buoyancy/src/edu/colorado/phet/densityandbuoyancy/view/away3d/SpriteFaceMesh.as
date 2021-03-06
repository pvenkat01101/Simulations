//  Copyright 2002-2011, University of Colorado
package edu.colorado.phet.densityandbuoyancy.view.away3d {
import away3d.materials.MovieMaterial;

import flash.display.Sprite;

/**
 * This is a single rectangle in 3d that is filled visually with the contents of a sprite.
 *
 * This is used as a child in Sprite3D, they are created each time the sprite is resized, as it cannot handle changing its size when its sprite
 * increases.
 */
public class SpriteFaceMesh extends SimpleMesh {
    private var sprite: Sprite;
    private var bottomLeft: Number;
    private var bottomRight: Number;
    private var topLeft: Number;
    private var topRight: Number;

    public function SpriteFaceMesh( sprite: Sprite ) {
        super();
        this.sprite = sprite;
        bottomLeft = v( 0, 0, 0 );
        bottomRight = v( sprite.width, 0, 0 );
        topLeft = v( 0, sprite.height, 0 );
        topRight = v( sprite.width, sprite.height, 0 );
        uv( 0, 0 );
        uv( 1, 0 );
        uv( 0, 1 );
        uv( 1, 1 );
        const movieMaterial: MovieMaterial = new MovieMaterial( sprite );
        movieMaterial.smooth = true;
        plane( bottomLeft, bottomRight, topRight, topLeft, movieMaterial );
        mouseEnabled = false;
    }

    public function get width(): Number {
        return sprite.width;
    }

    public function get height(): Number {
        return sprite.height;
    }
}
}