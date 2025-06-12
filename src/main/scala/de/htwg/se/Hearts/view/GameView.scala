package de.htwg.se.Hearts.view
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.util.{Try, Success, Failure}

class GameView(controller: GameController) extends Observer {
  controller.addObserver(this)
  val separator = "=" * 80 + "\n"
  val header = "HEARTS GAME SETUP\n"
  override def update(): Unit = {
    val state = controller.getCurrentState()
    if (state.startsWith("GetPlayerNumberState")) {
      println(generateOutputStringGetPlayerNumberState(controller))
    } else if (state.startsWith("GetHumanPlayerCountState")) {
      println(generateOutputStringGetHumanPlayerCountState(controller))
    } else if (state.startsWith("GetPlayerNamesState")) {
      println(generateOutputStringGetPlayerNamesState(controller))
    } else if (state.startsWith("GamePlayState")) {
      println(generateOutputStringGamePlayState(controller))
    } else if (state.startsWith("GameOverState")) {
      println(generateStateStringGameOverState(controller))
    } else if (state.startsWith("GetSortStrategyState")) {
      println(generateOutputStringGetSortStrategyState(controller))
    }
  }

  def generateOutputStringGetPlayerNumberState(controller: GameController): String = {
    var output = ""
    output += separator
    output += header

    controller.getLastPlayerCountTry match {
      case Failure(_: IndexOutOfBoundsException) =>
        output += "Error: Spieleranzahl außerhalb des gültigen Bereichs\n"
      case Failure(_: NumberFormatException) =>
        output += "Please enter a Number\n"
      case Failure(e) =>
        output += s"Error: ${e.getMessage}\n"
      case Success(_) =>
    }

    output += separator + "\n"
    output += "Enter the number of players (2-4): "
    output
  }

  def generateOutputStringGetHumanPlayerCountState(controller: GameController): String = {
    var output = ""
    output += separator
    output += header

    controller.getLastHumanCountTry match {
      case Failure(_: IndexOutOfBoundsException) =>
        output += "Error: Ungültige Spieleranzahl\n"
      case Failure(_: NumberFormatException) =>
        output += "Please enter a Number\n"
      case Failure(e) =>
        output += s"Error: ${e.getMessage}\n"
      case Success(_) =>
    }

    output += separator + "\n"
    output += "Wie viele menschliche Spieler? "
    output
  }

  def generateOutputStringGetPlayerNamesState(controller: GameController): String = {
    val state = controller.getInternalPlayerNameStateInfo

    val prompt = state match {
      case Left(_) => "Error: Invalid state"
      case Right((index, _)) =>
        s"Gib den Namen für Spieler ${index + 1} ein:"
    }

    separator +
    header +
    separator + "\n" +
    prompt
  }

  def generateOutputStringGamePlayState(controller: GameController): String = {
  val separator = "=" * 150 + "\n"

  var output = ""
  output += separator
  output += s"Current Player: ${controller.getCurrentPlayerName}\n"
  output += separator + "\n\n"

  val currentPlayerHand = controller.getSortedHand

  val header =
    "\t|Pts:" +
      currentPlayerHand.indices.map(i => f"| $i%-2d ").mkString("") +
      "|\n"

  output += header

  val maxNameLength = controller.getAllPlayers.map(_.name.length).maxOption.getOrElse(0) + 3
  val playersWithIndices = controller.getAllPlayers.zipWithIndex

  playersWithIndices.foreach { case (player, index) =>
    val sortedHand = controller.getSortedHandForPlayer(index)

    val handStr = sortedHand.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      f"$cardStr%-3s"
    }.mkString("| ")

    val currentMarker = if (player.name == controller.getCurrentPlayerName) " *" else "   "
    val nameWithMarker = s"${player.name}$currentMarker"
    val points = f"| ${player.points}%-2d |"
    output += s"${nameWithMarker.padTo(maxNameLength, ' ')}$points $handStr|\n"
  }

  output += "\n"

  output += "Current Pot:\n"
  val potStr = if (controller.getCurrentPot.isEmpty)
    "Empty"
  else controller.getCurrentPot.map { card =>
    val cardStr = s"${card.rank.toString}${card.suit.toString}"
    f"$cardStr%-3s"
  }.mkString(" | ")
  output += s"| $potStr |\n"

  controller.getLastCardIndexTry match {
    case Failure(e: NumberFormatException) =>
      output += "\nError: Please enter a valid number.\n"
    case Failure(e: IndexOutOfBoundsException) =>
      output += s"\nError: ${e.getMessage} Please enter a number between 0 and ${currentPlayerHand.length -1}\n"
    case Failure(e) =>
      output += s"\nError: ${e.getMessage}\n"
    case Success(_) =>
  }

  output += separator
  output += s"${controller.getCurrentPlayerName}, which card do you want to play? (Enter the index): \n"

  output
}


  def generateStateStringGameOverState(controller: GameController): String = {
    val header = "GAME OVER\n"

    val allPlayers = controller.getAllPlayers
    val scoreSection =
      if (allPlayers.isEmpty) {
        "Keine Spieler vorhanden."
      } else {
        val sortedPlayers = allPlayers.sortBy(_.points)
        sortedPlayers.zipWithIndex.map { case (player, index) =>
          s"${index + 1}. ${player.name}: ${player.points} points"
        }.mkString("\n")
      }

    separator +
    header +
    separator + "\n" +
    "Final Scores:\n" +
    scoreSection + "\n\n" +
    "Play again? (y/n): "
  }

  def generateOutputStringGetSortStrategyState(controller: GameController): String = {
    separator +
    "WÄHLE EINE SORTIERSTRATEGIE\n" +
    separator +
    "1: Nach Farbe und Rang sortieren (Standard)\n" +
    "2: Nur nach Rang sortieren\n" +
    "3: Zufällige Reihenfolge\n" +
    "Bitte gib die Zahl der gewünschten Strategie ein: "
  }
}


