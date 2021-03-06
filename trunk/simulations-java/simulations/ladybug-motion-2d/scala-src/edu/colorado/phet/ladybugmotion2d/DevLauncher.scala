package edu.colorado.phet.ladybugmotion2d

import edu.colorado.phet.common.phetcommon.application.ApplicationConstructor
import edu.colorado.phet.common.phetcommon.view.util.SwingUtils
import edu.colorado.phet.common.phetcommon.view.VerticalLayoutPanel
import java.awt.event.{ActionListener, KeyAdapter, ActionEvent, KeyEvent}
import javax.swing._
import edu.colorado.phet.common.phetcommon.application.PhetApplicationLauncher
import edu.colorado.phet.common.phetcommon.application.PhetApplication
import edu.colorado.phet.common.phetcommon.application.PhetApplicationConfig
import edu.colorado.phet.scalacommon.ScalaClock
import edu.colorado.phet.ladybugmotion2d.model.LadybugModel

object DevLauncher {
  def main(args: Array[String]) {
    val dialog = new JFrame
    val contentPane = new VerticalLayoutPanel
    val checkBox: JCheckBox = new JCheckBox("Remote Control is an indicator too", LadybugDefaults.remoteIsIndicator)
    checkBox.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        LadybugDefaults.remoteIsIndicator = checkBox.isSelected
      }
    })
    contentPane.add(new JLabel("Ladybug 2D Options"))
    contentPane.setFillNone()
    contentPane.add(checkBox)

    val stickyCheckBox = new JCheckBox("Velocity and Acceleration are sticky", LadybugDefaults.vaSticky)
    stickyCheckBox.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        LadybugDefaults.vaSticky = stickyCheckBox.isSelected
      }
    })
    contentPane.add(stickyCheckBox)

    val timelineLengthTextField = new JTextField("" + LadybugDefaults.timelineLengthSeconds, 20)
    timelineLengthTextField.setBorder(BorderFactory.createTitledBorder("Timeline length (s)"))
    timelineLengthTextField.addKeyListener(new KeyAdapter() {
      override def keyReleased(e: KeyEvent) {
        LadybugDefaults.timelineLengthSeconds = Integer.parseInt(timelineLengthTextField.getText)
      }
    })

    contentPane.add(timelineLengthTextField)

    val jButton: JButton = new JButton("Launch")
    jButton.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        val clock = new ScalaClock(30, 30 / 1000.0)
        new Thread(new Runnable() {
          //put into thread so it can call invokeAndWait
          def run {
            dialog.dispose()
            new PhetApplicationLauncher().launchSim(
              new PhetApplicationConfig(args, "moving-man", "ladybug-2d"),
              new ApplicationConstructor() {
                override def getApplication(a: PhetApplicationConfig): PhetApplication = new PhetApplication(a) {
                  addModule(new LadybugModule[LadybugModel](clock))
                }
              })
          }
        }).start()
      }
    })
    contentPane.add(jButton)
    dialog.setContentPane(contentPane)
    dialog.pack()
    SwingUtils.centerWindowOnScreen(dialog)
    dialog.setVisible(true)
  }
}