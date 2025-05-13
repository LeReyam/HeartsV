package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.io.StdIn

class GameController(game: Game) extends Observable {
  private var currentPot: ListBuffer[Card] = ListBuffer()
  private var currentPlayerIndex: Int = 0
  private var gameOver: Boolean = false

  def getPlayerCount: Int = game.players.length

  def getCurrentPlayerName: String = game.players(currentPlayerIndex).name

  def getCurrentPlayerHand: List[Card] = game.players(currentPlayerIndex).hand

  def getAllPlayers: List[Player] = game.players

  def getCurrentPot: ListBuffer[Card] = currentPot

  def gameIsOver: Boolean = gameOver

  def getPlayerPoints(playerIndex: Int): Int = game.players(playerIndex).points

  def playCard(index: Int): Boolean = {
    val currentPlayer = game.players(currentPlayerIndex)

    if (index >= 0 && index < currentPlayer.hand.length) {
      val selectedCard = currentPlayer.hand(index)
      currentPlayer.removeCard(selectedCard)
      currentPot += selectedCard
      currentPlayerIndex = (currentPlayerIndex + 1) % game.players.length
      if (game.players.forall(_.hand.isEmpty)) {
        gameOver = true
      }
      true
    } else {
      false
    }
  }


  def runGame(): Unit = {
    var playing = true

    while (playing && !gameIsOver) {
      notifyObservers()
      val cardIndex = getCardIndexFromPlayer()
      if (cardIndex >= 0) {
        playCard(cardIndex)
      }
      score()
      if (gameIsOver) {
        playing = false
      }
    }
    notifyObservers()
  }

  protected def getCardIndexFromPlayer(): Int = {
    val input = StdIn.readLine()
    parseCardIndex(input)
  }



  def parseCardIndex(input: String): Int = {
    val handSize = getCurrentPlayerHand.length
    try {
      val index = input.toInt
      if (index >= 0 && index < handSize) {
        index
      } else {
        -1
      }
    } catch {
      case _: NumberFormatException =>
        -1
    }
  }

  def score() :Boolean =
    if (getPlayerCount == getCurrentPot.length) {
      val firstCard = currentPot.head
      val firstSuit = firstCard.suit
      val highestCard = currentPot
        .filter(_.suit == firstSuit)
        .maxBy(card => card.rank)
      val winnerIndex = currentPot.indexOf(highestCard)
      var trickPoints = 0
      for (card <- currentPot) {
        if (card.suit == Suit.Hearts) {
          trickPoints += 1
        }
        else if (card.suit == Suit.Spades && card.rank == Rank.Queen) {
          trickPoints += 13
        }
      }
      val winner = game.players(winnerIndex)
      winner.points += trickPoints
      currentPot.clear()
      currentPlayerIndex = winnerIndex
      true
    }else{
      false
    }
}