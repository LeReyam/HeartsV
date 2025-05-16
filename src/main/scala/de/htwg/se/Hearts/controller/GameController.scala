package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.io.StdIn
import scala.compiletime.uninitialized

class GameController extends Observable {
  private var game: Game = uninitialized
  private var currentPot: ListBuffer[Card] = ListBuffer()
  private var currentPlayerIndex: Int = 0
  private var gameOver: Boolean = false
  private var currentState: GameState = new GetPlayerNumberState()
  private var sortStrategy: SortStrategy = new SortBySuitThenRank()

  def setSortStrategy(strategy: SortStrategy): Unit = {
    sortStrategy = strategy
  }

  def getSortedHand: List[Card] = {
    sortStrategy.sort(getCurrentPlayerHand)
  }

  def getSortedHandForPlayer(playerIndex: Int): List[Card] = {
    if (game != null && playerIndex >= 0 && playerIndex < game.players.length) {
      sortStrategy.sort(game.players(playerIndex).hand)
    } else {
      List()
    }
  }

  def initializeGame(newGame: Game): Unit = {
    game = newGame
    currentPot = ListBuffer()
    currentPlayerIndex = 0
    gameOver = false
  }

  def getPlayerCount: Int = if (game != null) game.players.length else 0

  def getCurrentPlayerName: String = if (game != null) game.players(currentPlayerIndex).name else ""

  def getCurrentPlayerHand: List[Card] = if (game != null) game.players(currentPlayerIndex).hand else List()

  def getAllPlayers: List[Player] = if (game != null) game.players else List()

  def getCurrentPot: ListBuffer[Card] = currentPot

  def gameIsOver: Boolean = gameOver

  def getPlayerPoints(playerIndex: Int): Int = if (game != null) game.players(playerIndex).points else 0

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
    var running = true

    notifyObservers()

    while (running) {
      val input = GetUserInput()

      val nextState = currentState.handleInput(input, this)

      if (nextState != currentState) {
        currentState = nextState
      }

      if (currentState.isInstanceOf[GameOverState] && input.trim.toLowerCase != "y") {
        running = false
      }

      notifyObservers()
    }
  }

  protected def GetUserInput(): String = {
    StdIn.readLine()
  }

  def getCurrentState(): String = {
    currentState.generateStateString(this)
  }

  def handleInput(input: String): Unit = {
    currentState = currentState.handleInput(input, this)
    notifyObservers()
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

  def score(): Boolean = {
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
    } else {
      false
    }
  }
}