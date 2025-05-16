package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class SortStrategySpec extends AnyWordSpec with Matchers {

  val heart3 = Card(Rank.Three, Suit.Hearts)
  val spadeQ = Card(Rank.Queen, Suit.Spades)
  val clubA  = Card(Rank.Ace, Suit.Clubs)

  val hand = List(spadeQ, heart3, clubA)

  "SortByRankOnly" should {
    "sort cards by rank ascending" in {
      val sorted = new SortByRankOnly().sort(hand)
      sorted shouldBe List(heart3, spadeQ, clubA)
    }
  }

  "SortBySuitThenRank" should {
    "group by suit then rank" in {
      val sorted = new SortBySuitThenRank().sort(hand)
      assert(sorted.map(_.suit) == sorted.map(_.suit).sorted)
    }
  }

  "RandomSort" should {
    "shuffle the cards" in {
      val hand2 = List(Card(Rank.Ace, Suit.Hearts),Card(Rank.Two, Suit.Hearts),Card(Rank.Three, Suit.Hearts),Card(Rank.Four, Suit.Hearts),Card(Rank.Five, Suit.Hearts),Card(Rank.Six, Suit.Hearts),Card(Rank.Seven, Suit.Hearts),Card(Rank.Eight, Suit.Hearts),Card(Rank.Nine, Suit.Hearts),Card(Rank.Ten, Suit.Hearts))
      val shuffled = new RandomSort().sort(hand2)
      shuffled should not equal hand2 // kann theoretisch auch mal gleich sein
    }
  }
}
