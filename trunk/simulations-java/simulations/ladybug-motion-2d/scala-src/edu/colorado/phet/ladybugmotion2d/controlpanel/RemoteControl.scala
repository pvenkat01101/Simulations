package edu.colorado.phet.ladybugmotion2d.controlpanel

import edu.colorado.phet.common.phetcommon.view.util.PhetFont
import edu.colorado.phet.common.piccolophet.event.CursorHandler
import javax.swing.border.BevelBorder
import edu.colorado.phet.common.piccolophet.nodes.PhetPPath
import edu.colorado.phet.common.phetcommon.view.graphics.transforms.ModelViewTransform2D
import edu.colorado.phet.common.phetcommon.view.VerticalLayoutPanel
import edu.colorado.phet.common.piccolophet.nodes.ArrowNode
import edu.colorado.phet.common.piccolophet.PhetPCanvas
import java.awt.geom.{Rectangle2D, Ellipse2D, Point2D}
import java.awt.{Rectangle, Dimension, Color}
import javax.swing._
import edu.colorado.phet.scalacommon.Predef._
import edu.colorado.phet.ladybugmotion2d.model.LadybugModel
import edu.colorado.phet.ladybugmotion2d.model.Ladybug
import edu.colorado.phet.ladybugmotion2d.LadybugColorSet
import edu.colorado.phet.ladybugmotion2d.LadybugDefaults
import edu.colorado.phet.ladybugmotion2d.LadybugMotion2DResources._
import edu.colorado.phet.scalacommon.util.Observable
import edu.umd.cs.piccolo.util.PDimension
import edu.umd.cs.piccolo.event.PBasicInputEventHandler
import edu.umd.cs.piccolo.event.PInputEvent
import edu.colorado.phet.scalacommon.swing.MyRadioButton
import edu.umd.cs.piccolo.PNode
import edu.colorado.phet.scalacommon.view.ToggleListener
import edu.colorado.phet.scalacommon.math.Vector2D

class RemoteControl(model: LadybugModel, setMotionManual: () => Unit) extends VerticalLayoutPanel with Observable {
  def mode = _mode

  val CANVAS_WIDTH = 155
  val CANVAS_HEIGHT = 155
  val arrowHeadWidth = 30
  val arrowHeadHeight = 30
  val arrowTailWidth = 20
  setBorder(new BevelBorder(BevelBorder.RAISED))
  val positionMode = new RemoteMode(LadybugColorSet.position, 20, _.getPosition) {
    def setLadybugState(pt: Point2D) {
      model.setSamplePoint(pt)
      model.setUpdateModePosition()
    }

    override def dragging_=(d: Boolean) {
      super.dragging_=(d)
      model.setPenDown(d)
    }
  }
  val vectorScale = 14
  val velocityMode = new RemoteMode(LadybugColorSet.velocity, vectorScale, _.getVelocity) {
    def setLadybugState(pt: Point2D) {
      model.ladybug.setVelocity(pt)
      model.setUpdateModeVelocity()
    }
  }
  val accelerationMode = new RemoteMode(LadybugColorSet.acceleration, vectorScale / LadybugDefaults.ACCEL_VECTOR_SCALE, _.getAcceleration) {
    def setLadybugState(pt: Point2D) {
      model.ladybug.setAcceleration(pt)
      model.setUpdateModeAcceleration()
    }
  }
  var _mode: RemoteMode = positionMode;
  _mode.updateArrow()

  abstract class RemoteMode(color: Color, rangeWidth: Double, getter: Ladybug => Vector2D) {
    val transform = new ModelViewTransform2D(new Rectangle2D.Double(-rangeWidth / 2, -rangeWidth / 2, rangeWidth, rangeWidth), new Rectangle(CANVAS_WIDTH, CANVAS_HEIGHT), LadybugDefaults.POSITIVE_Y_IS_UP)
    val arrowNode = new ArrowNode(transform.modelToView(new Point2D.Double(0, 0)), transform.modelToView(new Point2D.Double(0, 0)), arrowHeadWidth, arrowHeadHeight, arrowTailWidth, 0.5, true)
    arrowNode.setPickable(false)
    arrowNode.setChildrenPickable(false)
    arrowNode.setPaint(color)
    private var _dragging = false

