package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

trait View {
  def displayPlayerHand(player: Player): Unit
  def displayGameState(players: List[Player], currentPot: ListBuffer[Card]): Unit
  def displayMessage(message: String): Unit
  def run(): Unit  // Neue Methode zum Starten der View
}