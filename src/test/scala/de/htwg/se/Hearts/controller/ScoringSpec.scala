package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ScoringSpec extends AnyWordSpec with Matchers {

  "The Scoring object" should {
    "update scores for players based on the trick" in {
      val alice = new Player("Alice", List(
        Card(Rank.Two, Suit.Hearts),
        Card(Rank.Ace, Suit.Spades)
      ))
      val bob = new Player("Bob", List(
        Card(Rank.Ten, Suit.Clubs),
        Card(Rank.King, Suit.Diamonds)
      ))

      val trick = List(
        Card(Rank.Queen, Suit.Hearts),
        Card(Rank.Jack, Suit.Hearts)
      )

      // Currently, the updateScores method is a dummy implementation
      // This test just verifies it can be called without errors
      noException should be thrownBy Scoring.updateScores(List(alice, bob), trick)

      // In the future, when the method is implemented, we should add assertions
      // to verify that scores are updated correctly
    }

    "handle empty tricks" in {
      val players = List(
        new Player("Alice", List(Card(Rank.Two, Suit.Hearts))),
        new Player("Bob", List(Card(Rank.Ten, Suit.Clubs)))
      )

      val emptyTrick = List.empty[Card]

      // Verify that the method handles empty tricks without errors
      noException should be thrownBy Scoring.updateScores(players, emptyTrick)
    }

    "handle empty player list" in {
      val emptyPlayers = List.empty[Player]
      val trick = List(Card(Rank.Queen, Suit.Hearts))

      // Verify that the method handles empty player list without errors
      noException should be thrownBy Scoring.updateScores(emptyPlayers, trick)
    }
  }
}