package de.htwg.se.Hearts.view
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class GameView(controller: GameController) extends Observer {
  controller.addObserver(this)

  override def update(): Unit = {
    println(createGameFrame())
  }

  def createGameFrame(): String = {
    val sb = new StringBuilder


    val separator = "=" * 150
    sb.append(separator).append("\n")
    sb.append(s"Aktueller Spieler: ${controller.getCurrentPlayerName}\n")
    sb.append(separator).append("\n\n")


    val headerBuilder = new StringBuilder("\t")
    headerBuilder.append("|Pts:")
    val currentPlayerHand = controller.getCurrentPlayerHand
    for (i <- 0 until currentPlayerHand.length) {
      headerBuilder.append(f"| $i%-2d ")
    }
    headerBuilder.append("|\n")


    sb.append(headerBuilder.toString())


    // Find the longest player name to determine fixed width
    val maxNameLength = controller.getAllPlayers.map(_.name.length).max + 3 // +3 for the marker " *"

    controller.getAllPlayers.foreach { player =>
      val handStr = player.hand.map { card =>
        val cardStr = s"${card.rank.toString}${card.suit.toString}"
        f"$cardStr%-3s"
      }.mkString("| ")
      val currentMarker = if (player.name == controller.getCurrentPlayerName) " *" else "   "
      val nameWithMarker = s"${player.name}${currentMarker}"
      val points = f"| ${player.points}%-2d |"
      sb.append(s"${nameWithMarker.padTo(maxNameLength, ' ')}$points $handStr|\n")
    }
    sb.append("\n")


    sb.append("Aktueller Pot:\n")
    val potStr = if (controller.getCurrentPot.isEmpty)
      "Leer"
    else controller.getCurrentPot.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      f"$cardStr%-3s"
    }.mkString(" | ")
    sb.append(s"| $potStr |\n")
    sb.append(separator).append(s"\n${controller.getCurrentPlayerName}, welche Karte möchtest du spielen? (Gib den Index ein): \n")
    sb.toString
  }
}