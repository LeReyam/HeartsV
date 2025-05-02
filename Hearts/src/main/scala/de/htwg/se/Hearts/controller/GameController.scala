package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.io.StdIn.readLine

class GameController(game: Game) extends Controller {
  private var currentPot: ListBuffer[Card] = ListBuffer()  // Der aktuelle Pott
  private var currentPlayerIndex: Int = 0  // Aktueller Spielerindex, beginnt bei 0 (erster Spieler)
  private var gameOver: Boolean = false

  // Implementierung der Controller-Interface-Methoden
  override def getCurrentPlayerName: String = game.players(currentPlayerIndex).name

  override def getCurrentPlayerHand: List[Card] = game.players(currentPlayerIndex).hand

  override def getAllPlayers: List[Player] = game.players

  override def getCurrentPot: ListBuffer[Card] = currentPot

  override def gameIsOver: Boolean = gameOver

  override def playCard(index: Int): Boolean = {
    val currentPlayer = game.players(currentPlayerIndex)

    if (index >= 0 && index < currentPlayer.hand.length) {
      val selectedCard = currentPlayer.hand(index)
      currentPlayer.playCard(selectedCard)
      currentPot += selectedCard

      // Wechsle zum nächsten Spieler
      currentPlayerIndex = (currentPlayerIndex + 1) % game.players.length

      // Prüfe, ob alle Spieler keine Karten mehr haben
      if (game.players.forall(_.hand.isEmpty)) {
        gameOver = true
      }

      true
    } else {
      false
    }
  }

  // Methode zum Zurücksetzen des Spiels
  def resetGame(): Unit = {
    currentPlayerIndex = 0
    currentPot.clear()
    gameOver = false
  }
}