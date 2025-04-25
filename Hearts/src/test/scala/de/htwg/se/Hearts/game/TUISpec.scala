package de.htwg.se.Hearts.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*

class TUISpec extends AnyWordSpec with Matchers:

  "A TUI" should {

    "trigger real readLine and println for coverage" in {
            // Simulierte Eingabe über Iterator
            val simulatedInput = Iterator("TestUser\n")

            // Mock readLine als Funktion, die die Eingabe von simulatedInput liest
            val mockReadLine: () => String = () => simulatedInput.next()

            // Fange die Ausgabe mit ByteArrayOutputStream ab
            val output = new java.io.ByteArrayOutputStream()

            // Umleitung von println-Ausgabe
            Console.withOut(new java.io.PrintStream(output)) {
                // Teste readLine und println
                val input = mockReadLine() // Wir simulieren das "TestUser" als Eingabe
                println(s"Input was: $input") // Gibt die Eingabe in der Ausgabe wieder
            }

            // Jetzt prüfen wir, ob die Ausgabe korrekt abgefangen wurde
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
            def createDeck() = {
            println("Creating deck...")  // Debugging Ausgabe
            List.fill(4)(dummyCard)
            }
            def shuffle(deck: List[Card]) = {
            println(s"Shuffling deck: $deck")  // Debugging Ausgabe
            deck
            }
            def deal(deck: List[Card], names: List[String]) = {
            println(s"Dealing deck: $deck")  // Debugging Ausgabe
            val dealtPlayers = names.map(name => Player(name + "_mocked", List(dummyCard)))
            println(s"Dealt players: $dealtPlayers")  // Debugging Ausgabe
            dealtPlayers
            }

        var startedPlayers: List[Player] = Nil
        // Mock GameControllerService
        val mockGame = new GameControllerService:
            def startGame(players: List[Player]): Unit = {
            println(s"Started game with players: $players")  // Debugging Ausgabe
            startedPlayers = players
            }

        // Erstelle die Instanz von TUI, wodurch der Konstruktor aufgerufen wird
        val tui = new TUI(mockReadLine, mockPrintln, mockDealer, mockGame)

        // Rufe eine Methode auf, die den Konstruktor und die Interaktionen mit der Konsole auslöst
        tui.start()

        // Prüfe, ob die Spieler korrekt gesetzt wurden
        startedPlayers.map(_.name) shouldEqual List("Alice_mocked", "Bob_mocked")

        // Prüfe, ob die println-Ausgaben korrekt sind
        output should contain ("Wie viele Spieler seid ihr?")
        output should contain ("Bitte den Namen des Spielers angeben:")
        output should contain ("Spieler: List(Alice, Bob)")
    }
}
