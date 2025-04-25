package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class gameSpec extends AnyWordSpec with Matchers {

  "A Trick" should {
  "print the correct winner" in {
    val player1 = new Player("Alice", List(Card(suit = Suit.Hearts, rank = Rank.Ten)))
    val player2 = new Player("Dave", List(Card(suit = Suit.Hearts, rank = Rank.Jack)))
    val players = List(player1, player2)

    // Capture println output
    val outCapture = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outCapture)) {
      Game.playRound(players)
    }

    val output = outCapture.toString

    output should include("Dave") // Dave should be printed as winner
    output should include("Stich gespielt") // German for 'Trick played'
  }
}
}