package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.Players
import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.view.*
import scala.io.StdIn.readLine

class GameController(game: Game, view: View) {
  var currentPot: ListBuffer[Card] = ListBuffer()  // Der aktuelle Pott
  var currentPlayerIndex: Int = 0  // Aktueller Spielerindex, beginnt bei 0 (erster Spieler)

  def startGame(): Unit = {
    var playing = true

    // Erstelle ein `Players`-Objekt aus der Liste der Spieler im Spiel
    val playerList = Players(game.players)

    // Setze den aktuellen Spielerindex zurück auf 0
    currentPlayerIndex = 0

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
    // Aktueller Spieler basierend auf dem Index
    val currentPlayer = game.players(currentPlayerIndex)

    println("Welche Karte möchtest du spielen? (Gib den Index ein)")
    val input = readLine().trim

    try {
      val index = input.toInt
      if (index >= 0 && index < currentPlayer.hand.length) {
        val selectedCard = currentPlayer.hand(index)
        Some(selectedCard)
      } else {
        view.displayMessage(s"Ungültiger Index: $index. Bitte einen gültigen Index eingeben.")
        None
      }
    } catch {
      case _: NumberFormatException =>
        view.displayMessage("Bitte eine Zahl eingeben.")
        None
    }
  }
}
