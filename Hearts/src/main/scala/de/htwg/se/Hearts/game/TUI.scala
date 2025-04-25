package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.game.{Dealer, GameController}
import scala.io.StdIn.readLine

class TUI():
  def start(): Unit =
    val playerNames = getPlayerNames()
    val deck = Dealer.createDeck()
    val shuffledDeck = Dealer.shuffle(deck)
    val players = Dealer.deal(shuffledDeck, playerNames)
    GameController.startGame(players)


  def getPlayerNames(): List[String] =
    println("Wie viele Spieler seid ihr?")
    val playerAmount = readLine().toInt
    val names =
      for i <- 1 to playerAmount yield
        println("Bitte den Namen des Spielers angeben:")
        readLine()

    val playerList = names.toList
    println(s"Spieler: $playerList")
    playerList
