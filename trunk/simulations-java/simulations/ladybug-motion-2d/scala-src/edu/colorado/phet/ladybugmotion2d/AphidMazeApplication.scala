package edu.colorado.phet.ladybugmotion2d

import aphidmaze.AphidMazeModule
import edu.colorado.phet.scalacommon.{ScalaClock, ScalaApplicationLauncher}

//aphid maze is a tab in ladybug 2d, just an application during development to facilitate deployment and testing
object AphidMazeApplication {
  def main(args: Array[String]) = {
    ScalaApplicationLauncher.launchApplication(args, "ladybug-motion-2d", "aphid-maze", () => new AphidMazeModule(new ScalaClock(30, 30 / 1000.0)))
  }
}