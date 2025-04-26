package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._
import scala.collection.mutable.ListBuffer

object GameController extends GameControllerService:
  def startGame(players: List[Player]): Unit =
    val totalRounds = players.head.hand.length
    for round <- 1 to totalRounds do
      println(s"Runde $round:")
      val trick = Game.playRound(players)
      updateScores(players, trick)
      players.foreach(p => println(s"${p.name}: ${p.points} Punkte"))

  def updateScores(players: List[Player], trick: ListBuffer[(Card, Player)]): Unit =
    // Wenn der Trick nicht leer ist, berechne die Punkte
    if trick.nonEmpty then
      // Finde den Gewinner des Tricks
      val leadingSuit = trick.head._1.suit
      val winner = trick
        .filter { case (card, _) => card.suit == leadingSuit }
        .maxBy { case (card, _) => card }._2

      // Zähle die Punkte für Herzkarten und die Pik Dame
      var points = 0
      trick.foreach { case (card, _) =>
        if card.suit == Suit.Hearts then
          points += 1
        else if card.suit == Suit.Spades && card.rank == Rank.Queen then
          points += 13
      }

      // Füge die Punkte dem Gewinner hinzu
      winner.points += points

      println(s"${winner.name} erhält $points Punkte für diesen Stich")
    end if