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
    // Convert input to Option[Int], then validate the range
    scala.util.Try(input.toInt).toOption
      .filter(count => count >= 2 && count <= 4)
      .map(count => new GetPlayerNamesState(count))
      .getOrElse(this)
  }

  override def generateStateString(controller: GameController): String = {
    "GetPlayerNumberState"
  }
}

class GetPlayerNamesState(playerCount: Int) extends GameState {
  private var playerNames: List[String] = List()
  private var currentPlayerIndex: Int = 0

  override def handleInput(input: String, controller: GameController): GameState = {
    playerNames = playerNames :+ input
    currentPlayerIndex += 1
    if (currentPlayerIndex >= playerCount) {
      val game = Dealer.deal(Dealer.shuffle(Dealer.createDeck()), playerNames)
      controller.initializeGame(game)
      new GetSortStrategyState()

    } else {
      this
    }
  }


  override def generateStateString(controller: GameController): String = {
    "GetPlayerNamesState"
  }
}

class GamePlayState extends GameState {
  override def handleInput(input: String, controller: GameController): GameState = {
    input.trim.toLowerCase match {
      case "undo" =>
        if (controller.undoLastCard()) {
          this
        } else {
          this
        }
      case "redo" =>
        if (controller.redoLastCard()) {
          this
        } else {
          this
        }
      case _ =>
        val cardIndex = controller.parseCardIndex(input)

        if (cardIndex >= 0) {
          controller.playCard(cardIndex)
          if (controller.gameIsOver) {
            new GameOverState()
          } else {
            this
          }
        } else {
          this
        }
    }
  }

  override def generateStateString(controller: GameController): String = {
    val stateString = new StringBuilder("GamePlayState\n")
    stateString.append("Type 'undo' to take back your last move\n")
    stateString.append("Type 'redo' to redo an undone move\n")
    stateString.toString
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
