//  Copyright 2002-2011, University of Colorado
package edu.colorado.phet.densityandbuoyancy.test {
import Box2D.Dynamics.b2DebugDraw;
import Box2D.Dynamics.b2World;

import edu.colorado.phet.densityandbuoyancy.DensityAndBuoyancyConstants;

import flash.display.Sprite;

import mx.core.UIComponent;

/**
 * Debugging resource that shows a 2D version of our Box2D physics engine
 */
public class Box2DDebug {
    private var holder: UIComponent;

    public function Box2DDebug( world: b2World ) {
        // debug draw start
        var m_sprite: Sprite;
        m_sprite = new Sprite();
        m_sprite.x = 400;
        m_sprite.y = 400;
        holder = new UIComponent();

        //Prevent the box2d debug graphic from intercepting mouse events
        holder.mouseEnabled = false;
        holder.mouseChildren = false;

        holder.addChild( m_sprite );

        var dbgDraw: b2DebugDraw = new b2DebugDraw();
        var dbgSprite: Sprite = new Sprite();
        m_sprite.addChild( dbgSprite );
        dbgDraw.m_sprite = m_sprite;
        dbgDraw.m_drawScale = 150 / DensityAndBuoyancyConstants.SCALE_BOX2D;
        dbgDraw.m_alpha = 1;
        dbgDraw.m_fillAlpha = 0.5;
        dbgDraw.m_lineThickness = 1;
        dbgDraw.m_drawFlags = b2DebugDraw.e_shapeBit | b2DebugDraw.e_jointBit | b2DebugDraw.e_coreShapeBit | b2DebugDraw.e_aabbBit | b2DebugDraw.e_obbBit | b2DebugDraw.e_pairBit | b2DebugDraw.e_centerOfMassBit;
        world.SetDebugDraw( dbgDraw );
    }
}
}