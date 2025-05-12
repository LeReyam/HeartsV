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


    val separator = "=" * 80
    sb.append(separator).append("\n")


    sb.append(s"Aktueller Spieler: ${controller.getCurrentPlayerName}\n")
    sb.append(separator).append("\n\n")


    val headerBuilder = new StringBuilder("\t")
    val currentPlayerHand = controller.getCurrentPlayerHand
    for (i <- 0 until currentPlayerHand.length) {
      headerBuilder.append(f"| $i%-2d ")
    }
    headerBuilder.append("|\n")
    sb.append(headerBuilder.toString())


    controller.getAllPlayers.foreach { player =>
      val handStr = player.hand.map { card =>
        val cardStr = s"${card.rank.toString}${card.suit.toString}"
        f"$cardStr%-3s"
      }.mkString("| ")
      val currentMarker = if (player.name == controller.getCurrentPlayerName) " *" else ""
      sb.append(s"${player.name}${currentMarker}\t| $handStr|\n")
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