package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.Card
import scala.collection.mutable.ListBuffer

trait Command {
  def execute(): Boolean
  def undo(): Boolean
}

class PlayCardCommand(
    controller: GameController,
    cardIndex: Int,
    private var previousPlayerIndex: Int = -1,
    private var playedCard: Option[Card] = None
) extends Command {

  override def execute(): Boolean = {
    val players = controller.getAllPlayers
    if (players.isEmpty) return false

    val currentPlayer = players(controller.getCurrentPlayerIndex)

    if (cardIndex >= 0 && cardIndex < controller.getSortedHand.length) {
      // Save state for undo
      previousPlayerIndex = controller.getCurrentPlayerIndex

      // Get the sorted hand and use the index on it to find the correct card
      val sortedHand = controller.getSortedHand
      val selectedCard = sortedHand(cardIndex)
      playedCard = Some(selectedCard)

      // Execute the card play
      currentPlayer.removeCard(selectedCard)
      controller.addCardToPot(selectedCard)
      controller.advanceToNextPlayer()

      true
    } else {
      false
    }
  }

  override def undo(): Boolean = {
    if (previousPlayerIndex >= 0 && playedCard.isDefined) {
      // Revert to previous player
      controller.setCurrentPlayerIndex(previousPlayerIndex)

      // Remove the card from the pot
      controller.removeCardFromPot(playedCard.get)

      // Add the card back to the player's hand
      val players = controller.getAllPlayers
      if (players.isEmpty || previousPlayerIndex >= players.length) return false

      val currentPlayer = players(previousPlayerIndex)
      currentPlayer.addCard(playedCard.get)

      true
    } else {
      false
    }
  }
}

class ScoreCommand(
    controller: GameController,
    private var winnerIndex: Int = -1,
    private var trickPoints: Int = 0,
    private var potCards: ListBuffer[Card] = ListBuffer(),
    private var previousPlayerIndex: Int = -1
) extends Command {

  override def execute(): Boolean = {
    // Save the current state for undo
    previousPlayerIndex = controller.getCurrentPlayerIndex

    // Save the current pot for undo
    potCards = controller.getCurrentPot.clone()

    // Score the pot
    if (controller.getPlayerCount == controller.getCurrentPot.length) {
      val firstCard = controller.getCurrentPot.head
      val firstSuit = firstCard.suit
      val highestCard = controller.getCurrentPot
        .filter(_.suit == firstSuit)
        .maxBy(card => card.rank)

      val potSize = controller.getCurrentPot.length
      val playerCount = controller.getAllPlayers.length
      val firstPlayerIndex = controller.getCurrentPlayerIndex

      val highCardPosition = controller.getCurrentPot.indexOf(highestCard)
      winnerIndex = (firstPlayerIndex + highCardPosition) % playerCount

      trickPoints = 0
      for (card <- controller.getCurrentPot) {
        if (card.suit == de.htwg.se.Hearts.model.Suit.Hearts) {
          trickPoints += 1
        }
        else if (card.suit == de.htwg.se.Hearts.model.Suit.Spades && card.rank == de.htwg.se.Hearts.model.Rank.Queen) {
          trickPoints += 13
        }
      }

      val winner = controller.getAllPlayers(winnerIndex)
      winner.points += trickPoints
      controller.getCurrentPot.clear()
      controller.setCurrentPlayerIndex(winnerIndex)
      true
    } else {
      false
    }
  }

  override def undo(): Boolean = {
    if (winnerIndex >= 0 && trickPoints > 0) {
      // Restore the pot
      controller.getCurrentPot.clear()
      potCards.foreach(card => controller.addCardToPot(card))

      // Revert points
      val winner = controller.getAllPlayers(winnerIndex)
      winner.points -= trickPoints

      // Restore previous player
      controller.setCurrentPlayerIndex(previousPlayerIndex)

      true
    } else {
      false
    }
  }
}