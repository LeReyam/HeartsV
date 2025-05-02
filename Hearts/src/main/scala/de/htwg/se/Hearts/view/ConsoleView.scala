package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.{Player, Card}
import de.htwg.se.Hearts.controller.Controller
import scala.collection.mutable.ListBuffer
import scala.io.StdIn.readLine

class ConsoleView(controller: Controller) extends View {

  override def displayPlayerHand(player: Player): Unit = {
    // Formatiere jede Karte mit fester Breite (3 Zeichen + Padding)
    val handStr = player.hand.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      // Füge Leerzeichen hinzu, um eine einheitliche Breite zu gewährleisten
      f"$cardStr%-3s"
    }.mkString(" | ")
    println(s"${player.name}\t| $handStr |")
  }

  // Diese Methode erwartet jetzt eine Liste von Spielern und den aktuellen Pott
  override def displayGameState(players: List[Player], currentPot: ListBuffer[Card]): Unit = {
    println("-" * 100)

    // Dynamische Generierung der Spaltenüberschriften basierend auf der Länge der Hand des aktuellen Spielers
    val headerBuilder = new StringBuilder("\t")
    // Prüfen, ob es Spieler gibt und ob der aktuelle Spieler eine Hand hat
    if (players.nonEmpty) {
      val currentPlayer = players.find(_.name == controller.getCurrentPlayerName).getOrElse(players.head)
      for (i <- 0 until currentPlayer.hand.length) {
        headerBuilder.append(f"|  $i  ")
      }
      headerBuilder.append("|")
    }
    println(headerBuilder.toString())

    // Spielerhände anzeigen
    players.foreach(displayPlayerHand)
    println("-" * 100)

    // Anzeige des aktuellen Potts mit einheitlicher Formatierung
    val potStr = currentPot.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      // Füge Leerzeichen hinzu, um eine einheitliche Breite zu gewährleisten
      f"$cardStr%-3s"
    }.mkString(" | ")
    println(f"Current Pott :\t| $potStr\t|")

    println("-" * 100)
  }

  override def displayMessage(message: String): Unit = {
    println(message)
  }

  override def run(): Unit = {
    var playing = true

    while (playing && !controller.gameIsOver) {
      // Zeige den aktuellen Spielzustand an
      displayGameState(controller.getAllPlayers, controller.getCurrentPot)

      // Zeige an, welcher Spieler am Zug ist
      displayMessage(s"${controller.getCurrentPlayerName} ist am Zug.")

      // Lese die Eingabe des Benutzers
      displayMessage("Welche Karte möchtest du spielen? (Gib den Index ein)")
      val input = readLine().trim

      try {
        val index = input.toInt
        if (controller.playCard(index)) {
          // Karte wurde erfolgreich gespielt
          val currentPlayer = controller.getAllPlayers.find(_.name == controller.getCurrentPlayerName).get
          displayMessage(s"Karte wurde gespielt. ${currentPlayer.name} ist jetzt am Zug.")
        } else {
          displayMessage("Ungültiger Index. Bitte einen gültigen Index eingeben.")
        }
      } catch {
        case _: NumberFormatException =>
          displayMessage("Bitte eine Zahl eingeben.")
      }

      // Prüfe, ob das Spiel vorbei ist
      if (controller.gameIsOver) {
        displayMessage("Alle Spieler haben ihre Karten gespielt. Spiel beendet.")
        playing = false
      }
    }
  }
}
