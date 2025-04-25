package de.htwg.se.Hearts.game


import de.htwg.se.Hearts.model._

object Scoring extends ScoringService:
  def updateScores(players: List[Player], trick: List[Card]): Unit =
    // Dummy: hier könnte man Punkte zählen
    ()