package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ControllerSpec extends AnyWordSpec with Matchers {

  "A GameController" should {
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

    "return the current player's name" in {
      controller.getCurrentPlayerName should be("Alice")
    }

    "return the current player's hand" in {
      controller.getCurrentPlayerHand should contain theSameElementsAs List(
        Card(Rank.Two, Suit.Hearts),
        Card(Rank.Ace, Suit.Spades),
        Card(Rank.Jack, Suit.Diamonds)
      )
    }

    "return all players" in {
      controller.getAllPlayers should have length 2
      controller.getAllPlayers.head.name should be("Alice")
      controller.getAllPlayers(1).name should be("Bob")
    }

    "return the current pot" in {
      controller.getCurrentPot should be(empty)
    }

    "initially not be over" in {
      controller.gameIsOver should be(false)
    }

    "allow playing a card with valid index" in {
      controller.playCard(0) should be(true)
      controller.getCurrentPot should have size 1
      controller.getCurrentPot.head should be(Card(Rank.Two, Suit.Hearts))

      // After playing a card, it should be Bob's turn
      controller.getCurrentPlayerName should be("Bob")
    }

    "not allow playing a card with invalid index" in {
      controller.playCard(10) should be(false)
    }

    "detect when the game is over" in {
      // Create a new controller with players having fewer cards for this test
      val player1 = new Player("Player1", List(Card(Rank.Two, Suit.Hearts)))
      val player2 = new Player("Player2", List(Card(Rank.Three, Suit.Hearts)))
      val testGame = new Game(List(player1, player2))
      val testController = new GameController(testGame)

      // Play all remaining cards
      testController.playCard(0) // Player1 plays their card
      testController.playCard(0) // Player2 plays their card

      // At this point, both players should have no cards left
      testController.gameIsOver should be(true)
    }
  }
}