/*import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.view._
import de.htwg.se.Hearts.controller._

@main def runHearts(): Unit =
  val controller = new GameController()
  val gameView = new GameView(controller)
  controller.runGame()*/
package de.htwg.se.Hearts

import scalafx.application.JFXApp3
import de.htwg.se.Hearts.controller.GameController
import de.htwg.se.Hearts.view.Gui

object Main extends JFXApp3 {

  val controller = new GameController()

  // TUI in separatem Thread
  new Thread(() => {
    scala.io.Source.stdin.getLines().foreach(controller.handleInput)
  }).start()

  // GUI starten
  override def start(): Unit = {
    val gui = new Gui(controller)
    stage = new JFXApp3.PrimaryStage {
      title = "Hearts"
      scene = gui.scene
    }
  }
}