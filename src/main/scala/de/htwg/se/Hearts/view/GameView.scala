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

    // Trennlinie
    val separator = "=" * 80
    sb.append(separator).append("\n")

    // Spielername und aktueller Spieler
    sb.append(s"Aktueller Spieler: ${controller.getCurrentPlayerName}\n")
    sb.append(separator).append("\n\n")

    // Dynamische Generierung der Spaltenüberschriften basierend auf der Länge der Hand des aktuellen Spielers
    val headerBuilder = new StringBuilder("\t")
    val currentPlayerHand = controller.getCurrentPlayerHand
    for (i <- 0 until currentPlayerHand.length) {
      headerBuilder.append(f"|  $i  ")
    }
    headerBuilder.append("|\n")
    sb.append(headerBuilder.toString())

    // Alle Spieler und ihre Hände anzeigen
    controller.getAllPlayers.foreach { player =>
      val handStr = player.hand.map { card =>
        val cardStr = s"${card.rank.toString}${card.suit.toString}"
        f"$cardStr%-3s"
      }.mkString(" | ")
      val currentMarker = if (player.name == controller.getCurrentPlayerName) " *" else ""
      sb.append(s"${player.name}${currentMarker}\t| $handStr |\n")
    }
    sb.append("\n")

    // Aktueller Pot
    sb.append("Aktueller Pot:\n")
    val potStr = if (controller.getCurrentPot.isEmpty) "Leer" else controller.getCurrentPot.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      f"$cardStr%-3s"
    }.mkString(" | ")
    sb.append(s"| $potStr |\n")
    sb.append(separator).append("\n")
    if (controller.gameIsOver) {
      sb.append("Spiel beendet!\n")
    }

    sb.toString
  }
}