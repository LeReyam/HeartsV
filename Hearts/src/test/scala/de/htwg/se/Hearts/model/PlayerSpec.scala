package de.htwg.se.Hearts.model

import de.htwg.se.Hearts.game.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {
    "return name and hand as String" in {
      val player = new Player("Alice", List(Card(suit = Suit.Hearts,rank = Rank.Ten)))
      player.toString should be ("Alice's hand:\n  - 10\u2665")
    }
  }
}
