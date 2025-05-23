package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameSpec extends AnyWordSpec with Matchers {

  "A Game" should {
    val alice = new HumanPlayer("Alice", List(
      Card(Rank.Two, Suit.Hearts),
      Card(Rank.Ace, Suit.Spades)
    ))
    val bob = BotPlayer("Bot_1", List(
      Card(Rank.Ten, Suit.Clubs),
      Card(Rank.King, Suit.Diamonds)
    ))

    "store a list of players" in {
      val game = new Game(List(alice, bob))
      game.players should have length 2
      game.players should contain(alice)
      game.players should contain(bob)
    }

    "allow access to individual players" in {
      val game = new Game(List(alice, bob))
      game.players.head should be(alice)
      game.players(1) should be(bob)
    }
  }
}
