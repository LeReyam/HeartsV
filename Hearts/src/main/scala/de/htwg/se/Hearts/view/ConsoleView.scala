package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.{Player, Card, Players}

class ConsoleView extends View {
  override def displayPlayerHand(player: Player): Unit = {
    val handStr = player.hand.map(c => s"${c.rank} of ${c.suit}").mkString(", ")
    println(s"${player.name}'s hand: $handStr")
  }

  override def displayGameState(players: Players): Unit = {
    println("Current Game State:")
    players.foreach(displayPlayerHand)
  }

  override def displayMessage(message: String): Unit = {
    println(message)
  }
}
