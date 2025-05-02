package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

trait Controller {
  // Methoden, die von der View aufgerufen werden können
  def getCurrentPlayerName: String
  def getCurrentPlayerHand: List[Card]
  def getAllPlayers: List[Player]
  def getCurrentPot: ListBuffer[Card]
  def playCard(index: Int): Boolean
  def gameIsOver: Boolean
}