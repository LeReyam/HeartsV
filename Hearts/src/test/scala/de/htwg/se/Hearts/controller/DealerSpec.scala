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

      // Since shuffle uses Random, we can only verify the shuffled deck contains the same cards
      shuffledDeck should contain theSameElementsAs deck

      // Note: The original test was deterministic but shuffle is random,
      // so we can't guarantee a specific outcome
    }
  }

  "A deal" should {
    "split all cards between players" in {
      val deck = Dealer.createDeck()
      val playerNames = List("Alice", "Bob", "Charlie", "Diana")
      val players = Dealer.deal(deck, playerNames)

      // Verify we have the correct number of players
      players should have length playerNames.length

      // Verify player names are correct
      players.map(_.name) should contain theSameElementsAs playerNames

      // Verify each player has the correct number of cards
      val expectedCardsPerPlayer = deck.length / playerNames.length
      players.foreach(player => player.hand.length should be(expectedCardsPerPlayer))

      // Verify all cards from the deck are distributed
      val allCards = players.flatMap(_.hand)
      allCards should contain theSameElementsAs deck
    }
  }
}