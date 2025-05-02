package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.Players


import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.view.*
import scala.io.StdIn.readLine

class GameController(game: Game, view: View) {
  var currentPot: ListBuffer[Card] = ListBuffer()  // Der aktuelle Pott

  def startGame(): Unit = {
    var playing = true
    val player = game.players.head  // Aktuell nur 1 Spieler (z. B. Alice)

    // Erstelle ein `Players`-Objekt aus der Liste der Spieler
    val PlayerList = Players(game.players)

    while (playing) {
      // Übergibt das `Players`-Objekt und den `currentPot` an die View
      view.displayGameState(PlayerList, currentPot)

      readCardInput() match {
        case Some(card) =>
          if (player.hand.contains(card)) {
            player.playCard(card)
            // Füge die Karte zum aktuellen Pott hinzu
            currentPot += card
            view.displayMessage(s"${player.name} spielt: ${card.rank} of ${card.suit}")
          } else {
            view.displayMessage("Diese Karte ist nicht in deiner Hand.")
          }
        case None =>
          view.displayMessage("Ungültige Eingabe. Bitte erneut versuchen.")
      }

      if (player.hand.isEmpty) {
        view.displayMessage("Alle Karten gespielt. Spiel beendet.")
        playing = false
      }
    }
  }

  def readCardInput(): Option[Card] = {
    println("Welche Karte möchtest du spielen? (z.B. 'Hearts Two')")
    val input = readLine().trim.split(" ")

    if (input.length != 2) {
      println("Ungültiges Format. Bitte 'Suit Rank' eingeben.")
      None
    } else {
      val suitStr = input(0).capitalize
      val rankStr = input(1).capitalize

      val maybeSuit = suitStr match {
        case "Hearts"   => Some(Suit.Hearts)
        case "Spades"   => Some(Suit.Spades)
        case "Diamonds" => Some(Suit.Diamonds)
        case "Clubs"    => Some(Suit.Clubs)
        case _ => None
      }

      val maybeRank = Rank.values.find(_.toString == rankStr)

      (maybeSuit, maybeRank) match {
        case (Some(suit), Some(rank)) =>
          println(s"Du hast gewählt: ${rank.toString} of ${suit.toString}")
          Some(Card(rank, suit))
        case _ =>
          println(s"Ungültige Eingabe: $suitStr $rankStr. Bitte gültige Karte eingeben.")
          None
      }
    }
  }
}
