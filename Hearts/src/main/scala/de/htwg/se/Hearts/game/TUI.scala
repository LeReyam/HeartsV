package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.game.{Dealer, GameController}

class TUI(readLine: () => String = scala.io.StdIn.readLine, printlnFunc: String => Unit = println):

	def start(): Unit =
		val playerNames = getPlayerNames()
		val deck = Dealer.createDeck()
		val shuffledDeck = Dealer.shuffle(deck)
		val players = Dealer.deal(shuffledDeck,playerNames)
		GameController.startGame(players)


	def getPlayerNames(): List[String] =
		printlnFunc("Wie viele Spieler seid ihr?")
		val playerAmount = readLine().toInt
		val names =
			for i <- 1 to playerAmount yield
				printlnFunc("Bitte den Namen des Spielers angeben:")
				readLine()

		val playerList = names.toList
		printlnFunc(s"Spieler: $playerList")
		playerList