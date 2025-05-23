package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class CommandSpec extends AnyWordSpec with Matchers {

  "A PlayCardCommand" should {
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

    "execute a card play correctly" in {
      val command = new PlayCardCommand(controller, 0)

      // Before execution
      controller.getCurrentPlayerName should be("Player 1")
      controller.getCurrentPot.size should be(0)
      player1.hand.size should be(2)

      // Execute
      command.execute() should be(true)

      // After execution
      controller.getCurrentPlayerName should be("Player 2")
      controller.getCurrentPot.size should be(1)
      controller.getCurrentPot.head should be(Card(Rank.Ace, Suit.Hearts))
      player1.hand.size should be(1)
    }

    "undo a card play correctly" in {
      val command = new PlayCardCommand(controller, 0)
      command.execute() should be(true)

      // Before undo
      controller.getCurrentPlayerName should be("Player 1") // After full trick, back to Player 1
      controller.getCurrentPot.size should be(0) // Pot is cleared after scoring
      player1.hand.size should be(1)
      player2.hand.size should be(1)

      // Undo
      command.undo() should be(true)

      // After undo
      controller.getCurrentPlayerName should be("Player 2")
      controller.getCurrentPot.size should be(1)
      player2.hand.size should be(2)
      player2.hand should contain(Card(Rank.Jack, Suit.Spades))
    }

    "redo a card play correctly" in {
      val command = new PlayCardCommand(controller, 0)
      command.execute() should be(true)
      command.undo() should be(true)

      // Before redo
      controller.getCurrentPlayerName should be("Player 2")
      controller.getCurrentPot.size should be(1)
      player2.hand.size should be(2)

      // Redo
      command.redo() should be(true)

      // After redo
      controller.getCurrentPlayerName should be("Player 1") // After full trick, back to Player 1
      controller.getCurrentPot.size should be(0) // Pot is cleared after scoring
      player2.hand.size should be(1)
    }

    "handle scoring when all players have played a card" in {
      // Reset the game
      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Two, Suit.Clubs)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts), Card(Rank.Queen, Suit.Spades)))
      val newGame = new Game(List(p1, p2))
      val newController = new GameController()
      newController.initializeGame(newGame)

      // First player plays a card
      val command1 = new PlayCardCommand(newController, 0)
      command1.execute() should be(true)

      // No scoring yet
      newController.getPlayerPoints(0) should be(0)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(1)

      // Second player plays a card, which should trigger scoring
      val command2 = new PlayCardCommand(newController, 0)
      command2.execute() should be(true)

      // Scoring should have happened
      newController.getPlayerPoints(0) should be(2) // P1 gets 2 points for Ace of Hearts
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(0) // Pot is cleared after scoring

      // Undo the second play (which includes undoing the scoring)
      command2.undo() should be(true)

      // Scoring should be undone
      newController.getPlayerPoints(0) should be(0)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(1)

      // Redo the second play (which includes redoing the scoring)
      command2.redo() should be(true)

      // Scoring should be redone
      newController.getPlayerPoints(0) should be(2)
      newController.getPlayerPoints(1) should be(0)
      newController.getCurrentPot.size should be(0)
    }

    "handle invalid card indices" in {
      val invalidCommand = new PlayCardCommand(controller, -1)
      invalidCommand.execute() should be(false)

      val outOfBoundsCommand = new PlayCardCommand(controller, 10)
      outOfBoundsCommand.execute() should be(false)
    }
  }
  "A Card Command" should {
    val controller = new GameController()
    "handle a unitizialized game" in{
      val command = new PlayCardCommand(controller, 0)
      command.execute() should be (false)
      command.undo() should be (false)
      command.redo() should be (false)
    }
  }
}