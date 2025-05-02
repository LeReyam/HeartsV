package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.{Player, Card, Players}
import scala.collection.mutable.ListBuffer

class ConsoleView extends View {

  override def displayPlayerHand(player: Player): Unit = {
    val handStr = player.hand.map(card => s"${card.rank.toString}${card.suit.toString}").mkString(" | ")
    println(s"${player.name}\t| $handStr |")
  }

  // Diese Methode erwartet jetzt `Players` und den aktuellen Pott
  override def displayGameState(players: Players, currentPot: ListBuffer[Card]): Unit = {
    println("-" * 50)
    println(f"\t\t| 1\t\t| 2\t\t|")  // Hier kannst du mehr Spalten einfügen, wenn du mehr Spieler hast

    // Spielerhände anzeigen
    players.players.foreach(displayPlayerHand)

    // Anzeige des aktuellen Potts
    val potStr = currentPot.map(card => s"${card.rank.toString}${card.suit.toString}").mkString(" | ")
    println(f"Current Pott :\t| $potStr\t|")

    println("-" * 50)
  }

  override def displayMessage(message: String): Unit = {
    println(message)
  }
}
