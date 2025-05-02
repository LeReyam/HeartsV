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
      val outcome1 = List(Card(Rank.Ace,Suit.Clubs),Card(Rank.Eight,Suit.Spades))
      val outcome2 = List(Card(Rank.Eight,Suit.Spades),Card(Rank.Ace,Suit.Clubs))
      val deck = List(Card(Rank.Ace,Suit.Clubs),Card(Rank.Eight,Suit.Spades))
      val shuffledDeck = Dealer.shuffle(deck)
      shuffledDeck should(
        equal(outcome1) or
        equal(outcome2)
      )
    }
  }

  "A deal" should {
    "split all cards between players" in {
      val deck = Dealer.createDeck()
      val playerNames = List("Alice", "Bob", "Charlie", "Diana")
      val players = Dealer.deal(deck,playerNames)
      players(0).hand should contain theSameElementsAs deck.take(deck.length / playerNames.length)
    }
  }
}