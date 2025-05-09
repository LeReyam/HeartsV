package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.util.MockUserInterface

class GameControllerSpec extends AnyWordSpec with Matchers {

  "A GameController" should {
    val mockUI = new MockUserInterface()
    val player1 = new Player("Spieler 1", List(
      Card(Rank.Ace, Suit.Hearts),
      Card(Rank.King, Suit.Hearts)
    ))
    val player2 = new Player("Spieler 2", List(
      Card(Rank.Queen, Suit.Diamonds),
      Card(Rank.Jack, Suit.Clubs)
    ))
    val game = new Game(List(player1, player2))
    val controller = new GameController(game, mockUI)

    "return the current player's name" in {
      controller.getCurrentPlayerName should be("Spieler 1")
    }

    "return the current player's hand" in {
      controller.getCurrentPlayerHand should contain(Card(Rank.Ace, Suit.Hearts))
      controller.getCurrentPlayerHand should contain(Card(Rank.King, Suit.Hearts))
    }

    "allow a player to play a card" in {
      controller.playCard(0) should be(true)
      controller.getCurrentPlayerName should be("Spieler 2") // Nächster Spieler
      controller.getCurrentPot.size should be(1)
    }

    "get card index from player" in {
      mockUI.addInput("0") // Simuliere Benutzereingabe "0"
      val index = controller.getCardIndexFromPlayer()
      index should be(0)
      mockUI.getOutput should include("Spieler 2, welche Karte möchtest du spielen?")
    }

    "handle invalid card index" in {
      mockUI.clearOutput()
      mockUI.addInput("5") // Ungültiger Index
      val index = controller.getCardIndexFromPlayer()
      index should be(-1)
      mockUI.getOutput should include("Ungültiger Index")
    }

    "handle non-numeric input" in {
      mockUI.clearOutput()
      mockUI.addInput("abc") // Keine Zahl
      val index = controller.getCardIndexFromPlayer()
      index should be(-1)
      mockUI.getOutput should include("Bitte eine Zahl eingeben")
    }

    "run a complete game" in {
      // Bereite den Mock für einen kompletten Spielablauf vor
      mockUI.clearOutput()
      mockUI.addInput("0") // Spieler 2 spielt erste Karte
      mockUI.addInput("0") // Spieler 1 spielt letzte Karte

      controller.runGame()

      // Überprüfe, ob das Spiel beendet wurde
      controller.gameIsOver should be(true)
      mockUI.getOutput should include("Spieler 2, welche Karte möchtest du spielen?")
      mockUI.getOutput should include("Spieler 1, welche Karte möchtest du spielen?")
    }
  }
}