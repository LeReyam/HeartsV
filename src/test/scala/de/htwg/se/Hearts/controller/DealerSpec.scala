package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.controller.Dealer

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class DealerSpec extends AnyWordSpec with Matchers {

  "The Creation of a deck" should {

    "generate a complete Deck" in {
      val deck = Dealer.createDeck()
      deck should have length 52
    }

    "have specific cards in it" in {
      val deck = Dealer.createDeck()
      deck should contain(Card(rank = Rank.Queen, suit = Suit.Hearts))
      deck should contain(Card(rank = Rank.Ace, suit = Suit.Spades))
    }
  }

  "A shuffle" should {
    "Mix all the cards" in {
      val deck = List(Card(Rank.Ace, Suit.Clubs), Card(Rank.Eight, Suit.Spades))
      val shuffledDeck = Dealer.shuffle(deck)
      shuffledDeck should contain theSameElementsAs deck
    }
  }

  "A deal with player names" should {
    "create a game with cards for players" in {
      val deck = Dealer.createDeck()
      val playerNames = List("Alice", "Bob", "Charlie", "Diana")

      val game = Dealer.deal(deck, playerNames)
      game.players should have length playerNames.length
      game.players.map(_.name) should contain theSameElementsAs playerNames
      val expectedCardsPerPlayer = deck.length / playerNames.length
      game.players.foreach(player => player.hand.length should be(expectedCardsPerPlayer))
      val allCards = game.players.flatMap(_.hand)
      allCards should contain theSameElementsAs deck
    }
  }
}