package de.htwg.se.Hearts
import de.htwg.se.Hearts.model
import de.htwg.se.Hearts.game.*
import scala.io.StdIn.readLine
import de.htwg.se.Hearts.game.Dealer
import de.htwg.se.Hearts.game.GameController
import scala.compiletime.ops.double

// chcp 65001

@main def runHearts(): Unit =
  val tui = new TUI()
  tui.start()