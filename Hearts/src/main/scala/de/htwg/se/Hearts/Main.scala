package de.htwg.se.Hearts
import de.htwg.se.Hearts.model
import de.htwg.se.Hearts.game.*


// chcp 65001



import de.htwg.se.Hearts.game.Dealer
import de.htwg.se.Hearts.game.GameController

@main def runHearts(): Unit =
  val playerNames = List("Alice", "Bob", "Carol", "Dave")
  val deck = Dealer.createDeck()
  val players = Dealer.shuffleAndDeal(deck, playerNames)

  GameController.startGame(players)