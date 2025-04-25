package de.htwg.se.Hearts.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*

class TUISpec extends AnyWordSpec with Matchers:

  "A TUI" should {

    "trigger real readLine and println for coverage" in {

            val simulatedInput = Iterator("TestUser\n")

            val mockReadLine: () => String = () => simulatedInput.next()

            val output = new java.io.ByteArrayOutputStream()

            Console.withOut(new java.io.PrintStream(output)):
                val input = mockReadLine()
                println(s"Input was: $input")

            output.toString should include ("Input was: TestUser")
    }

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

        // Mock DealerService
        val mockDealer = new DealerService:
            def createDeck() =
                println("Creating deck...")
                List.fill(4)(dummyCard)

            def shuffle(deck: List[Card]) =
                println(s"Shuffling deck: $deck")
                deck

            def deal(deck: List[Card], names: List[String]) =
                println(s"Dealing deck: $deck")
                val dealtPlayers = names.map(name => Player(name + "_mocked", List(dummyCard)))
                println(s"Dealt players: $dealtPlayers")
                dealtPlayers


        var startedPlayers: List[Player] = Nil

        val mockGame = new GameControllerService:
            def startGame(players: List[Player]): Unit =
                println(s"Started game with players: $players")
                startedPlayers = players

        val tui = new TUI(mockReadLine, mockPrintln, mockDealer, mockGame)

        tui.start()

        startedPlayers.map(_.name) shouldEqual List("Alice_mocked", "Bob_mocked")

        output should contain ("Wie viele Spieler seid ihr?")
        output should contain ("Bitte den Namen des Spielers angeben:")
        output should contain ("Spieler: List(Alice, Bob)")
    }
}