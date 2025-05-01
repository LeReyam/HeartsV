package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.view.View
import scala.io.StdIn.readLine

class GameController(game: Game, view: View) {

  def startGame(): Unit =
    var playing = true
    val player = game.players.head  // Aktuell nur 1 Spieler (z. B. Alice)

    while playing do
      view.displayPlayerHand(player)
      readCardInput() match
        case Some(card) =>
          if player.hand.contains(card) then
            player.playCard(card)
            view.displayMessage(s"${player.name} spielt: ${card.rank} of ${card.suit}")
          else
            view.displayMessage("Diese Karte ist nicht in deiner Hand.")
        case None =>
          view.displayMessage("Ungültige Eingabe. Bitte erneut versuchen.")

      if player.hand.isEmpty then
        view.displayMessage("Alle Karten gespielt. Spiel beendet.")
        playing = false

  def readCardInput(): Option[Card] =
    println("Welche Karte möchtest du spielen? (z.B. 'Hearts Two')")
    val input = readLine().trim.split(" ")

    if input.length != 2 then
      println("Ungültiges Format. Bitte 'Suit Rank' eingeben.")
      None
    else
      val suitStr = input(0).capitalize
      val rankStr = input(1).capitalize

      // Mapped die Text-Eingabe der Suit zu einem Suit-Wert
      val maybeSuit = suitStr match
        case "Hearts"   => Some(Suit.Hearts)
        case "Spades"   => Some(Suit.Spades)
        case "Diamonds" => Some(Suit.Diamonds)
        case "Clubs"    => Some(Suit.Clubs)
        case _ => None  // Ungültiger Wert

      val maybeRank = Rank.values.find(_.toString == rankStr)

      (maybeSuit, maybeRank) match
        case (Some(suit), Some(rank)) =>
          // Gibt die Karte mit dem Symbol aus
          println(s"Du hast gewählt: ${rank.toString} of ${suit.toString}")
          Some(Card(rank, suit))
        case _ =>
          println(s"Ungültige Eingabe: $suitStr $rankStr. Bitte gültige Karte eingeben.")
          None
}