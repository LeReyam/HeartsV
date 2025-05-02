package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.game.{Dealer, GameController}
import scala.io.StdIn.readLine

class TUI():

  def start(): Unit = {
    val playerNames = getPlayerNames()
    val deck = Dealer.createDeck()
    val shuffledDeck = Dealer.shuffle(deck)
    val players = Dealer.deal(shuffledDeck, playerNames)
    GameController.startGame(players)
  }

  def getPlayerNames(): List[String] = {
    println("Wie viele Spieler seid ihr?")
    val playerAmount = readLine().toInt
    val names = for i <- 1 to playerAmount yield {
      println(s"Bitte den Namen des Spielers $i angeben:")
      readLine()
    }
    val playerList = names.toList
    println(s"Spieler: $playerList")
    playerList
  }

  // Zeigt die Handkarten eines Spielers an, sortiert nach Farben
  def displayHandCards(players: List[Player]): Unit = {
    // Wir nehmen an, dass wir nur einen Spieler anzeigen
    val player = players.head

    // Gruppiere Karten nach Farbe
    val cardsBySuit = player.hand.groupBy(_.suit)

    // Bestimme die maximale Anzahl von Karten pro Farbe
    val maxCardsPerSuit = cardsBySuit.values.map(_.size).maxOption.getOrElse(0)

    // Filtere nur die Farben, von denen der Spieler Karten hat
    val suitsWithCards = List(Suit.Spades, Suit.Hearts, Suit.Diamonds, Suit.Clubs)
      .filter(suit => cardsBySuit.contains(suit) && cardsBySuit(suit).nonEmpty)

    // Wenn keine Karten vorhanden sind, früh zurückkehren
    if (suitsWithCards.isEmpty) {
      println("Keine Karten auf der Hand.")
      return
    }

    // Erstelle die Kopfzeile mit genügend Spalten
    val headerWidth = 17 * maxCardsPerSuit + 1  // 17 Zeichen pro Spalte + 1 für die letzte Trennlinie
    println("-" * headerWidth)

    val headerRow = (1 to maxCardsPerSuit).map(i => f"| Nr. | Karte   ").mkString("") + "|"
    println(headerRow)
    println("-" * headerWidth)

    // Für jede Farbe, von der Karten vorhanden sind, eine Zeile ausgeben
    suitsWithCards.foreach { suit =>
      val cardsOfSuit = cardsBySuit(suit).sortBy(_.rank)

      // Erstelle die Zeile für diese Farbe
      val rowContent = (0 until maxCardsPerSuit).map { colIndex =>
        if (colIndex < cardsOfSuit.size) {
          val card = cardsOfSuit(colIndex)
          val cardIndex = player.hand.indexOf(card) + 1  // +1 für benutzerfreundliche Nummerierung
          f"| $cardIndex%2d | $card%-7s "
        } else {
          "|    |         "  // Leere Zelle für fehlende Karten
        }
      }.mkString("") + "|"

      println(rowContent)
      println("-" * headerWidth)
    }

    println(s"Wählen Sie eine Karte aus (1-${player.hand.size}):")
  }

  // Funktion zur Kartenauswahl des Spielers
  def readCardSelection(maxCards: Int): Int = {
    var validInput = false
    var selectedIndex = -1

    while !validInput do
      println(s"Bitte wählen Sie eine Karte aus (1-$maxCards):")
      val input = readLine()

      try {
        val selection = input.toInt
        if selection >= 1 && selection <= maxCards then
          selectedIndex = selection - 1 // -1, weil Liste bei 0 beginnt
          validInput = true
        else
          println(s"Ungültige Eingabe: Bitte eine Zahl zwischen 1 und $maxCards eingeben.")
      } catch {
        case _: NumberFormatException =>
          println("Ungültige Eingabe: Bitte eine Zahl eingeben.")
      }

    selectedIndex
  }

  // Auswahl einer Karte durch den Spieler
  def selectCard(player: Player): Option[Card] = {
    val cardNumber = readCardSelection(player.hand.length)
    if (cardNumber >= 0 && cardNumber < player.hand.length) {
      Some(player.hand(cardNumber)) // Karte aus der Hand auswählen
    } else {
      println(s"Ungültige Eingabe, bitte eine Zahl zwischen 1 und ${player.hand.length} eingeben.")
      None
    }
  }

  // Zeigt die bereits gespielten Karten an (z.B. in einer Runde)
  def displayPlayedCards(playedCards: List[PlayedCard]): Unit = {
    playedCards.foreach { played =>
      println(s"${played.getPlayer.name}: ${played.getCard.rank}${played.getCard.suit}")
    }
  }