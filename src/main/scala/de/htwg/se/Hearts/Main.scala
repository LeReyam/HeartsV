import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.view._
import de.htwg.se.Hearts.controller._

@main def runHearts(): Unit =

  val playernames = List("Alice","Bob")
  val game = Dealer.deal(Dealer.shuffle(Dealer.createDeck()), playernames)
  val controller = new GameController(game)
  val gameView = new GameView(controller)
  controller.runGame()