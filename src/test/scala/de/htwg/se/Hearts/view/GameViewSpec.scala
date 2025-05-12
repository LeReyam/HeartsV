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



      "Display the current Pot if there are cards in it" in {
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
        controller.playCard(0)
        controller.playCard(0)

        val outputString = gameView.createGameFrame()
        outputString should include regex """(?s)Aktueller Pot:.*?2\s*\u2665.*?10\s*\u2663"""
      }




      "Display the empty pot correctly" in {
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

        outputString should include regex "Leer".r
      }

      "Have consistent line lengths for player hand display" in {
        val alice = new Player("Alice", List(
          Card(Rank.Two, Suit.Hearts),
          Card(Rank.Ace, Suit.Spades),
          Card(Rank.Jack, Suit.Diamonds),
          Card(Rank.Ten, Suit.Clubs)
        ))
        val bob = new Player("Bob", List(
          Card(Rank.Ace, Suit.Clubs),
          Card(Rank.King, Suit.Diamonds),
          Card(Rank.Queen, Suit.Hearts),
          Card(Rank.Nine, Suit.Spades)
        ))
        val game = new Game(List(alice, bob))
        val controller = new GameController(game)
        val gameView = new GameView(controller)

        val outputString = gameView.createGameFrame()

        val lines = outputString.split("\n")
        val playerHandLines = lines.filter(line =>
          line.contains("Alice") || line.contains("Bob")
        )
        playerHandLines(1).length should be (playerHandLines(2).length)
      }
    }
}