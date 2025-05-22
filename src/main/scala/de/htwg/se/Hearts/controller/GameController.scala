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
  private var commandHistory: ListBuffer[Command] = ListBuffer()
  private var redoStack: ListBuffer[Command] = ListBuffer()

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

  def getCurrentPlayerIndex: Int = currentPlayerIndex

  def getCurrentPlayerName: String = if (game != null) game.players(currentPlayerIndex).name else ""

  def getCurrentPlayerHand: List[Card] = if (game != null) game.players(currentPlayerIndex).hand else List()

  def getAllPlayers: List[Player] = if (game != null) game.players else List()

  def getCurrentPot: ListBuffer[Card] = currentPot

  def addCardToPot(card: Card): Unit = {
    currentPot += card
  }

  def removeCardFromPot(card: Card): Unit = {
    currentPot -= card
  }

  def gameIsOver: Boolean = gameOver

  def setCurrentPlayerIndex(index: Int): Unit = {
    if (game != null && index >= 0 && index < game.players.length) {
      currentPlayerIndex = index
    }
  }

  def advanceToNextPlayer(): Unit = {
    currentPlayerIndex = (currentPlayerIndex + 1) % game.players.length
    if (game.players.forall(_.hand.isEmpty)) {
      gameOver = true
    }
  }

  def getPlayerPoints(playerIndex: Int): Int = if (game != null) game.players(playerIndex).points else 0

  def playCard(index: Int): Boolean = {
    val command = new PlayCardCommand(this, index)
    val result = command.execute()

    if (result) {
      commandHistory += command
      redoStack.clear()  // Clear redo stack when a new command is executed
    }

    result
  }

  def undoLastCard(): Boolean = {
    if (commandHistory.nonEmpty) {
      val lastCommand = commandHistory.last
      val result = lastCommand.undo()

      if (result) {
        commandHistory.remove(commandHistory.size - 1)
        redoStack.prepend(lastCommand)  // Add to redo stack

        // If we just undid a ScoreCommand, automatically undo the PlayCardCommand that led to it
        if (lastCommand.isInstanceOf[ScoreCommand] && commandHistory.nonEmpty &&
            commandHistory.last.isInstanceOf[PlayCardCommand]) {
          val playCardCommand = commandHistory.last
          val playCardResult = playCardCommand.undo()

          if (playCardResult) {
            commandHistory.remove(commandHistory.size - 1)
            redoStack.prepend(playCardCommand)  // Add to redo stack
          }
        }
      }

      result
    } else {
      false
    }
  }

  def redoLastCard(): Boolean = {
    if (redoStack.nonEmpty) {
      val commandToRedo = redoStack.head
      val result = commandToRedo.execute()

      if (result) {
        redoStack.remove(0)  // Remove from redo stack
        commandHistory += commandToRedo  // Add back to command history

        // If we just redid a PlayCardCommand and there's a ScoreCommand next in the redo stack,
        // automatically redo the ScoreCommand as well
        if (commandToRedo.isInstanceOf[PlayCardCommand] && redoStack.nonEmpty &&
            redoStack.head.isInstanceOf[ScoreCommand]) {
          val scoreCommand = redoStack.head
          val scoreResult = scoreCommand.execute()

          if (scoreResult) {
            redoStack.remove(0)  // Remove from redo stack
            commandHistory += scoreCommand  // Add back to command history
          }
        }
      }

      result
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
    val sortedHand = getSortedHand
    val handSize = sortedHand.length
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
    val command = new ScoreCommand(this)
    val result = command.execute()

    if (result) {
      commandHistory += command
    }

    result
  }
}