package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._
import scala.collection.mutable.ListBuffer

object GameController extends GameControllerService:
  def startGame(players: List[Player]): Unit =
    val totalRounds = players.head.hand.length
    for round <- 1 to totalRounds do
      println(s"Runde $round:")
      val trick = Game.playRound(players)
      Scoring.updateScores(players, trick)
      players.foreach(p => println(s"${p.name}: ${p.points} Punkte"))