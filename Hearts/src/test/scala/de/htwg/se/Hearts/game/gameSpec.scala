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
    val player3 = new Player("Steve", List(Card(suit = Suit.Clubs, rank = Rank.Two)))
    val playersHeartsfirst = List(player1, player2, player3)

    // Capture println output
    val outCapture = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outCapture)) {
      Game.playRound(playersHeartsfirst)
    }
    val output = outCapture.toString
    output should include("Dave") // Dave should be printed as winner
    output should include("Stich gespielt") //'Trick played'
  }
  "print the correct Winner based on Suit" in{
    val player1 = new Player("Alice", List(Card(suit = Suit.Hearts, rank = Rank.Ten)))
    val player2 = new Player("Dave", List(Card(suit = Suit.Hearts, rank = Rank.Jack)))
    val player3 = new Player("Steve", List(Card(suit = Suit.Clubs, rank = Rank.Two)))
    val playersHeartsfirst = List(player3, player2, player1)

    // Capture println output
    val outCapture = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outCapture)) {
      Game.playRound(playersHeartsfirst)
    }
    val output = outCapture.toString
    output should include("Steve") // Dave should be printed as winner
    output should include("Stich gespielt") //'Trick played'
  }
}
}