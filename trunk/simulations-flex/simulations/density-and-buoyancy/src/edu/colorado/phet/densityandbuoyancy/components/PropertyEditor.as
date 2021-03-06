//  Copyright 2002-2011, University of Colorado
package edu.colorado.phet.densityandbuoyancy.components {
import edu.colorado.phet.densityandbuoyancy.DensityAndBuoyancyConstants;
import edu.colorado.phet.densityandbuoyancy.view.units.Unit;
import edu.colorado.phet.flashcommon.MathUtil;
import edu.colorado.phet.flexcommon.model.NumericProperty;

import flash.display.DisplayObject;
import flash.events.FocusEvent;
import flash.events.KeyboardEvent;

import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.controls.Label;
import mx.controls.TextInput;
import mx.events.FlexEvent;
import mx.events.SliderEvent;

/**
 * Control component with a label, text box and slider--like a LinearValueControl in Java common that uses a NumericProperty for its model.
 */
public class PropertyEditor extends GridRow {
    private var _property: NumericProperty;
    private static const FONT_SIZE: Number = 12;
    private var sliderWidth: Number;
    protected const textField: TextInput = new TextInput();
    protected var _sliderDecorator: SliderDecorator;
    private var _unit: Unit;
    protected var addLabels: Boolean;
    private var userChanged: Boolean = false;

    public function PropertyEditor( property: NumericProperty, minimum: Number, maximum: Number, unit: Unit, dataTipClamp: Function, bounds: NumericClamp, sliderWidth: Number = 280, addLabels: Boolean = true ) {
        super();
        this.addLabels = addLabels;
        this.sliderWidth = sliderWidth;
        this._property = property;
        this._unit = unit;

        var label: Label = new Label();
        label.text = property.name;
        label.setStyle( DensityAndBuoyancyConstants.FLEX_FONT_SIZE, FONT_SIZE );
        label.setStyle( DensityAndBuoyancyConstants.FLEX_FONT_WEIGHT, DensityAndBuoyancyConstants.FLEX_FONT_BOLD );
        addGridItem( label );

        _sliderDecorator = createSlider( property, minimum, maximum, unit, bounds );
        if ( addLabels ) {
            addGridItem( _sliderDecorator );
        }
        else {
            addGridItem( _sliderDecorator, 3 );
        }

        if ( addLabels ) {

            textField.setStyle( DensityAndBuoyancyConstants.FLEX_FONT_SIZE, FONT_SIZE );
            textField.setStyle( "disabledColor", 0x000000 );
            //        textField.setStyle( "fontWeight", "bold" );
            textField.setStyle( "backgroundDisabledColor", 0xEEEEEE );
            textField.width = DensityAndBuoyancyConstants.SLIDER_READOUT_TEXT_FIELD_WIDTH;
            textField.restrict = ".0-9";//TODO: does this handle languages that use a comma instead of a decimal place?
            function updateText(): void {
                textField.text = DensityAndBuoyancyConstants.format( unit.fromSI( property.value ) );
            }

            function updateModelFromTextField(): void {
                if ( !userChanged ) {
                    return;
                }
                userChanged = true;
                const number: Number = unit.toSI( Number( textField.text ) );
                property.value = bounds.clamp( MathUtil.clamp( minimum, number, maximum ) );
                if ( number != property.value ) {
                    // it was changed!
                    updateText();
                }
            }

            updateText();
            const listener: Function = function( evt: Object ): void {
                if ( focusManager != null ) { //Have to do a null check because it can be null if the component is not in the scene graph?
                    if ( focusManager.getFocus() == textField ) {//Only update the model if the user is editing the text field, otherwise there are loops that cause errant behavior
                        updateModelFromTextField();
                    }
                }
            };

            textField.addEventListener( FocusEvent.FOCUS_OUT, function( evt: Object ): void {
                updateModelFromTextField();
            } );
//            textField.addEventListener( FlexEvent.VALUE_COMMIT, listener ); // removed since this was definitely not necessary
            textField.addEventListener( FlexEvent.ENTER, listener );
            textField.addEventListener( KeyboardEvent.KEY_DOWN, function( evt: KeyboardEvent ): void {
                // If the user types into the text field, we mark it as user-changed so that another type of event will cause an update.
                // This prevents the User mass change => volume change / clamp => incorrect mass change bug
                userChanged = true;
            } );

            property.addListener( updateText );
            addGridItem( textField );

            var unitsLabel: Label = new Label();
            unitsLabel.setStyle( DensityAndBuoyancyConstants.FLEX_FONT_SIZE, FONT_SIZE );
            unitsLabel.text = unit.name;
            addGridItem( unitsLabel );
        }
    }

    //The density slider requires a reference to the density object in order to bound the volume when necessary.
    //This is because when selecting Styrofoam or other non-dense objects, then moving the mass slider to maximum,
    //The volume increases dramatically, making the object larger than the pool size.
    protected function createSlider( property: NumericProperty, minimum: Number, maximum: Number, unit: Unit, bounds: NumericClamp ): SliderDecorator {
        const slider: SliderDecorator = new SliderDecorator( getSliderThumbOffset() );
        slider.sliderThumbClass = getSliderThumbClass();
        slider.sliderWidth = sliderWidth;

        slider.minimum = unit.fromSI( minimum );
        slider.maximum = unit.fromSI( maximum );
        slider.liveDragging = true;//Update values during drag instead of only after dropping the thumb

        function sliderDragHandler( event: SliderEvent ): void {
            var newValue: Number = unit.toSI( event.value );
            newValue = bounds.clamp( newValue );
            property.value = newValue;
            slider.value = unit.fromSI( newValue );//Make sure the slider thumb does not move beyond the clamped value (none of the other indicators move beyond the clamped value either)
        }

        //This different functionality is necessary to support track presses and keyboard handling
        //Otherwise, when one slider reaches its min or max, it sets that value back on the model
        function trackPressAndKeyboardHandler( event: SliderEvent ): void {
            if ( event.value != slider.maximum && event.value != slider.minimum ) {
                sliderDragHandler( event );
            }
        }

        slider.addSliderEventListener( SliderEvent.THUMB_DRAG, sliderDragHandler );
        slider.addSliderEventListener( SliderEvent.CHANGE, trackPressAndKeyboardHandler );
        function updateSlider(): void {
            const setValue: Number = unit.fromSI( property.value );
            slider.value = setValue;
            try {
                var alphaValue: Number = 1;
                if ( setValue > slider.maximum || setValue < slider.minimum ) {
                    alphaValue = 0.25;
                }
                slider.getThumbAt( 0 ).alpha = alphaValue;
            }
            catch( exception: Error ) {

            }
        }

        updateSlider();
        property.addListener( updateSlider );

        return slider;
    }

    protected function getSliderThumbOffset(): Number {
        return 0;
    }

    protected function getSliderThumbClass(): Class {
        return BiggerSliderThumb;
    }

    private function addGridItem( displayObject: DisplayObject, colSpan: Number = 1 ): void {
        const item: GridItem = new GridItem();
        item.addChild( displayObject );
        item.colSpan = colSpan;
        item.setStyle( "verticalAlign", "middle" );
        addChild( item );
    }

    protected override function updateDisplayList( unscaledWidth: Number, unscaledHeight: Number ): void {
        super.updateDisplayList( unscaledWidth, unscaledHeight );
    }

    public function get property(): NumericProperty {
        return _property;
    }

    public function get unit(): Unit {
        return _unit;
    }
}
}