package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._
import scala.collection.mutable.ListBuffer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayOutputStream, PrintStream}

class GameControllerSpec extends AnyWordSpec with Matchers {

  "The GameController" should {
    "start a game and play all rounds" in {
      // Arrange
      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        // Create test players with predefined cards
        val player1 = Player("Alice", List(
          Card(Rank.Ace, Suit.Hearts),
          Card(Rank.King, Suit.Diamonds)
        ))
        val player2 = Player("Bob", List(
          Card(Rank.Queen, Suit.Clubs),
          Card(Rank.Jack, Suit.Spades)
        ))
        val players = List(player1, player2)

        // Act
        GameController.startGame(players)

        // Assert
        val output = outputStream.toString
        output should include("Runde 1:")
        output should include("Runde 2:")
        output should include("Alice:")
        output should include("Bob:")

        // Verify that all cards have been played
        player1.hand shouldBe empty
        player2.hand shouldBe empty
      }
    }

    "correctly calculate the total number of rounds based on hand size" in {
      // Arrange
      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        // Create test players with different hand sizes
        val player1 = Player("Alice", List(
          Card(Rank.Ace, Suit.Hearts),
          Card(Rank.King, Suit.Diamonds),
          Card(Rank.Queen, Suit.Clubs)
        ))
        val player2 = Player("Bob", List(
          Card(Rank.Jack, Suit.Spades),
          Card(Rank.Ten, Suit.Hearts),
          Card(Rank.Nine, Suit.Diamonds)
        ))
        val players = List(player1, player2)

        // Act
        GameController.startGame(players)

        // Assert
        val output = outputStream.toString
        output should include("Runde 1:")
        output should include("Runde 2:")
        output should include("Runde 3:")
        output should not include("Runde 4:")
      }
    }

    "update player scores after each round" in {
      // Arrange
      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        // Create test players with cards that will score points
        val player1 = Player("Alice", List(
          Card(Rank.Ace, Suit.Hearts),  // Hearts card (1 point)
          Card(Rank.King, Suit.Diamonds)
        ))
        val player2 = Player("Bob", List(
          Card(Rank.Queen, Suit.Spades), // Spades Queen (13 points)
          Card(Rank.Jack, Suit.Clubs)
        ))
        val players = List(player1, player2)

        // Reset points before test
        player1.points = 0
        player2.points = 0

        // Act
        GameController.startGame(players)

        // Assert
        // Points should have been updated during the game
        // The exact point values will depend on the game logic implementation
        // We just verify that points were assigned
        (player1.points + player2.points) should be > 0
      }
    }
  }
}

