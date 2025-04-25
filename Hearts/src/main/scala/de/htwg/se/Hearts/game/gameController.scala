package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._

object GameController extends GameControllerService:
  def startGame(players: List[Player]): Unit =
    val totalRounds = players.head.hand.length
    for round <- 1 to totalRounds do
      println(s"Runde $round:")
      Game.playRound(players)
      Scoring.updateScores(players, List())
      players.foreach(p => println(s"${p.name}: ${p.points} Punkte"))