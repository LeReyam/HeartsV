package de.htwg.se.Hearts.controller

import scala.util.Try
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.io.StdIn


trait GameState {
  def handleInput(input: String, controller: GameController): GameState
  def generateStateString(controller: GameController): String
}


class GetPlayerNumberState extends GameState {
  override def handleInput(input: String, controller: GameController): GameState = {
    Try(input.toInt).toOption.filter(n => n >= 2 && n <= 4) match {
      case Some(validPlayerCount) => new GetPlayerNamesState(validPlayerCount)
      case None => this
    }
  }

  override def generateStateString(controller: GameController): String = {
    "GetPlayerNumberState"
  }
}

// State for collecting player names
// State for collecting player names

class GetPlayerNamesState(playerCount: Int) extends GameState {
  private var humanCountOpt: Option[Int] = None
  private var playerNames: List[String] = List()
  private var currentPlayerIndex: Int = 0

    override def handleInput(input: String, controller: GameController): GameState = {
    humanCountOpt match {
      case None =>
        Try(input.toInt).toOption.filter(n => n >= 0 && n <= playerCount) match {
          case Some(validHumanCount) =>
            humanCountOpt = Some(validHumanCount)
          case None => return this
        }
        return this

      case Some(humanCount) =>
        if (currentPlayerIndex < humanCount) {
          playerNames = playerNames :+ input.trim
          currentPlayerIndex += 1
        }

        // Bot-Auffüllung danach:
        if (currentPlayerIndex >= humanCount && currentPlayerIndex < playerCount) {
          while (currentPlayerIndex < playerCount) {
            playerNames = playerNames :+ s"Bot_${currentPlayerIndex - humanCount + 1}"
            currentPlayerIndex += 1
          }
        }

        if (currentPlayerIndex >= playerCount) {
          val deck = Dealer.shuffle(Dealer.createDeck())
          val game = Dealer.deal(deck, playerNames)
          controller.initializeGame(game)
          new GetSortStrategyState()
        } else {
          this
        }
    }
  }

  def getInternalState: Either[Unit, (Int, Int)] = {
    humanCountOpt match {
      case None => Left(())
      case Some(humanCount) => Right((currentPlayerIndex, humanCount))
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
