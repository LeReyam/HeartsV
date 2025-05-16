package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class GameControllerSpec extends AnyWordSpec with Matchers {

  "A GameController" should {
    val player1 = new Player("Spieler 1", List(
      Card(Rank.Ace, Suit.Hearts),
      Card(Rank.King, Suit.Hearts)
    ))
    val player2 = new Player("Spieler 2", List(
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
      controller.getCurrentPlayerName should be("Spieler 2")
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
      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts)))
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

      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts)))
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


    "correctly change States" in {
      val controller = new GameController()
      controller.getCurrentState() should include ("GetPlayerNumberState")
      controller.handleInput("2")
      controller.getCurrentState() should include("GetPlayerNamesState")
      controller.handleInput("Player1")
      controller.handleInput("Player2")
      controller.getCurrentState() should be ("GetSortStrategyState")
      controller.handleInput("1")
      controller.getCurrentState() should include("GamePlayState")
      controller.getPlayerCount should be(2)
      for(i <- 0 to 52){
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
    }




    "run a complete game with predefined inputs" in {
      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts)))
      val game = new Game(List(p1, p2))
      val inputs = List("a","6","2", "P1", "P2","1", "-1", "0")
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

      // Mock the runGame method to avoid infinite loop
      // Call the mock instead of the actual runGame
      testController.runGame()

      // After playing all cards, the game should be over
      testController.gameIsOver should be(true)
      updates should be (60)
    }

    "update scores for players based on the trick" in {
      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts),Card (Rank.Two, Suit.Clubs)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts),Card(Rank.Queen,Suit.Spades)))
      val game = new Game(List(p1, p2))
      val testController = new GameController()
      testController.initializeGame(game)

      testController.playCard(0) should be(true)
      testController.score() should be (false)
      testController.playCard(0) should be(true)
      testController.score() should be (true)
      testController.getCurrentPot shouldBe empty
      testController.getPlayerPoints(0) should be (2)
      testController.getPlayerPoints(1) should be (0)

      testController.playCard(0) should be(true)
      testController.score() should be (false)
      testController.playCard(0) should be(true)
      testController.score() should be (true)
      testController.getCurrentPot shouldBe empty
      testController.getPlayerPoints(0) should be (15)
      testController.getPlayerPoints(1) should be (0)
    }
  }
}