package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._

object GameController:
  def startGame(players: List[Player]): Unit =
    val totalRounds = 13
    for round <- 1 to totalRounds do
      println(s"Runde $round:")
      Game.playRound(players)
      // Dummy-Stich: später sinnvoll befüllen
      Scoring.updateScores(players, List())
      players.foreach(p => println(s"${p.name}: ${p.points} Punkte"))