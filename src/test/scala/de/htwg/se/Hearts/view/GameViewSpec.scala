package de.htwg.se.Hearts.view

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.controller.*

class GameViewSpec extends AnyWordSpec with Matchers {

    "A Frame should" should {

      "Display Playernames and hands in order" in {
        val alice = new Player("Alice", List(
          Card(Rank.Two, Suit.Hearts),
          Card(Rank.Ace, Suit.Spades),
          Card(Rank.Jack, Suit.Diamonds)
        ))
        val bob = new Player("Bob", List(
          Card(Rank.Ten, Suit.Clubs),
          Card(Rank.King, Suit.Diamonds),
          Card(Rank.Queen, Suit.Hearts)
        ))
        val game = new Game(List(alice, bob))
        val controller = new GameController(game)
        val gameView = new GameView(controller)

        val outputString = gameView.createGameFrame()

        outputString should include regex """(?s)Alice.*?2\s*\u2665.*?A\s*\u2660.*?J\s*\u2666.*?Bob.*?10\s*\u2663.*?K\s*\u2666.*?Q\s*\u2665"""
      }
    }
}