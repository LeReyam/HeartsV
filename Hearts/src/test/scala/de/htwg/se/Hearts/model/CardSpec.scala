package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CardSpec extends AnyWordSpec with Matchers {

  "A Card" should {

    "have a rank and a suit" in {
      val card = Card("Q", Suit.Spades)
      card.rank should be ("Q")
      card.suit should be (Suit.Spades)
    }

    "have a rank and a different suit" in {
      val card = Card("Q", Suit.Hearts)
      card.rank should be ("Q")
      card.suit should be (Suit.Hearts)
    }

    "show the correct string with suit symbol" in {
      val card = Card("10", Suit.Hearts)
      card.toString should be ("10\u2665") // or "10♥"
    }

    "display correct symbols for each suit" in {
      Suit.Hearts.symbol should be ("\u2665")    // ♥
      Suit.Diamonds.symbol should be ("\u2666")  // ♦
      Suit.Spades.symbol should be ("\u2660")    // ♠
      Suit.Clubs.symbol should be ("\u2663")     // ♣
    }

  }
}