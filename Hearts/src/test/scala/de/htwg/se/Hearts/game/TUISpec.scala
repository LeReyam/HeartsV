package de.htwg.se.Hearts.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TuiSpec extends AnyWordSpec with Matchers {

  "The TUI" should {
    "read player names correctly" in {
      val input =
        """2
          |Alice
          |Bob
          |""".stripMargin

      val inStream = new ByteArrayInputStream(input.getBytes())
      val outStream = new ByteArrayOutputStream()

      Console.withIn(inStream) {
        Console.withOut(new PrintStream(outStream)) {
          val tui = new TUI()
          val result = tui.getPlayerNames()

          result shouldEqual List("Alice", "Bob")
          outStream.toString should include ("Wie viele Spieler seid ihr?")
          outStream.toString should include ("Bitte den Namen des Spielers angeben:")
        }
      }
    }
    "display played cards correctly" in {
      val outStream = new ByteArrayOutputStream()

      Console.withOut(new PrintStream(outStream)) {
        val tui = new TUI()
        val playedCards = List(
          PlayedCard(Player("Alice", List.empty[Card]), Card(Rank.Queen, Suit.Hearts)),
          PlayedCard(Player("Bob", List.empty[Card]), Card(Rank.King, Suit.Diamonds))
        )
        tui.displayPlayedCards(playedCards)

        val output = outStream.toString
        output should include("Alice: Q\u2665")
        output should include("Bob: K\u2666")
      }
    }
  }
}
