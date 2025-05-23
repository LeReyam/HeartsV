package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayersSpec extends AnyWordSpec with Matchers {

  "A Players object" should {
    val alice = new HumanPlayer("Alice", List(
      Card(Rank.Two, Suit.Hearts),
      Card(Rank.Ace, Suit.Spades)
    ))
    val bob = BotPlayer("Bot_1", List(
      Card(Rank.Ten, Suit.Clubs),
      Card(Rank.King, Suit.Diamonds)
    ))
    val playerList = List(alice, bob)

    "allow access to individual players" in {
      val players = Players(playerList)
      players.players.head should be(alice)
      players.players(1) should be(bob)
    }
  }
}
