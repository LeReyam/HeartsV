package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class RankSpec extends AnyWordSpec with Matchers {

  "A Rank" should {
    "have thirteen values" in {
      Rank.values should have length 13
    }

    "include all card ranks from Two to Ace" in {
      Rank.values should contain(Rank.Two)
      Rank.values should contain(Rank.Three)
      Rank.values should contain(Rank.Four)
      Rank.values should contain(Rank.Five)
      Rank.values should contain(Rank.Six)
      Rank.values should contain(Rank.Seven)
      Rank.values should contain(Rank.Eight)
      Rank.values should contain(Rank.Nine)
      Rank.values should contain(Rank.Ten)
      Rank.values should contain(Rank.Jack)
      Rank.values should contain(Rank.Queen)
      Rank.values should contain(Rank.King)
      Rank.values should contain(Rank.Ace)
    }

    "have correct string representations" in {
      Rank.Two.toString should be("2")
      Rank.Three.toString should be("3")
      Rank.Four.toString should be("4")
      Rank.Five.toString should be("5")
      Rank.Six.toString should be("6")
      Rank.Seven.toString should be("7")
      Rank.Eight.toString should be("8")
      Rank.Nine.toString should be("9")
      Rank.Ten.toString should be("10")
      Rank.Jack.toString should be("J")
      Rank.Queen.toString should be("Q")
      Rank.King.toString should be("K")
      Rank.Ace.toString should be("A")
    }
  }
}