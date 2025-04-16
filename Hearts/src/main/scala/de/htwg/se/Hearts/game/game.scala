package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._

object Game:
  def playRound(players: List[Player]): Unit =
    val trick = collection.mutable.ListBuffer[Card]()
    players.foreach { player =>
      player.showHand()
      val cardToPlay = player.hand.head // vereinfachte Logik
      player.playCard(cardToPlay)
      trick += cardToPlay
    }
    // Gewinnerlogik kann später noch cleverer werden
    println("Stich gespielt.\n")