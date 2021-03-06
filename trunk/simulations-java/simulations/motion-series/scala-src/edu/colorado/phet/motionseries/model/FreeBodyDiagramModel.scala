package edu.colorado.phet.motionseries.model

import edu.colorado.phet.scalacommon.util.Observable
import edu.colorado.phet.common.phetcommon.math.MathUtil
import edu.colorado.phet.motionseries.MotionSeriesDefaults

/**
 * This file contains model components for MVC pattern for customizable or interactive behaviors.
 * @author Sam Reid
 */
class FreeBodyDiagramModel(val popupDialogOnly: Boolean) extends Observable {
  private var _windowed = false
  private var _visible = false
  private var _closable = true

  resetAll()

  def resetAll() {
    windowed = false
    visible = false
    closable = true
  }

  def closable = _closable

  def closable_=(b: Boolean) {
    _closable = b
    notifyListeners()
  }

  def visible = _visible

  def windowed = _windowed || popupDialogOnly

  def visible_=(value: Boolean) {
    _visible = value
    notifyListeners()
  }

  def windowed_=(value: Boolean) {
    _windowed = value
    notifyListeners()
  }

}

class AdjustableCoordinateModel extends Observable {
  private var _fixed = true

  resetAll()

  def resetAll() {
    fixed = true
  }

  def fixed = _fixed

  def adjustable = !_fixed

  def fixed_=(b: Boolean) {
    _fixed = b
    notifyListeners()
  }

  def adjustable_=(b: Boolean) {
    _fixed = !b
    notifyListeners()
  }
}

class CoordinateFrameModel(rampSegment: RampSegment) extends Observable {
  //TODO: if snapped to the ramp, should rotate with ramp
  private var _proposedAngle = 0.0 //the angle the user has tried to drag the coordinate frame to, not including snapping
  private val snapRange = 5.0.toRadians
  private var snappedToRamp = false

  rampSegment.addListener(() => if ( snappedToRamp ) {
    proposedAngle = rampSegment.angle
  })

  def angle = {
    val angleList = rampSegment.angle :: 0.0 :: Nil
    val acceptedAngles = for ( s <- angleList if ( proposedAngle - s ).abs < snapRange ) yield {
      s
    }
    if ( acceptedAngles.length == 0 ) {
      proposedAngle
    }
    else {
      acceptedAngles(0)
    } //take the first snap angle from the list
  }

  def proposedAngle = _proposedAngle

  def proposedAngle_=(d: Double) {
    _proposedAngle = MathUtil.clamp(0, d, MotionSeriesDefaults.MAX_ANGLE)
    notifyListeners()
  }

  def dropped() {
    snappedToRamp = angle == rampSegment.angle
  }
}