package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.Player

trait GameService {
  def playRound(players: List[Player]): Unit
}

