package de.htwg.se.Hearts.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.game.TUI
import scala.io.StdIn.readLine

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TuiSpec extends AnyWordSpec with Matchers {

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
  }
}
}
