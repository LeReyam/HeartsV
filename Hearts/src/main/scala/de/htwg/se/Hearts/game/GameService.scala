package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.Player
import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Card

trait GameService {
  def playRound(players: List[Player]): ListBuffer[(Card, Player)]
}

