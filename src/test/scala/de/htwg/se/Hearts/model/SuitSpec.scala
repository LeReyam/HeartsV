package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class SuitSpec extends AnyWordSpec with Matchers {

  "A Suit" should {
    "have four values" in {
      Suit.values should have length 4
    }

    "include Hearts, Diamonds, Clubs, and Spades" in {
      Suit.values should contain(Suit.Hearts)
      Suit.values should contain(Suit.Diamonds)
      Suit.values should contain(Suit.Clubs)
      Suit.values should contain(Suit.Spades)
    }

    "have correct string representations" in {
      Suit.Hearts.toString should be("♥")
      Suit.Diamonds.toString should be("♦")
      Suit.Clubs.toString should be("♣")
      Suit.Spades.toString should be("♠")
    }
  }
}