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

    "reset the game" in {
      // First, play some cards to change the state
      controller.playCard(0) // Bob plays a card
      controller.getCurrentPot should have size 2

      // Reset the game
      controller.resetGame()

      // Check that the state is reset
      controller.getCurrentPot should be(empty)
      controller.getCurrentPlayerName should be("Alice")
      controller.gameIsOver should be(false)
    }

    "detect when the game is over" in {
      // Play all cards to end the game
      controller.playCard(0) // Alice plays first card
      controller.playCard(0) // Bob plays first card
      controller.playCard(0) // Alice plays second card
      controller.playCard(0) // Bob plays second card

      // At this point, both players should have no cards left
      controller.gameIsOver should be(true)
    }
  }
}