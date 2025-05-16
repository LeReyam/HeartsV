import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.view._
import de.htwg.se.Hearts.controller._

@main def runHearts(): Unit =
  val controller = new GameController()
  val gameView = new GameView(controller)
  controller.runGame()