package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.io.StdIn.readLine

class GameController(game: Game) extends Observable {
  private var currentPot: ListBuffer[Card] = ListBuffer()
  private var currentPlayerIndex: Int = 0
  private var gameOver: Boolean = false

  // Implementierung der Controller-Interface-Methoden
  def getCurrentPlayerName: String = game.players(currentPlayerIndex).name

  def getCurrentPlayerHand: List[Card] = game.players(currentPlayerIndex).hand

  def getAllPlayers: List[Player] = game.players

  def getCurrentPot: ListBuffer[Card] = currentPot

  def gameIsOver: Boolean = gameOver

  def playCard(index: Int): Boolean = {
    val currentPlayer = game.players(currentPlayerIndex)

    if (index >= 0 && index < currentPlayer.hand.length) {
      val selectedCard = currentPlayer.hand(index)
      currentPlayer.playCard(selectedCard)
      currentPot += selectedCard
      currentPlayerIndex = (currentPlayerIndex + 1) % game.players.length
      if (game.players.forall(_.hand.isEmpty)) {
        gameOver = true
      }
      true
    } else {
      false
    }
  }


  // Methode zum Starten und Durchführen des Spiels
  def runGame(): Unit = {
    var playing = true

    while (playing && !gameIsOver) {
      notifyObservers()
      val cardIndex = getCardIndexFromPlayer()
      if (cardIndex >= 0) {
        playCard(cardIndex)
      }
      if (gameIsOver) {
        playing = false
      }
    }
    notifyObservers()
  }

  def getCardIndexFromPlayer(): Int = {
    print(s"\n${getCurrentPlayerName}, welche Karte möchtest du spielen? (Gib den Index ein): ")
    val input = readLine().trim

    try {
      val index = input.toInt
      if (index >= 0 && index < getCurrentPlayerHand.length) {
        return index
      } else {
        println("Ungültiger Index. Bitte einen gültigen Index eingeben.")
        return -1
      }
    } catch {
      case _: NumberFormatException =>
        println("Bitte eine Zahl eingeben.")
        return -1
    }
  }
}