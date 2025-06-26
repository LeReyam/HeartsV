package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.Card
import scala.collection.mutable.ListBuffer

trait Command {
  def execute(): Boolean
  def undo(): Boolean
  def redo(): Boolean
}

class PlayCardCommand(
    controller: GameController,
    cardIndex: Int,
    private var previousPlayerIndex: Int = -1,
    private var playedCard: Option[Card] = None,
    private var winnerIndex: Int = -1,
    private var trickPoints: Int = 0,
    private var potCards: ListBuffer[Card] = ListBuffer(),
    private var shouldScore: Boolean = false,
    private var previousPotState: ListBuffer[Card] = ListBuffer(), // Store previous pot state
    private var potWasCleared: Boolean = false // Track if pot was cleared during execute
) extends Command {

  override def execute(): Boolean = {
    val players = controller.getAllPlayers
    if (players.isEmpty) return false

    val currentPlayer = players(controller.getCurrentPlayerIndex)

    // Store the current pot state before any modifications
    previousPotState = controller.getCurrentPot.clone()

    // Check if we need to clear the pot (when a new trick starts)
    val potSize = controller.getCurrentPot.size
    val playerCount = if (players.nonEmpty) players.length else 0

    // Clear the pot if a complete trick has been played
    if (potSize > 0 && potSize % playerCount == 0) {
      controller.getCurrentPot.clear()
      potWasCleared = true
    } else {
      potWasCleared = false
    }

    if (cardIndex >= 0 && cardIndex < controller.getSortedHand.length) {
      previousPlayerIndex = controller.getCurrentPlayerIndex
      val sortedHand = controller.getSortedHand
      val selectedCard = sortedHand(cardIndex)
      playedCard = Some(selectedCard)
      currentPlayer.removeCard(selectedCard)
      controller.addCardToPot(selectedCard)
      controller.advanceToNextPlayer()
      shouldScore = controller.getPlayerCount == controller.getCurrentPot.length

      if (shouldScore) {
        potCards = controller.getCurrentPot.clone()
        val firstCard = controller.getCurrentPot.head
        val firstSuit = firstCard.suit
        val highestCard = controller.getCurrentPot
          .filter(_.suit == firstSuit)
          .maxBy(card => card.rank)

        val playerCount = controller.getAllPlayers.length
        val firstPlayerIndex = previousPlayerIndex

        val highCardPosition = controller.getCurrentPot.indexOf(highestCard)
        winnerIndex = (firstPlayerIndex + highCardPosition + 1) % playerCount

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
        controller.setCurrentPlayerIndex(winnerIndex)
      }

      true
    } else {
      false
    }
  }

  override def undo(): Boolean = {
    if (previousPlayerIndex >= 0 && playedCard.isDefined) {
      // Restore scoring if it happened
      if (shouldScore && winnerIndex >= 0 && trickPoints > 0) {
        val winner = controller.getAllPlayers(winnerIndex)
        winner.points -= trickPoints
      }

      // Restore the previous pot state
      controller.getCurrentPot.clear()
      controller.getCurrentPot.addAll(previousPotState)

      // Restore player state
      controller.setCurrentPlayerIndex(previousPlayerIndex)
      val players = controller.getAllPlayers
      val currentPlayer = players(previousPlayerIndex)
      currentPlayer.addCard(playedCard.get)

      true
    } else {
      false
    }
  }

  override def redo(): Boolean = {
    if (playedCard.isDefined) {
      // Check if we need to clear the pot (when a new trick starts)
      val potSize = controller.getCurrentPot.size
      val playerCount = controller.getAllPlayers.length

      // Clear the pot if a complete trick has been played
      if (potSize > 0 && potSize % playerCount == 0) {
        controller.getCurrentPot.clear()
      }

      val players = controller.getAllPlayers
      val player = players(previousPlayerIndex)
      player.removeCard(playedCard.get)
      controller.addCardToPot(playedCard.get)
      controller.advanceToNextPlayer()

      if (shouldScore && winnerIndex >= 0) {
        val winner = controller.getAllPlayers(winnerIndex)
        winner.points += trickPoints
        controller.setCurrentPlayerIndex(winnerIndex)
      }

      true
    } else {
      false
    }
  }
}