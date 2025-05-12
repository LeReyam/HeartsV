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
    val controller = new GameController(game)

    "return the current player's name" in {
      controller.getCurrentPlayerName should be("Spieler 1")
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
      controller.getCurrentPot shouldBe a[ListBuffer[_]]
      controller.getCurrentPot.size should be(0)
    }

    "initially not be in game over state" in {
      controller.gameIsOver should be(false)
    }

    "allow a player to play a card" in {
      controller.playCard(0) should be(true)
      controller.getCurrentPlayerName should be("Spieler 2") // Nächster Spieler
      controller.getCurrentPot.size should be(1)
    }

    "reject invalid card indices" in {
      // Negative index
      controller.playCard(-1) should be(false)

      // Index out of bounds
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
      // Neues Spiel mit Spielern, die nur eine Karte haben
      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts)))
      val newGame = new Game(List(p1, p2))
      val newController = new GameController(newGame)

      newController.gameIsOver should be(false)

      // Erste Karte spielen
      newController.playCard(0) should be(true)
      newController.gameIsOver should be(false)

      // Zweite Karte spielen, danach sollten alle Spieler keine Karten mehr haben
      newController.playCard(0) should be(true)
      newController.gameIsOver should be(true)
    }

    "notify observers when game state changes" in {
      // Erstellen eines Test-Observers
      var notified = false
      val observer = new Observer {
        override def update(): Unit = {
          notified = true
        }
      }

      val p1 = new Player("P1", List(Card(Rank.Ace, Suit.Hearts)))
      val p2 = new Player("P2", List(Card(Rank.King, Suit.Hearts)))
      val testGame = new Game(List(p1, p2))
      val testController = new GameController(testGame)

      // Observer registrieren
      testController.addObserver(observer)

      // Methode aufrufen, die notifyObservers() verwendet
      testController.playCard(0)

      // Überprüfen, ob der Observer benachrichtigt wurde
      notified should be(true)
    }

    "run a complete game" in {

    }

    
  }
}