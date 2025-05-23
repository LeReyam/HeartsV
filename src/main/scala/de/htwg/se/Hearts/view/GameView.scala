package de.htwg.se.Hearts.view
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class GameView(controller: GameController) extends Observer {
  controller.addObserver(this)

  override def update(): Unit = {
    controller.getCurrentState() match
      case "GetPlayerNumberState" => println(generateOutputStringGetPlayerNumberState(controller))
      case "GetPlayerNamesState" => println(generateOutputStringGetPlayerNamesState(controller))
      case "GamePlayState" => println(generateOutputStringGamePlayState(controller))
      case "GameOverState" => println(generateStateStringGameOverState(controller))
      case "GetSortStrategyState" => println(generateOutputStringGetSortStrategyState(controller))

  }
  def generateOutputStringGetPlayerNumberState(controller: GameController): String = {
    val sb = new StringBuilder
    val separator = "=" * 80

    sb.append(separator).append("\n")
    sb.append("HEARTS GAME SETUP\n")
    sb.append(separator).append("\n\n")
    sb.append("Enter the number of players (2-4): ")

    sb.toString
  }

  def generateOutputStringGetPlayerNamesState(controller: GameController): String = {
  val separator = "=" * 80
  val header = "HEARTS GAME SETUP"
  val state = controller.getInternalPlayerNameStateInfo  // <- eigene neue Methode

  val prompt = state match {
    case Left(_) => "Wie viele menschliche Spieler?"
    case Right((index, _)) =>
      s"Gib den Namen für Spieler ${index + 1} ein:"
  }

  separator + "\n" +
  header + "\n" +
  separator + "\n\n" +
  prompt
}



  def generateOutputStringGamePlayState(controller: GameController): String = {
    val sb = new StringBuilder

    val separator = "=" * 150
    sb.append(separator).append("\n")
    sb.append(s"Current Player: ${controller.getCurrentPlayerName}\n")
    sb.append(separator).append("\n\n")

    val currentPlayerHand = controller.getSortedHand

    val headerBuilder = new StringBuilder("\t")
    headerBuilder.append("|Pts:")
    for (i <- 0 until currentPlayerHand.length) {
      headerBuilder.append(f"| $i%-2d ")
    }
    headerBuilder.append("|\n")

    sb.append(headerBuilder.toString())

    // Find the longest player name to determine fixed width
    val maxNameLength = controller.getAllPlayers.map(_.name.length).maxOption.getOrElse(0) + 3


    // Get all players and their indices
    val playersWithIndices = controller.getAllPlayers.zipWithIndex

    playersWithIndices.foreach { case (player, index) =>
      // Get sorted hand for each player using the new method
      val sortedHand = controller.getSortedHandForPlayer(index)

      // Format the sorted hand for display
      val handStr = sortedHand.map { card =>
        val cardStr = s"${card.rank.toString}${card.suit.toString}"
        f"$cardStr%-3s"
      }.mkString("| ")

      val currentMarker = if (player.name == controller.getCurrentPlayerName) " *" else "   "
      val nameWithMarker = s"${player.name}${currentMarker}"
      val points = f"| ${player.points}%-2d |"
      sb.append(s"${nameWithMarker.padTo(maxNameLength, ' ')}$points $handStr|\n")
    }
    sb.append("\n")

    sb.append("Current Pot:\n")
    val potStr = if (controller.getCurrentPot.isEmpty)
      "Empty"
    else controller.getCurrentPot.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      f"$cardStr%-3s"
    }.mkString(" | ")
    sb.append(s"| $potStr |\n")
    sb.append(separator).append(s"\n${controller.getCurrentPlayerName}, which card do you want to play? (Enter the index): \n")

    sb.toString
  }


  def generateStateStringGameOverState(controller: GameController): String = {
    val separator = "=" * 80
    val header = "GAME OVER"

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

    separator + "\n" +
    header + "\n" +
    separator + "\n\n" +
    "Final Scores:\n" +
    scoreSection + "\n\n" +
    "Play again? (y/n): "
  }


  def generateOutputStringGetSortStrategyState(controller: GameController): String = {
    val sb = new StringBuilder
    val separator = "=" * 80

    sb.append(separator).append("\n")
    sb.append("WÄHLE EINE SORTIERSTRATEGIE\n")
    sb.append(separator).append("\n")
    sb.append("1: Nach Farbe und Rang sortieren (Standard)\n")
    sb.append("2: Nur nach Rang sortieren\n")
    sb.append("3: Zufällige Reihenfolge\n")
    sb.append("Bitte gib die Zahl der gewünschten Strategie ein: ")

    sb.toString
  }

}


