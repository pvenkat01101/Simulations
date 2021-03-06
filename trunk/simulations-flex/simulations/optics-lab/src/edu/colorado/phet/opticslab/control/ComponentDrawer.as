/*
 * Copyright 2002-2012, University of Colorado
 */

/**
 * Created by Dubson on 5/25/2014.
 * View and Controller. "Drawer" from which user can grab light sources and optical components.
 */
package edu.colorado.phet.opticslab.control {
import edu.colorado.phet.opticslab.view.*;
import edu.colorado.phet.opticslab.model.OpticsModel;

import flash.display.Graphics;

import flash.display.Sprite;
import flash.events.MouseEvent;

public class ComponentDrawer extends Sprite {
    private var myMainView: MainView;
    private var myOpticsModel: OpticsModel;
    private var sourceCompartment: Sprite; //compartment containing light sources
    private var maskCompartment: Sprite;   //compartment containing aperature masks
    private var lensCompartment: Sprite;   //contains lenses
    private var mirrorCompartment: Sprite; //contains mirrors
    private var stageW: Number;     //width of main stage, read from MainView
    private var stageH: Number;     //height of main stage, read from MainView

    public function ComponentDrawer( mainView: MainView, opticsModel: OpticsModel ) {
        myMainView = mainView;
        myOpticsModel = opticsModel;
        this.stageW = myMainView.stageW;
        this.stageH = myMainView.stageH;
        this.sourceCompartment = new Sprite();
        this.maskCompartment = new Sprite();
        this.lensCompartment = new Sprite();
        this.mirrorCompartment = new Sprite();
        //no need to register with Model since this is a controller.
        init();
    }//end constructor

    private function init():void{
        this.addChild( sourceCompartment );
        this.addChild( maskCompartment );
        this.addChild( lensCompartment );
        this.addChild( mirrorCompartment );
        this.drawGraphics();
        makeCompartmentsActive();

    }//end init()

    //Draw box representing drawer full of components and compartments for each type
    private function drawGraphics():void{
        var cornerRadius: int = 15;
        var lineColor: uint = 0xffffff;
        var fillColor: uint  = 0x00ff00;
        var w:Number = 0.7*stageW; //width and height of outer perimeter
        var h:Number = 0.15*stageH;
        var gBox:Graphics = this.graphics;
        //draw outer box
        with( gBox ){
            clear();
            lineStyle( 3, lineColor );
            beginFill( fillColor, 1 );
            drawRoundRect( 0, 0, w, h, cornerRadius, cornerRadius );
//            moveTo( 0, 0 );
//            lineTo( w, 0 );
//            lineTo( w, h );
//            lineTo( 0, h );
//            lineTo( 0, 0 );
            endFill();
        }
        var gS:Graphics = sourceCompartment.graphics;  //draw light source compartment
        lineColor = 0x0000ff;
        fillColor = 0x99ff99;
        with(gS){
            clear();
            lineStyle( 3, lineColor );
            beginFill( fillColor );
            drawRoundRect( 0.1*h, 0.1*h, 0.2*w, 0.8*h, cornerRadius, cornerRadius );
            endFill();
        }
        var gM:Graphics = maskCompartment.graphics;  //draw light source compartment
        lineColor = 0x0000ff;
        fillColor = 0x99ff99;
        with(gM){
            clear();
            lineStyle( 3, lineColor );
            beginFill( fillColor );
            drawRoundRect( 0.2*h + 0.2*w , 0.1*h, 0.2*w, 0.8*h, cornerRadius, cornerRadius );
            endFill();
        }

    }//end drawGraphics()

    /*
    * Graphic view of component is created when user clicks on component drawer, and graphic
    * is draggable onto stage. Component is not instantiated unless its graphic is released on
    * stage. Component views dragged into drawer and released are destroyed.
    * */
    private function makeCompartmentsActive():void {
        sourceCompartment.buttonMode = true;
        var thisSprite: Object = this;
        sourceCompartment.addEventListener( MouseEvent.MOUSE_DOWN, createNewLightSource );
        maskCompartment.addEventListener( MouseEvent.MOUSE_DOWN, createNewMask )
//        function createNewLightSource():void{
//            myOpticsModel.createNewLightSource();
//        }
        maskCompartment.buttonMode = true;
    }

    private function createNewLightSource( evt:MouseEvent ):void{
        //trace("ComponentDrawer.createNewLightSource called.");
        myOpticsModel.createNewLightSource();
    }

    private function createNewMask( evt:MouseEvent ):void{
        myOpticsModel.createNewMask();
    }

}//end class
}//end package
