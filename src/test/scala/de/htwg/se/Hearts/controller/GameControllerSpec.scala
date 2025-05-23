package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class GameControllerSpec extends AnyWordSpec with Matchers {

  "A GameController" should {
    val player1 = new HumanPlayer("Spieler 1", List(
      Card(Rank.Ace, Suit.Hearts),
      Card(Rank.King, Suit.Hearts)
    ))
    val player2 = BotPlayer("Bot_1", List(
      Card(Rank.Queen, Suit.Diamonds),
      Card(Rank.Jack, Suit.Clubs)
    ))
    val game = new Game(List(player1, player2))
    val controller = new GameController()
    controller.initializeGame(game)

    "return the current player's name" in {
      controller.getCurrentPlayerName should be("Spieler 1")
    }

    "return the number of players" in {
      controller.getPlayerCount should be (2)
    }

    "return the current player's hand" in {
      controller.getCurrentPlayerHand should contain(Card(Rank.Ace, Suit.Hearts))
      controller.getCurrentPlayerHand should contain(Card(Rank.King, Suit.Hearts))
    }

    "return all players" in {
      controller.getAllPlayers should be(List(player1, player2))
      controller.getAllPlayers.size should be(2)
    }

    "return the current pot" in {
      controller.getCurrentPot shouldBe a[ListBuffer[?]]
      controller.getCurrentPot.size should be(0)
    }

    "initially not be in game over state" in {
      controller.gameIsOver should be(false)
    }

    "allow a player to play a card" in {
      controller.playCard(0) should be(true)
      controller.getCurrentPlayerName should be("Bot_1")
      controller.getCurrentPot.size should be(1)
    }

    "reject invalid card indices" in {
      controller.playCard(-1) should be(false)
      controller.playCard(10) should be(false)
    }

    "test if index of card is valid" in {
      controller.parseCardIndex("0") should be(0)
    }

    "handle invalid card index" in {
      controller.parseCardIndex("3") should be(-1)
    }

    "handle non-numeric input" in {
      controller.parseCardIndex("a") should be(-1)
    }

    "handle empty input" in {
      controller.parseCardIndex("") should be(-1)
    }

    "correctly detect game over when all players have no cards" in {
      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts)))
      val newGame = new Game(List(p1, p2))
      val newController = new GameController()
      newController.initializeGame(newGame)

      newController.gameIsOver should be(false)

      newController.playCard(0) should be(true)
      newController.gameIsOver should be(false)

      newController.playCard(0) should be(true)
      newController.gameIsOver should be(true)
    }

    "notify observers when game state changes" in {
      var notified = false
      val observer = new Observer {
        override def update(): Unit = {
          notified = true
        }
      }

      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts)))
      val testGame = new Game(List(p1, p2))
      val testController = new GameController()
      testController.initializeGame(testGame)

      testController.addObserver(observer)

      testController.notifyObservers()
      notified should be(true)

      testController.getCurrentPlayerName should be("P1")
      testController.playCard(0) should be(true)
      testController.getCurrentPlayerName should be("P2")
      testController.playCard(0) should be(true)
      testController.gameIsOver should be(true)
    }

    "handle invalid human player input and stay in GetPlayerNamesState" in {
      val controller = new GameController()
      controller.handleInput("3")         // Gesamtspielerzahl
      controller.handleInput("abc")       // Ungültiger Input für menschliche Spieler

      controller.getCurrentState() should include("GetPlayerNamesState")
    }

    "getInternalPlayerNameStateInfo returns Left when not in GetPlayerNamesState" in {
      val controller = new GameController()
      controller.getInternalPlayerNameStateInfo should be (Left(()))
    }

    "correctly change States" in {
      val controller = new GameController()
      controller.getCurrentState() should include ("GetPlayerNumberState")
      controller.handleInput("2")
      controller.handleInput("1")
      controller.getCurrentState() should include("GetPlayerNamesState")
      controller.handleInput("Player1")
      controller.handleInput("Player2")
      controller.getCurrentState() should be ("GetSortStrategyState")
      controller.handleInput("1")
      controller.getCurrentState() should include("GamePlayState")
      controller.getPlayerCount should be(2)
      controller.handleInput("redo")
      controller.handleInput("undo")
      controller.handleInput("0")
      controller.handleInput("undo")
      controller.handleInput("redo")
      for(i <- 0 to 51){
        controller.handleInput("0")
      }
      controller.getCurrentState() should be ("GameOverState")
      controller.handleInput("y")
      controller.getCurrentState() should be ("GetPlayerNumberState")
    }

    "give correct returns even if the game is uninitialized" in {
      val controller = new GameController
      controller.getPlayerCount should be (0)
      controller.getCurrentPlayerName should be ("")
      controller.getCurrentPlayerHand should be (List())
      controller.getAllPlayers should be (List())
      controller.getPlayerPoints(0) should be (0)
      controller.getSortedHandForPlayer(0) should be (List())
    }

    "run a complete game with predefined inputs and sorting strat 2" in {
      val controller = new GameController()
      controller.getCurrentState() should include ("GetPlayerNumberState")
      controller.handleInput("2")
      controller.handleInput("2")
      controller.getCurrentState() should include("GetPlayerNamesState")
      controller.handleInput("Player1")
      controller.handleInput("Player2")
      controller.getCurrentState() should be ("GetSortStrategyState")
      controller.handleInput("2")
      controller.getCurrentState() should include("GamePlayState")
      controller.getPlayerCount should be(2)
      for(i <- 0 to 52){
        controller.handleInput("0")
      }
      controller.getCurrentState() should be ("GameOverState")
      controller.handleInput("y")
      controller.getCurrentState() should be ("GetPlayerNumberState")
    }

    "stay in GameOverState when input is not 'y'" in {
      val controller = new GameController()
      val state = new GameOverState()
      val nextState = state.handleInput("n", controller)
      nextState shouldBe a [GameOverState]
    }

    "stay in GetSortStrategyState when input is invalid" in {
      val controller = new GameController()
      val state = new GetSortStrategyState()
      val nextState = state.handleInput("invalid_input", controller)
      nextState shouldBe a [GetSortStrategyState]
    }

    "run a complete game with predefined inputs and sorting strat 3" in {
      val controller = new GameController()
      controller.getCurrentState() should include ("GetPlayerNumberState")
      controller.handleInput("2")
      controller.handleInput("2")
      controller.getCurrentState() should include("GetPlayerNamesState")
      controller.handleInput("Player1")
      controller.handleInput("Player2")
      controller.getCurrentState() should be ("GetSortStrategyState")
      controller.handleInput("3")
      controller.getCurrentState() should include("GamePlayState")
      controller.getPlayerCount should be(2)
      for(i <- 0 to 52){
        controller.handleInput("0")
      }
      controller.getCurrentState() should be ("GameOverState")
      controller.handleInput("y")
      controller.getCurrentState() should be ("GetPlayerNumberState")

    }

    "add human player and increment currentPlayerIndex in GetPlayerNamesState" in {
      val controller = new GameController()
      controller.handleInput("2")      // total players
      controller.handleInput("1")      // 1 human
      controller.handleInput("Alice")  // triggers: currentPlayerIndex += 1

      controller.getAllPlayers.exists(_.name == "Alice") should be(true)
    }

    "skip manual name input if human count is zero" in {
      val controller = new GameController()
      controller.handleInput("2")      // total players
      controller.handleInput("0")      // 0 human players → skips manual input entirely

      controller.getAllPlayers.forall(_.name.startsWith("Bot_")) should be(true)
    }


    "stay in GetPlayerNamesState on out-of-range number of human players" in {
      val controller = new GameController()
      controller.handleInput("4")          // Spieleranzahl 4
      controller.handleInput("5")          // ungültige Anzahl menschlicher Spieler (> 4)
      controller.getCurrentState() should include("GetPlayerNamesState")
    }

    "run a complete game with predefined inputs and sorting strat 1" in {
      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts)))
      val game = new Game(List(p1, p2))
      val inputs = List("a","6","2","2", "P1", "P2","1", "-1", "0")
      var inputIndex = 0
      val testController = new GameController() {
        override protected def GetUserInput(): String = {
          if (inputIndex < inputs.length) {
            val input = inputs(inputIndex)
            inputIndex += 1
            input
          } else {
            "0" // Default fallback if we run out of inputs
          }
        }
      }
      testController.initializeGame(game)

      var updates = 0
      testController.addObserver(new Observer {
        override def update(): Unit = updates += 1
      })

      testController.runGame()


      testController.gameIsOver should be(true)
      updates should be (61)
    }

    "update scores for players based on the trick" in {
      val p1 = new HumanPlayer("P1", List(Card(Rank.Ace, Suit.Hearts),Card (Rank.Two, Suit.Clubs)))
      val p2 = new HumanPlayer("P2", List(Card(Rank.King, Suit.Hearts),Card(Rank.Queen,Suit.Spades)))
      val game = new Game(List(p1, p2))
      val testController = new GameController()
      testController.initializeGame(game)

      // First player plays a card
      testController.playCard(0) should be(true)
      // Pot should have one card
      testController.getCurrentPot.size should be(1)
      // No scoring yet
      testController.getPlayerPoints(0) should be (0)
      testController.getPlayerPoints(1) should be (0)

      // Second player plays a card, which should trigger scoring
      testController.playCard(0) should be(true)
      // Pot should be empty after scoring
      testController.getCurrentPot shouldBe empty
      // Player 1 should have 2 points (Ace of Hearts)
      testController.getPlayerPoints(0) should be (2)
      testController.getPlayerPoints(1) should be (0)

      // First player plays another card
      testController.playCard(0) should be(true)
      // Pot should have one card
      testController.getCurrentPot.size should be(1)

      // Second player plays another card, which should trigger scoring
      testController.playCard(0) should be(true)
      // Pot should be empty after scoring
      testController.getCurrentPot shouldBe empty
      // Player 1 should now have 15 points (2 + 13 for Queen of Spades)
      testController.getPlayerPoints(0) should be (15)
      testController.getPlayerPoints(1) should be (0)
    }

    "support undo and redo of card plays" in {
      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Two, Suit.Clubs)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts), Card(Rank.Queen, Suit.Spades)))
      val game = new Game(List(p1, p2))
      val testController = new GameController()
      testController.initializeGame(game)

      // Initial state
      testController.getCurrentPlayerName should be("P1")
      testController.getCurrentPlayerHand.size should be(2)
      testController.getCurrentPot.size should be(0)

      // Play a card
      testController.playCard(0) should be(true)
      testController.getCurrentPlayerName should be("P2")
      testController.getCurrentPot.size should be(1)
      testController.getCurrentPot.head should be(Card(Rank.Ace, Suit.Hearts))
      p1.hand.size should be(1)

      // Undo the play
      testController.undoLastCard() should be(true)
      testController.getCurrentPlayerName should be("P1")
      testController.getCurrentPot.size should be(0)
      p1.hand.size should be(2)
      p1.hand should contain(Card(Rank.Ace, Suit.Hearts))

      // Redo the play
      testController.redoLastCard() should be(true)
      testController.getCurrentPlayerName should be("P2")
      testController.getCurrentPot.size should be(1)
      testController.getCurrentPot.head should be(Card(Rank.Ace, Suit.Hearts))
      p1.hand.size should be(1)

      // Play another card to complete the trick
      testController.playCard(0) should be(true)
      testController.getCurrentPot.size should be(0) // Pot is cleared after scoring
      testController.getPlayerPoints(0) should be(2) // P1 gets 2 points for Ace of Hearts

      // Undo the second play (which includes undoing the scoring)
      testController.undoLastCard() should be(true)
      testController.getCurrentPlayerName should be("P2")
      testController.getCurrentPot.size should be(1)
      testController.getCurrentPot.head should be(Card(Rank.Ace, Suit.Hearts))
      testController.getPlayerPoints(0) should be(0) // Points should be back to 0
      p2.hand.size should be(2)
      p2.hand should contain(Card(Rank.King, Suit.Hearts))

      // Redo the second play (which includes redoing the scoring)
      testController.redoLastCard() should be(true)
      testController.getCurrentPot.size should be(0) // Pot is cleared after scoring
      testController.getPlayerPoints(0) should be(2) // P1 gets 2 points for Ace of Hearts
      p2.hand.size should be(1)
    }
  }
}