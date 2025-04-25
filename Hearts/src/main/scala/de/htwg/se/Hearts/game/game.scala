package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._

object Game extends GameService:
  def playRound(players: List[Player]): Unit =
    var trick = collection.mutable.ListBuffer[(Card, Player)]()
    players.foreach { player =>
      print(player.showHandString())
      val cardToPlay = player.hand.head // vereinfachte Logik
      player.playCard(cardToPlay)
      trick += ((cardToPlay, player))
    }
    // NEW: Get the suit of the first card played
    val leadingSuit = trick.head._1.suit

    // NEW: Only consider cards with matching suit for winning
    val winner = trick
    .filter { case (card, _) => card.suit == leadingSuit }
    .maxBy { case (card, _) => card }


    println(winner)
    // Gewinnerlogik kann später noch cleverer werden
    println("Stich gespielt.\n")