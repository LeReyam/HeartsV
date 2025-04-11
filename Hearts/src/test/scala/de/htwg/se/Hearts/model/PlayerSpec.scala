package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.must.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {

    "return name as toString" in {
      val player = Player("Alice")
      player.toString mustBe "Alice"
    }

    "have an empty hand by default" in {
      val player = Player("Bob")
      player.hand mustBe empty
    }

    "allow modifying the hand" in {
      val player = Player("Charlie")
      val card = Card(rank = "10", suit = Suit.Hearts)
      player.hand = List(card)
      player.hand mustBe List(card)
    }
  }
}
