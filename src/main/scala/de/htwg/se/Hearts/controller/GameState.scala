package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.io.StdIn


trait GameState {
  def handleInput(input: String, controller: GameController): GameState
  def generateStateString(controller: GameController): String
}


class GetPlayerNumberState extends GameState {
  override def handleInput(input: String, controller: GameController): GameState = {
    try {
      val playerCount = input.toInt
      if (playerCount >= 2 && playerCount <= 4) {
        // Valid player count, transition to collecting player names
        new GetPlayerNamesState(playerCount)
      } else {
        // Invalid player count, stay in this state
        this
      }
    } catch {
      case _: NumberFormatException => this // Invalid input, stay in this state
    }
  }

  override def generateStateString(controller: GameController): String = {
    "GetPlayerNumberState"
  }
}

// State for collecting player names
// State for collecting player names
class GetPlayerNamesState(playerCount: Int) extends GameState {
  private var playerNames: List[String] = List()
  private var currentPlayerIndex: Int = 0

  override def handleInput(input: String, controller: GameController): GameState = {
    // Add the new player name
    playerNames = playerNames :+ input
    currentPlayerIndex += 1

    if (currentPlayerIndex >= playerCount) {
      // All player names collected, create the game and move to gameplay state
      val game = Dealer.deal(Dealer.shuffle(Dealer.createDeck()), playerNames)
      controller.initializeGame(game)
      new GetSortStrategyState()

    } else {
      // Continue collecting names
      this
    }
  }


  override def generateStateString(controller: GameController): String = {
    "GetPlayerNamesState"
  }
}

// State for the main gameplay
class GamePlayState extends GameState {
  override def handleInput(input: String, controller: GameController): GameState = {
    val cardIndex = controller.parseCardIndex(input)

    if (cardIndex >= 0) {
      controller.playCard(cardIndex)
      controller.score()

      if (controller.gameIsOver) {
        new GameOverState()
      } else {
        this
      }
    } else {
      // Invalid input, stay in this state
      this
    }
  }

  override def generateStateString(controller: GameController): String = {
    "GamePlayState"
  }
}
class GetSortStrategyState extends GameState {
  override def handleInput(input: String, controller: GameController): GameState = {
    input.trim match {
      case "1" =>
        controller.setSortStrategy(new SortBySuitThenRank())
        new GamePlayState()
      case "2" =>
        controller.setSortStrategy(new SortByRankOnly())
        new GamePlayState()
      case "3" =>
        controller.setSortStrategy(new RandomSort())
        new GamePlayState()
      case _ =>
        // Ungültige Eingabe, wiederhole Auswahl
        this
    }
  }

  override def generateStateString(controller: GameController): String = {
    "GetSortStrategyState"
  }

}

// State for game over
class GameOverState extends GameState {
  override def handleInput(input: String, controller: GameController): GameState = {
    // Any input in game over state starts a new game
    if (input.trim.toLowerCase == "y") {
      new GetPlayerNumberState()
    } else {
      // Exit the game or stay in this state
      this
    }
  }

  override def generateStateString(controller: GameController): String = {
    "GameOverState"
  }
}