    def dragging_=(d: Boolean) {
      _dragging = d
    }

    def dragging = _dragging

    def updateArrow() {
      val doUpdate = ( !dragging && ( RemoteControl.this._mode eq this ) && LadybugDefaults.remoteIsIndicator )
      if ( doUpdate ) {
        _mode.arrowNode.setTipAndTailLocations(_mode.transform.modelToView(getter(model.ladybug)), _mode.transform.modelToView(new Point2D.Double(0, 0)))
      }
    }

    model.ladybug.addListener(() => {
      updateArrow()
    })
    updateArrow()

    def setDestination(pt: Point2D) {
      _mode.arrowNode.setTipAndTailLocations(_mode.transform.modelToView(pt), _mode.transform.modelToView(new Point2D.Double(0, 0)))
      setLadybugState(pt)
    }

    def setLadybugState(pt: Point2D) //template method
  }

  def resetAll() {
    mode = positionMode
  }

  def mode_=(m: RemoteMode) {
    _mode.dragging = false
    _mode = m
    _mode.dragging = false
    canvas.modeChanged()
    _mode.updateArrow()
    notifyListeners()
  }

  def isInteractive = model.readyForInteraction

  class RemoteControlCanvas extends PhetPCanvas(new PDimension(CANVAS_WIDTH, CANVAS_HEIGHT)) {
    val w = 8.5

    def handleMouseDragged(event: PInputEvent) {
      if ( isInteractive ) {
        _mode.dragging = true
        setMotionManual()
        _mode.setDestination(_mode.transform.viewToModel(event.getCanvasPosition.getX, event.getCanvasPosition.getY))
      }
    }

    val centerDot = new PhetPPath(new Ellipse2D.Double(-w / 2, -w / 2, w, w), Color.black)
    centerDot.addInputEventListener(new PBasicInputEventHandler() {
      override def mousePressed(event: PInputEvent) {
        _mode.setDestination(new Vector2D(0, 0))
      }

      override def mouseDragged(event: PInputEvent) {
        handleMouseDragged(event)
      }
    })

    val backgroundNode = new PhetPPath(new Rectangle(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT), Color.white)

    backgroundNode.addInputEventListener(new PBasicInputEventHandler() {
      override def mousePressed(event: PInputEvent) {
        if ( isInteractive ) {
          _mode.dragging = true
          setMotionManual()
          _mode.setDestination(_mode.transform.viewToModel(event.getCanvasPosition.getX, event.getCanvasPosition.getY))
        }
      }

      override def mouseReleased(event: PInputEvent) {
        if ( isInteractive ) {
          _mode.dragging = false
          setMotionManual()
          if ( !LadybugDefaults.vaSticky && ( _mode == velocityMode || _mode == accelerationMode ) ) {
            _mode.setDestination(new Vector2D(0, 0))
          }
        }
      }

      override def mouseDragged(event: PInputEvent) {
        handleMouseDragged(event)
      }
    })
    addInputEventListener(new ToggleListener(new CursorHandler, () => isInteractive))
    modeChanged()

    def modeChanged() {
      centerDot.setOffset(_mode.transform.modelToView(0, 0).getX, _mode.transform.modelToView(0, 0).getY)
    }
  }

  val label = new JLabel(getLocalizedString("controls.remote"))
  label.setFont(new PhetFont(14, true))
  add(label)
  val canvas = new RemoteControlCanvas
  canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT))
  add(canvas)

  val node = new PNode
  canvas.addWorldChild(node)

  def updateNode() {
    node.removeAllChildren()
    node.addChild(canvas.backgroundNode)
    node.addChild(_mode.arrowNode)
    node.addChild(canvas.centerDot)
  }

  updateNode()

  addListener(() => {
    updateNode()
  })
  add(new MyRadioButton(getLocalizedString("model.position"), mode = positionMode, mode == positionMode, this.addListener))
  add(new MyRadioButton(getLocalizedString("model.velocity"), mode = velocityMode, mode == velocityMode, this.addListener))
  add(new MyRadioButton(getLocalizedString("model.acceleration"), mode = accelerationMode, mode == accelerationMode, this.addListener))
  setFillNone()

}