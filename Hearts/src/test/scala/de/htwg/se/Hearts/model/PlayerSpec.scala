package de.htwg.se.Hearts.model

import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {
    "update hand after playing a Card" in {
      val player = new Player("Alice", List(Card(suit = Suit.Hearts,rank = Rank.Ten),Card(suit = Suit.Hearts,rank = Rank.Five)))
      player.playCard(Card(Rank.Five,Suit.Hearts))
      player.hand should contain theSameElementsAs (List(Card(suit = Suit.Hearts,rank = Rank.Ten)))
    }
  }
}
