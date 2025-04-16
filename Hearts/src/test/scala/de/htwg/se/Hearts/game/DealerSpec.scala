package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.*

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

  "A shuffle an deal" should{

    "Mix all the cards" in {
        val outcome1 = List(Card(Rank.Ace,Suit.Clubs))
        val outcome2 = List(Card(Rank.Eight,Suit.Spades))
        val listoutcome = List(outcome1,outcome2)
        val deck = List(Card(Rank.Ace,Suit.Clubs),Card(Rank.Eight,Suit.Spades))
        val playernames = List("Alice","Dave")
        val playstate = Dealer.shuffleAndDeal(deck,playernames)
        playstate(0).hand should(
          equal(outcome1) or
          equal(outcome2)
        )

    }
  }
}