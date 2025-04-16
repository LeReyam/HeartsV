package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._

object Game:
  def playRound(players: List[Player]): Unit =
    var trick = collection.mutable.ListBuffer[(Card, Player)]()
    players.foreach { player =>
      print(player.showHandString())
      val cardToPlay = player.hand.head // vereinfachte Logik
      player.playCard(cardToPlay)
      trick += ((cardToPlay, player))
    }
    val winnerName = trick.maxBy{ case (card, _) => card}
    println(winnerName)
    // Gewinnerlogik kann später noch cleverer werden
    println("Stich gespielt.\n")