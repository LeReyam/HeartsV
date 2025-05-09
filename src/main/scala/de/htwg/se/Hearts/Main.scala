import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.view._
import de.htwg.se.Hearts.controller._

@main def runHearts(): Unit =
  val alice = new Player("Alice", List(
    Card(Rank.Two, Suit.Hearts),
    Card(Rank.Ace, Suit.Spades),
    Card(Rank.Jack, Suit.Diamonds)
  ))
  val bob = new Player("Bob", List(
    Card(Rank.Ten, Suit.Clubs),
    Card(Rank.King, Suit.Diamonds),
    Card(Rank.Queen, Suit.Hearts)
  ))

  val game = new Game(List(alice, bob))
  val controller = new GameController(game)
  val gameView = new GameView(controller)
  controller.runGame()
  println("Game over!")

