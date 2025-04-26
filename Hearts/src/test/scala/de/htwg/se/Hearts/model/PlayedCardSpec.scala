package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayedCardSpec extends AnyWordSpec with Matchers {

  "A PlayedCard" should {
    "contain a player and a card" in {
      val player = Player("Alice", List.empty[Card])
      val card = Card(Rank.Queen, Suit.Hearts)
      val playedCard = new PlayedCard(player, card)

      playedCard.getPlayer shouldEqual player
      playedCard.getCard shouldEqual card
    }
    "have a correct string representation" in {
      val player = Player("Alice", List.empty[Card])
      val card = Card(Rank.Queen, Suit.Hearts)
      val playedCard = new PlayedCard(player, card)

      playedCard.toString shouldEqual "Alice: Q♥"
    }
  }
}
