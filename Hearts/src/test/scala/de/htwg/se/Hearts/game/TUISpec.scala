package de.htwg.se.Hearts.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*

class TUISpec extends AnyWordSpec with Matchers:

  "A TUI" should {

    "collect player names based on input" in {
      val input = Iterator("2", "Alice", "Bob")
      val mockReadLine: () => String = () => input.next()
      val capturedOutput = collection.mutable.ListBuffer[String]()
      val mockPrintln: String => Unit = s => capturedOutput += s

      val tui = new TUI(mockReadLine, mockPrintln)

      val result = tui.getPlayerNames()

      result shouldEqual List("Alice", "Bob")
      capturedOutput should contain ("Wie viele Spieler seid ihr?")
      capturedOutput should contain ("Bitte den Namen des Spielers angeben:")
      capturedOutput.last should include ("Spieler: List(Alice, Bob)")
    }

    "start the game using mocked dealer and game controller" in {
      val input = Iterator("2", "Alice", "Bob")
      val mockReadLine: () => String = () => input.next()
      val output = collection.mutable.ListBuffer[String]()
      val mockPrintln: String => Unit = s => output += s

      val dummyCard = Card(Rank.Seven, Suit.Hearts)

      val mockDealer = new DealerService:
        def createDeck() = List.fill(4)(dummyCard)
        def shuffle(deck: List[Card]) = deck
        def deal(deck: List[Card], names: List[String]) =
          names.map(name => Player(name + "_mocked", List(dummyCard)))

      var startedPlayers: List[Player] = Nil
      val mockGame = new GameControllerService:
        def startGame(players: List[Player]): Unit = startedPlayers = players

      val tui = new TUI(mockReadLine, mockPrintln, mockDealer, mockGame)

      tui.start()

      startedPlayers.map(_.name) shouldEqual List("Alice_mocked", "Bob_mocked")
    }
  }
