package de.htwg.se.Hearts.model

import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {
    "have a name and hand" in {
      val player = new Player("Alice", List(
        Card(Rank.Ten, Suit.Hearts),
        Card(Rank.Five, Suit.Hearts)
      ))

      player.name should be("Alice")
      player.hand should have length 2
      player.hand should contain(Card(Rank.Ten, Suit.Hearts))
      player.hand should contain(Card(Rank.Five, Suit.Hearts))
    }

    "update hand after playing a Card" in {
      val player = new Player("Alice", List(
        Card(Rank.Ten, Suit.Hearts),
        Card(Rank.Five, Suit.Hearts)
      ))

      player.removeCard(Card(Rank.Five, Suit.Hearts))
      player.hand should have length 1
      player.hand should contain only Card(Rank.Ten, Suit.Hearts)
    }

    "handle playing a card that is not in hand" in {
      val player = new Player("Alice", List(
        Card(Rank.Ten, Suit.Hearts),
        Card(Rank.Five, Suit.Hearts)
      ))

      // Playing a card not in hand should not change the hand
      player.removeCard(Card(Rank.Ace, Suit.Spades))
      player.hand should have length 2
      player.hand should contain(Card(Rank.Ten, Suit.Hearts))
      player.hand should contain(Card(Rank.Five, Suit.Hearts))
    }

    "handle empty hand" in {
      val player = new Player("Alice", List())
      player.hand should be(empty)

      // Playing a card with empty hand should not cause errors
      noException should be thrownBy player.removeCard(Card(Rank.Ace, Suit.Spades))
    }

    "have a score associated" in {
      val player = new Player("Alice", List())
      player.points should be (0)
    }
  }
}
