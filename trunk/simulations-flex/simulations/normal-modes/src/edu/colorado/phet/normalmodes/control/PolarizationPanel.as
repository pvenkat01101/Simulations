/*
 * Copyright 2002-2012, University of Colorado
 */

/**
 * Created by IntelliJ IDEA.
 * User: Dubson
 * Date: 7/14/11
 * Time: 7:10 AM
 * To change this template use File | Settings | File Templates.
 */
package edu.colorado.phet.normalmodes.control {
import edu.colorado.phet.flashcommon.controls.NiceLabel;
import edu.colorado.phet.flexcommon.FlexSimStrings;
import edu.colorado.phet.flexcommon.util.SpriteUIComponent;
import edu.colorado.phet.normalmodes.util.TwoHeadedArrow;
import edu.colorado.phet.normalmodes.view.MainView;

import flash.events.Event;

import mx.containers.Canvas;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.RadioButton;
import mx.controls.RadioButtonGroup;

/**
 * Small control panel for setting horizontal or vertical polarization mode in 1D mode.
 */
public class PolarizationPanel extends Canvas {
    private var myMainView: MainView;
    private var myModel: Object;     //could be either 1D or 2D model, but currently only used for 1D

    //Polarization radio buttons
    private var background: VBox;
    private var polarizationLabel: NiceLabel;
    private var modeTypeHBox: HBox;
    private var directionOfMode_rbg: RadioButtonGroup;
    private var horizPolarizationButton: RadioButton;
    private var vertPolarizationButton: RadioButton;
    private var horizArrow: TwoHeadedArrow;       //icon representing horizontal mode
    private var vertArrow: TwoHeadedArrow;        //icon representing vertical mode

    public var polarization_str: String;

    public function PolarizationPanel( myMainView: MainView, myModel: Object ) {
        super();
        this.myMainView = myMainView;
        this.myModel = myModel;
        this.init();
    }//end constructor

    private function init(): void {
        this.polarization_str = FlexSimStrings.get( "polarization:", "Polarization<br>Control:" );

        this.background = new VBox();
        with ( this.background ) {
            setStyle( "backgroundColor", 0x88ff88 );
            percentWidth = 100;
            setStyle( "borderStyle", "solid" );
            setStyle( "borderColor", 0x0000ff );
            setStyle( "cornerRadius", 6 );
            setStyle( "borderThickness", 2 );
            setStyle( "paddingTop", 0 );
            setStyle( "paddingBottom", 5 );
            setStyle( "paddingRight", 3 );
            setStyle( "paddingLeft", 8 );
            setStyle( "verticalGap", 0 );
            setStyle( "horizontalAlign", "center" );
        }

        //Set up polarization radio button box
        this.polarizationLabel = new NiceLabel( 12, polarization_str, true );
        this.polarizationLabel.centerText();
        this.modeTypeHBox = new HBox();
        this.directionOfMode_rbg = new RadioButtonGroup();
        this.horizPolarizationButton = new RadioButton();
        this.vertPolarizationButton = new RadioButton();
        //Create and position icons
        this.horizArrow = new TwoHeadedArrow();
        this.horizArrow.height = 10;
        this.horizArrow.width = 20;
        this.horizArrow.y = -0.5 * this.horizArrow.height;   //I don't understand why this must be negative.
        this.horizArrow.x = 5;                               //and why this is positive
        this.vertArrow = new TwoHeadedArrow();
        this.vertArrow.height = 10;
        this.vertArrow.width = 20;
        this.vertArrow.rotation = -90;
        this.vertArrow.x = 5;
        //set up radio buttons
        this.horizPolarizationButton.group = directionOfMode_rbg;
        this.vertPolarizationButton.group = directionOfMode_rbg;
        this.horizPolarizationButton.value = 1;
        this.vertPolarizationButton.value = 0;
        this.horizPolarizationButton.selected = false;
        this.vertPolarizationButton.selected = true;
        this.directionOfMode_rbg.addEventListener( Event.CHANGE, setPolarization );

        //Polarization type radio buttons
        this.addChild( this.background );
        this.background.addChild( new SpriteUIComponent( this.polarizationLabel ) );
        this.background.addChild( this.modeTypeHBox );
        this.modeTypeHBox.addChild( this.horizPolarizationButton );
        this.modeTypeHBox.addChild( new SpriteUIComponent( this.horizArrow, true ) );
        this.modeTypeHBox.addChild( this.vertPolarizationButton );
        this.modeTypeHBox.addChild( new SpriteUIComponent( this.vertArrow, true ) );
    }//end init()

    //Unused.
    public function setModel( currentModel: Object ): void {
        this.myModel = currentModel;
    }

    private function setPolarization( evt: Event ): void {
        var val: Object = this.directionOfMode_rbg.selectedValue;
        if ( val == 1 ) {
            this.myModel.xModes = true;
        }
        else {
            this.myModel.xModes = false;
        }
    }//end setPolarization();


}//end class
}//end package
