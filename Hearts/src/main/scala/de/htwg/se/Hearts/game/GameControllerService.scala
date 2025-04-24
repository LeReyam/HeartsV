package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.Player

trait GameControllerService:
  def startGame(players: List[Player]): Unit

