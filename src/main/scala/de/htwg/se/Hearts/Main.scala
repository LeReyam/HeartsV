package de.htwg.se.Hearts

import de.htwg.se.Hearts.controller.GameController
import de.htwg.se.Hearts.view.{Gui, GameView}
import scalafx.application.JFXApp3
import scalafx.scene.Scene

object Main extends JFXApp3 {



  

  override def start(): Unit = {
    val controller = new GameController()
    val gui = new Gui(controller)
    val tui = new GameView(controller)

    stage = new JFXApp3.PrimaryStage {
      title = "Hearts"
      scene = gui.scene
    }

    new Thread(() => {
      var running = true
      while (running) {
        val input = scala.io.StdIn.readLine()
        controller.handleInput(input)
        if (controller.getCurrentState().startsWith("GameOverState")) {
          println("TUI beendet – Spiel ist vorbei.")
          running = false
        }
      }
    }).start()
  }
}
