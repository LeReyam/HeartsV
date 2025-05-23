package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerFactorySpec extends AnyWordSpec with Matchers {

  "PlayerFactory" should {
    "create a HumanPlayer when kind is 'human'" in {
      val cardList = List(Card(Rank.Ten, Suit.Hearts))
      val player = PlayerFactory.createPlayer("human", "Alice", cardList)
      player shouldBe a [HumanPlayer]
      player.name should be ("Alice")
      player.hand should be (cardList)
    }

    "create a BotPlayer when kind is 'bot'" in {
      val cardList = List(Card(Rank.Five, Suit.Clubs))
      val player = PlayerFactory.createPlayer("bot", "Bot_1", cardList)
      player shouldBe a [BotPlayer]
      player.name should be ("Bot_1")
      player.hand should be (cardList)
    }

    "throw an exception for unknown kind" in {
      val cardList = List(Card(Rank.Ace, Suit.Spades))
      an [IllegalArgumentException] should be thrownBy {
        PlayerFactory.createPlayer("alien", "Unknown", cardList)
      }
    }
  }
}
