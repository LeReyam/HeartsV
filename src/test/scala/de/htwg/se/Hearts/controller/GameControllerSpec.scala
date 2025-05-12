package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*


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

    "allow a player to play a card" in {
      controller.playCard(0) should be(true)
      controller.getCurrentPlayerName should be("Spieler 2") // Nächster Spieler
      controller.getCurrentPot.size should be(1)
    }

    "test if index of card is valid" in {
      controller.parseCardIndex("0") should be (0)
    }

    "handle invalid card index" in {
      controller.parseCardIndex("3") should be (-1)
    }

    "handle non-numeric input" in {
      controller.parseCardIndex("a") should be (-1)
    }

    "run a complete game" in {

    }
  }
}