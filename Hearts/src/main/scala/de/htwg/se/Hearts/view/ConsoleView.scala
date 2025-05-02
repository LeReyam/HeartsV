package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.{Player, Card, Players}
import scala.collection.mutable.ListBuffer

class ConsoleView extends View {

  override def displayPlayerHand(player: Player): Unit = {
    // Formatiere jede Karte mit fester Breite (3 Zeichen + Padding)
    val handStr = player.hand.map { card =>
      val cardStr = s"${card.rank.toString}${card.suit.toString}"
      // Füge Leerzeichen hinzu, um eine einheitliche Breite zu gewährleisten
      f"$cardStr%-3s"
    }.mkString(" | ")
    println(s"${player.name}\t| $handStr |")
  }

  // Diese Methode erwartet jetzt `Players` und den aktuellen Pott
  override def displayGameState(players: Players, currentPot: ListBuffer[Card]): Unit = {
    println("-" * 100)

    // Dynamische Generierung der Spaltenüberschriften basierend auf der Länge der Hand des letzten Spielers
    val headerBuilder = new StringBuilder("\t")
    // Prüfen, ob es Spieler gibt und ob der letzte Spieler eine Hand hat
    if (players.players.nonEmpty) {
      val lastPlayer = players.players.last
      for (i <- 0 to lastPlayer.hand.length - 1) {
        headerBuilder.append(f"|  $i  ")
      }
      headerBuilder.append("|")
    }
    println(headerBuilder.toString())

    // Spielerhände anzeigen
    players.players.foreach(displayPlayerHand)
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
}
