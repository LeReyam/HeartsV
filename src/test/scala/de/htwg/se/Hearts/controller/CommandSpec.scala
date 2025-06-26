package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class CommandSpec extends AnyWordSpec with Matchers {

  "A PlayCardCommand" should {

    "execute a card play correctly" in {
      val player1 = new HumanPlayer("Player 1", List(
        Card(Rank.Ace, Suit.Hearts),
        Card(Rank.King, Suit.Clubs)
      ))
      val player2 = new HumanPlayer("Player 2", List(
        Card(Rank.Queen, Suit.Diamonds),
        Card(Rank.Jack, Suit.Spades)
      ))
      val game = new Game(List(player1, player2))
      val controller = new GameController()
      controller.initializeGame(game)

      val command = new PlayCardCommand(controller, 0)

      controller.getCurrentPlayerName should be("Player 1")
      controller.getCurrentPot.size should be(0)
      player1.hand.size should be(2)

      command.execute() should be(true)

      controller.getCurrentPlayerName should be("Player 2")
      controller.getCurrentPot.size should be(1)
      controller.getCurrentPot.head should be(Card(Rank.Ace, Suit.Hearts))
      player1.hand.size should be(1)
    }

    "undo a card play correctly" in {
      val player1 = new HumanPlayer("Player 1", List(
        Card(Rank.Ace, Suit.Hearts),
        Card(Rank.King, Suit.Clubs)
      ))
      val player2 = new HumanPlayer("Player 2", List(
        Card(Rank.Queen, Suit.Diamonds),
        Card(Rank.Jack, Suit.Spades)
      ))
      val game = new Game(List(player1, player2))
      val controller = new GameController()
      controller.initializeGame(game)

      val command = new PlayCardCommand(controller, 0)
      command.execute() should be(true)

     
      controller.getCurrentPlayerName should be("Player 2")
      controller.getCurrentPot.size should be(1)
      player1.hand.size should be(1)

      command.undo() should be(true)


      controller.getCurrentPlayerName should be("Player 1")
      controller.getCurrentPot.size should be(0)
      player1.hand.size should be(2)
      player1.hand should contain(Card(Rank.Ace, Suit.Hearts))
    }

    "redo a card play correctly" in {
      val player1 = new HumanPlayer("Player 1", List(
        Card(Rank.Ace, Suit.Hearts),
        Card(Rank.King, Suit.Clubs)
      ))
      val player2 = new HumanPlayer("Player 2", List(
        Card(Rank.Queen, Suit.Diamonds),
        Card(Rank.Jack, Suit.Spades)
      ))
      val game = new Game(List(player1, player2))
      val controller = new GameController()
      controller.initializeGame(game)

      val command = new PlayCardCommand(controller, 0)
      command.execute() should be(true)
      command.undo() should be(true)


      controller.getCurrentPlayerName should be("Player 1")
      controller.getCurrentPot.size should be(0)
      player1.hand.size should be(2)

      command.redo() should be(true)


      controller.getCurrentPlayerName should be("Player 2")
      controller.getCurrentPot.size should be(1)
      player1.hand.size should be(1)
    }

    "handle scoring when all players have played a card" in {
      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Two, Suit.Clubs)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts), Card(Rank.Queen, Suit.Spades)))
      val newGame = new Game(List(p1, p2))
      val newController = new GameController()
      newController.initializeGame(newGame)


      val command1 = new PlayCardCommand(newController, 0)
      command1.execute() should be(true)
      newController.getPlayerPoints(0) should be(0)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(1)


      val command2 = new PlayCardCommand(newController, 0)
      command2.execute() should be(true)
      newController.getPlayerPoints(0) should be(2)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(2)


      command2.undo() should be(true)
      newController.getPlayerPoints(0) should be(0)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(1)


      command2.redo() should be(true)
      newController.getPlayerPoints(0) should be(2)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(2)


      val command3 = new PlayCardCommand(newController, 0)
      command3.execute() should be(true)
      newController.getCurrentPot.size should be(1)
    }

    "handle pot clearing correctly with undo/redo" in {
      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Two, Suit.Clubs), Card(Rank.Three, Suit.Diamonds)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts), Card(Rank.Queen, Suit.Spades), Card(Rank.Four, Suit.Diamonds)))
      val newGame = new Game(List(p1, p2))
      val newController = new GameController()
      newController.initializeGame(newGame)


      val cmd1 = new PlayCardCommand(newController, 0)
      cmd1.execute() should be(true)
      val cmd2 = new PlayCardCommand(newController, 0)
      cmd2.execute() should be(true)

      newController.getCurrentPot.size should be(2)
      newController.getPlayerPoints(0) should be(2)


      val cmd3 = new PlayCardCommand(newController, 0)
      cmd3.execute() should be(true)

      newController.getCurrentPot.size should be(1)


      cmd3.undo() should be(true)

      newController.getCurrentPot.size should be(2)
      newController.getCurrentPot should contain(Card(Rank.Ace, Suit.Hearts))
      newController.getCurrentPot should contain(Card(Rank.King, Suit.Hearts))


      cmd3.redo() should be(true)

      newController.getCurrentPot.size should be(1)
    }

    "handle invalid card indices" in {
      val player1 = new HumanPlayer("Player 1", List(Card(Rank.Ace, Suit.Hearts)))
      val player2 = new HumanPlayer("Player 2", List(Card(Rank.King, Suit.Hearts)))
      val game = new Game(List(player1, player2))
      val controller = new GameController()
      controller.initializeGame(game)

      val invalidCommand = new PlayCardCommand(controller, -1)
      invalidCommand.execute() should be(false)

      val outOfBoundsCommand = new PlayCardCommand(controller, 10)
      outOfBoundsCommand.execute() should be(false)
    }
  }

  "A Card Command" should {
    val controller = new GameController()
    "handle a uninitialized game" in {
      val command = new PlayCardCommand(controller, 0)
      command.execute() should be(false)
      command.undo() should be(false)
      command.redo() should be(false)
    }
  }
}