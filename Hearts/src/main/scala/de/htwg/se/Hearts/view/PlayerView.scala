package de.htwg.se.Hearts.view
import de.htwg.se.Hearts.game.*
import de.htwg.se.Hearts.model.*

class PlayerView(player: Player) extends Observer {
  player.addObserver(this)

  override def update(): Unit = {
    val handStr = player.hand.map(card => s"${card.rank} of ${card.suit}").mkString(", ")
    //println(s"Updated hand for ${player.name}: $handStr")
  }
}