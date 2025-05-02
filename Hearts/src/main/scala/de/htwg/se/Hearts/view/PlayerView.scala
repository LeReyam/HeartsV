package de.htwg.se.Hearts.view
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*

class PlayerView(player: Player) extends Observer {
  player.addObserver(this)

  override def update(): Unit = {
    // Hier kannst du deine Karten als kurze Schreibweise ausgeben
    val handStr = player.hand.map(card => card.toString).mkString(" | ")
    println(s"${player.name}\t| $handStr |")
  }
}
