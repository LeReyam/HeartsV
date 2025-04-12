package de.htwg.se.Hearts
import de.htwg.se.Hearts.model
import de.htwg.se.Hearts.game.*


// chcp 65001



@main def runHearts(): Unit = {
  // Definiere die Spielernamen
  val playerNames = List("Alice", "Bob", "Carol", "Dave")

  // Erstelle und mische das Deck
  val deck = createDeck()

  // Teile die Karten an die Spieler aus
  val players = shuffleAndDeal(deck, playerNames)

  // Gib die Karten jedes Spielers aus
  players.foreach { player =>
    println(player)  // Aufruf der toString-Methode von Player, die auch die Karten ausgibt
  }
}