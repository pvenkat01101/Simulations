package edu.colorado.phet.ladybugmotion2d.controlpanel

import edu.colorado.phet.scalacommon.util.Observable

class VectorVisibilityModel extends Observable {
  var velVis = true
  var accelVis = true

  def isVelocityVisible = velVis

  def velocityVectorVisible: Boolean = velVis

  def accelerationVectorVisible: Boolean = accelVis

  def velocityVectorVisible_=(x: Boolean) {
    velVis = x
    notifyListeners()
  }

  def accelerationVectorVisible_=(x: Boolean) {
    accelVis = x
    notifyListeners()
  }

  def resetAll() {
    velocityVectorVisible = true
    accelerationVectorVisible = true
  }
}