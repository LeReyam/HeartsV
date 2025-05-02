package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.game.*
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

import de.htwg.se.Hearts.model.{Player, Players}

trait View {
  def displayPlayerHand(player: Player): Unit
  def displayGameState(players: Players, currentPot: ListBuffer[Card]): Unit
  def displayMessage(message: String): Unit
}