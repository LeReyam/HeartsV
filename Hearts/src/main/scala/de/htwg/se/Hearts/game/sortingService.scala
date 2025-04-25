package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.{Player, Card}

trait ScoringService:
  def updateScores(players: List[Player], tricks: List[Card]): Unit

