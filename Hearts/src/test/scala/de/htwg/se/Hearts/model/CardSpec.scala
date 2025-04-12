package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CardSpec extends AnyWordSpec with Matchers {

  "A Card" should {

    "have a rank and a suit" in {
      val card = Card(Rank.Queen, Suit.Spades)
      card.rank should be (Rank.Queen)
      card.suit should be (Suit.Spades)
    }

    "show the correct string with suit symbol" in {
      val cardJ = Card(Rank.Jack, Suit.Hearts)
      cardJ.toString should be ("J\u2665")
      val cardQ = Card(Rank.Queen, Suit.Hearts)
      cardQ.toString should be ("Q\u2665")
      val cardK = Card(Rank.King, Suit.Hearts)
      cardK.toString should be ("K\u2665")
      val card10 = Card(Rank.Ten, Suit.Hearts)
      card10.toString should be ("10\u2665")
      val card9 = Card(Rank.Nine, Suit.Hearts)
      card9.toString should be ("9\u2665")
      val card8 = Card(Rank.Eight, Suit.Hearts)
      card8.toString should be ("8\u2665")
      val card7 = Card(Rank.Seven, Suit.Hearts)
      card7.toString should be ("7\u2665")
      val card6 = Card(Rank.Six, Suit.Hearts)
      card6.toString should be ("6\u2665")
      val card5 = Card(Rank.Five, Suit.Hearts)
      card5.toString should be ("5\u2665")
      val card4 = Card(Rank.Four, Suit.Hearts)
      card4.toString should be ("4\u2665")
      val card3 = Card(Rank.Three, Suit.Spades)
      card3.toString should be ("3\u2660")
      val card2 = Card(Rank.Two, Suit.Clubs)
      card2.toString should be ("2\u2663")
      val cardA = Card(Rank.Ace, Suit.Diamonds)
      cardA.toString should be ("A\u2666")
    }
  }
}