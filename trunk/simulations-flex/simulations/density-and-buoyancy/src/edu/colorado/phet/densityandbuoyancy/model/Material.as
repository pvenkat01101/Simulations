//  Copyright 2002-2011, University of Colorado
package edu.colorado.phet.densityandbuoyancy.model {
import edu.colorado.phet.flexcommon.FlexSimStrings;

import flash.display.Bitmap;

/**
 * Materials have a name, density, color and texture--represents both solid (e.g. wood) and liquid (e.g. water) materials.
 */
public class Material {

    // we use embed to get class references so we can instantiate them later

    [Embed(source="../../../../../../data/density-and-buoyancy/images/wood.png")]
    private static var woodTextureClass: Class;

    [Embed(source="../../../../../../data/density-and-buoyancy/images/custom.jpg")]
    public static var customObjectTexture: Class;

    [Embed(source="../../../../../../data/density-and-buoyancy/images/styrofoam.jpg")]
    private static var styrofoamTextureClass: Class;

    [Embed(source="../../../../../../data/density-and-buoyancy/images/aluminum.jpg")]
    private static var aluminumTextureClass: Class;

    [Embed(source="../../../../../../data/density-and-buoyancy/images/wall.jpg")]
    private static var brickTextureClass: Class;

    [Embed(source="../../../../../../data/density-and-buoyancy/images/ice.jpg")]
    private static var iceTextureClass: Class;

    public static const STYROFOAM: Material = new Material( FlexSimStrings.get( "material.styrofoam", "Styrofoam" ), 150, false, 0xcccccc, new styrofoamTextureClass() );//between 25 and 200 according to http://wiki.answers.com/Q/What_is_the_density_of_styrofoam; chose 150 so it isn't too low to show on slider, but not 200 so it's not half of wood
    public static const WOOD: Material = new Material( FlexSimStrings.get( "material.wood", "Wood" ), 400, false, 0xa87113, new woodTextureClass() );
    public static const ICE: Material = new Material( FlexSimStrings.get( "material.ice", "Ice" ), 919, false, 0x6fbcff, new iceTextureClass(), 0.75 );
    public static const WATER: Material = new Material( FlexSimStrings.get( "material.water", "Water" ), 1000.0, false, 0x0088FF );
    public static const BRICK: Material = new Material( FlexSimStrings.get( "material.brick", "Brick" ), 2000, false, 0xd34f32, new brickTextureClass() );//see http://www.simetric.co.uk/si_materials.htm also tuned so that in BuoyancySameVolumeMode the brick is exactly 10.0kg
    public static const ALUMINUM: Material = new Material( FlexSimStrings.get( "material.aluminum", "Aluminum" ), 2700, false, 0x646464, new aluminumTextureClass() );
    public static const CUSTOM: Material = new Material( FlexSimStrings.get( "material.custom", "My Block" ), 1000.0, true, new customObjectTexture() );

    //NOTE: If other materials less dense than Wood are added, then a volume-bounding solution will need to be applied, like
    //the workaround in PropertyEditor.createSlider
    public static const SELECTABLE_MATERIALS: Array = [STYROFOAM, WOOD, ICE, BRICK, ALUMINUM];//Note that Custom is omitted from here, though it is added in some places where this list is used
    public static const LABELED_DENSITY_MATERIALS: Array = [STYROFOAM, WOOD, ICE, BRICK, ALUMINUM];

    public static const AIR: Material = new Material( FlexSimStrings.get( "material.air", "Air" ), 1.2, false, 0x666666 );
    public static const LEAD: Material = new Material( FlexSimStrings.get( "material.lead", "Lead" ), 11340, false );
    public static const DIAMOND: Material = new Material( FlexSimStrings.get( "material.diamond", "Diamond" ), 3530, false );
    public static const GOLD: Material = new Material( FlexSimStrings.get( "material.gold", "Gold" ), 19300, false );
    public static const GASOLINE: Material = new Material( FlexSimStrings.get( "material.gasoline", "Gasoline" ), 700, false, 0x444400 );
    public static const OLIVE_OIL: Material = new Material( FlexSimStrings.get( "material.oliveOil", "Olive Oil" ), 918, false, 0xAAAA00 );
    public static const HONEY: Material = new Material( FlexSimStrings.get( "material.honey", "Honey" ), 1420, false, 0xCC8800 );
    public static const APPLE: Material = new Material( FlexSimStrings.get( "material.apple", "Apple" ), 641, false );

    public static const LABELED_LIQUID_MATERIALS: Array = [AIR,GASOLINE,OLIVE_OIL,WATER,HONEY];

    private static function sortOnDensity( a: Material, b: Material ): Number {
        var aValue: Number = a.getDensity();
        var bValue: Number = b.getDensity();

        if ( aValue > bValue ) {
            return 1;
        }
        else {
            if ( aValue < bValue ) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public static var ALL: Array = [ ALUMINUM, APPLE, DIAMOND, GASOLINE, GOLD, ICE, LEAD, WATER, WOOD];//sorted below
    ALL.sort( sortOnDensity );

    private var density: Number;
    private var _name: String;
    private var _isCustom: Boolean;
    private var _tickColor: uint;
    private var _textureBitmap: Bitmap;
    private var _alpha: Number = 1;

    /**
     * Create a new material
     * @param name Translation key for the name
     * @param density Density in SI
     * @param isCustom Whether this is a "custom" material. ("my block")
     * @param tickColor If this will be put on a density slider, what are the tick colors?
     * @param textureBitmap (solid only) What texture should our blocks of this material type have?
     * @param alpha (solid only) What alpha value should this material have?
     */
    public function Material( name: String, density: Number, isCustom: Boolean, tickColor: uint = 0x000000, textureBitmap: Bitmap = null, alpha: Number = 1 ) {
        this.density = density;
        this._name = name;
        this._isCustom = isCustom;
        this._tickColor = tickColor;
        this._textureBitmap = textureBitmap;
        this._alpha = alpha;
    }

    public function get alpha(): Number {
        return _alpha;
    }

    public function getDensity(): Number {
        return density;
    }

    public function get name(): String {
        return _name;
    }

    public function equals( material: Material ): Boolean {
        return material.density == density && material._name == _name && material._isCustom == _isCustom;
    }

    public function isCustom(): Boolean {
        return _isCustom;
    }

    public function get tickColor(): uint {
        return _tickColor;
    }

    public function get textureBitmap(): Bitmap {
        return _textureBitmap;
    }
}
}