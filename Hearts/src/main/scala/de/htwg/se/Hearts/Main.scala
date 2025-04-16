package de.htwg.se.Hearts
import de.htwg.se.Hearts.model
import de.htwg.se.Hearts.game.*
import scala.io.StdIn.readLine


// chcp 65001



import de.htwg.se.Hearts.game.Dealer
import de.htwg.se.Hearts.game.GameController
import scala.compiletime.ops.double

@main def runHearts(): Unit =
  var playerNames = List[String]()
  println("Wie viele Spieler seid ihr?")
  val playeramount = readLine()
  println(playeramount)
  for player <- 1 to playeramount.toInt do
    println("Bitte den Namen des Spielers angeben:")
    playerNames = playerNames :+ readLine()
  println(playerNames)
  val deck = Dealer.createDeck()
  val players = Dealer.shuffleAndDeal(deck, playerNames)
  GameController.startGame(players)