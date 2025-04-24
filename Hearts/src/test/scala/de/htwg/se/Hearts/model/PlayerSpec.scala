package de.htwg.se.Hearts.model

import de.htwg.se.Hearts.game.*
import de.htwg.se.Hearts.model.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {
    "return name and hand as String" in {
      val player = new Player("Alice", List(Card(suit = Suit.Hearts,rank = Rank.Ten)))
      player.showHandString() should be ("Alice's hand: 10\u2665\n")
    }
    "update hand after playing a Card" in {
      val player = new Player("Alice", List(Card(suit = Suit.Hearts,rank = Rank.Ten),Card(suit = Suit.Hearts,rank = Rank.Five)))
      player.playCard(Card(Rank.Five,Suit.Hearts))
      player.hand should contain theSameElementsAs (List(Card(suit = Suit.Hearts,rank = Rank.Ten)))
    }
  }
}
