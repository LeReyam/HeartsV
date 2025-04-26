package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._
import scala.collection.mutable.ListBuffer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayOutputStream, PrintStream}

class GameControllerSpec extends AnyWordSpec with Matchers {

  // Helper methods to DRY up the setup
  private def createPlayersWithCards(cardsForPlayer1: List[Card], cardsForPlayer2: List[Card]): (Player, Player) = {
    val player1 = Player("Alice", cardsForPlayer1)
    val player2 = Player("Bob", cardsForPlayer2)
    (player1, player2)
  }

  private def resetPlayerScores(player1: Player, player2: Player): Unit = {
    player1.points = 0
    player2.points = 0
  }

  private def captureOutput(action: => Unit): String = {
    val outputStream = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outputStream)) {
      action
    }
    outputStream.toString
  }

  "The GameController" should {
    "start a game and play all rounds" in {

      val (player1, player2) = createPlayersWithCards(
        List(Card(Rank.Ace, Suit.Hearts), Card(Rank.King, Suit.Diamonds)),
        List(Card(Rank.Queen, Suit.Clubs), Card(Rank.Jack, Suit.Spades))
      )

      val output = captureOutput(GameController.startGame(List(player1, player2)))

      output should include("Runde 1:")
      output should include("Runde 2:")
      output should include("Alice:")
      output should include("Bob:")

      player1.hand shouldBe empty
      player2.hand shouldBe empty
    }

    "correctly calculate the total number of rounds based on hand size" in {

      val (player1, player2) = createPlayersWithCards(
        List(Card(Rank.Ace, Suit.Hearts), Card(Rank.King, Suit.Diamonds), Card(Rank.Queen, Suit.Clubs)),
        List(Card(Rank.Jack, Suit.Spades), Card(Rank.Ten, Suit.Hearts), Card(Rank.Nine, Suit.Diamonds))
      )

      val output = captureOutput(GameController.startGame(List(player1, player2)))

      output should include("Runde 1:")
      output should include("Runde 2:")
      output should include("Runde 3:")
      output should not include("Runde 4:")
    }

    "update player scores after each round" in {

      val (player1, player2) = createPlayersWithCards(
        List(Card(Rank.Ace, Suit.Hearts), Card(Rank.King, Suit.Diamonds)),
        List(Card(Rank.Queen, Suit.Spades), Card(Rank.Jack, Suit.Clubs))
      )
      resetPlayerScores(player1, player2)

      val output = captureOutput(GameController.startGame(List(player1, player2)))

      (player1.points + player2.points) should be > 0
    }

    "directly update scores when a player wins a trick with heart cards" in {
      val (player1, player2) = createPlayersWithCards(List.empty, List.empty)
      resetPlayerScores(player1, player2)

      val trick = ListBuffer(
        (Card(Rank.Ace, Suit.Diamonds), player1),
        (Card(Rank.King, Suit.Hearts), player2),
        (Card(Rank.Queen, Suit.Hearts), player1)
      )

      val output = captureOutput(GameController.updateScores(List(player1, player2), trick))

      player1.points shouldBe 2
      player2.points shouldBe 0
      output should include("Alice erhält 2 Punkte für diesen Stich")
    }

    "directly update scores when a player wins a trick with the Queen of Spades" in {

      val (player1, player2) = createPlayersWithCards(List.empty, List.empty)
      resetPlayerScores(player1, player2)

      val trick = ListBuffer(
        (Card(Rank.Ten, Suit.Clubs), player1),
        (Card(Rank.Queen, Suit.Spades), player2),
        (Card(Rank.Jack, Suit.Clubs), player1)
      )

      val output = captureOutput(GameController.updateScores(List(player1, player2), trick))

      player1.points shouldBe 13
      player2.points shouldBe 0
      output should include("Alice erhält 13 Punkte für diesen Stich")
    }

    "not update scores when the trick is empty" in {

      val (player1, player2) = createPlayersWithCards(List.empty, List.empty)
      resetPlayerScores(player1, player2)

      val trick = ListBuffer.empty[(Card, Player)]

      GameController.updateScores(List(player1, player2), trick)

      player1.points shouldBe 0
      player2.points shouldBe 0
    }
  }
}