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

    // Erstelle ein `Players`-Objekt aus der Liste der Spieler im Spiel
    val playerList = Players(game.players)

    // Aktueller Spielerindex, beginnt bei 0 (erster Spieler)
    var currentPlayerIndex = 0

    while (playing) {
      // Aktueller Spieler basierend auf dem Index
      val currentPlayer = game.players(currentPlayerIndex)

      // Übergibt das `Players`-Objekt und den `currentPot` an die View
      view.displayGameState(playerList, currentPot)

      // Zeige an, welcher Spieler am Zug ist
      view.displayMessage(s"${currentPlayer.name} ist am Zug.")

      readCardInput() match {
        case Some(card) =>
          if (currentPlayer.hand.contains(card)) {
            currentPlayer.playCard(card)
            // Füge die Karte zum aktuellen Pott hinzu
            currentPot += card
            view.displayMessage(s"${currentPlayer.name} spielt: ${card.rank} of ${card.suit}")

            // Wechsle zum nächsten Spieler
            currentPlayerIndex = (currentPlayerIndex + 1) % game.players.length
          } else {
            view.displayMessage("Diese Karte ist nicht in deiner Hand.")
          }
        case None =>
          view.displayMessage("Ungültige Eingabe. Bitte erneut versuchen.")
      }

      // Prüfe, ob alle Spieler keine Karten mehr haben
      if (game.players.forall(_.hand.isEmpty)) {
        view.displayMessage("Alle Spieler haben ihre Karten gespielt. Spiel beendet.")
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
