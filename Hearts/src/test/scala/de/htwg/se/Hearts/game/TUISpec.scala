package de.htwg.se.Hearts.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

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
  }
