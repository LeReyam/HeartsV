package de.htwg.se.Hearts.view

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*

class GameViewSpec extends AnyWordSpec with Matchers {

  "GameView" should {

    "render the player number input prompt correctly" in {
      val controller = new GameController()
      val view = new GameView(controller)
      val output = view.generateOutputStringGetPlayerNumberState(controller)
      output should include("HEARTS GAME SETUP")
    }

    "render the player name input prompt correctly" in {
      val controller = new GameController()
      val view = new GameView(controller)

      controller.handleInput("3")  // 3 Spieler
      controller.handleInput("2")  // 2 menschliche Spieler

      val output = view.generateOutputStringGetPlayerNamesState(controller)
      output should include("HEARTS GAME SETUP")
      output should (include("Gib den Namen für Spieler") or include("Bot_"))
    }

    "render the game play state correctly" in {
      val controller = new GameController()
      val view = new GameView(controller)

      controller.handleInput("2")
      controller.handleInput("2")
      controller.handleInput("P1")
      controller.handleInput("P2")
      controller.handleInput("1") // Sortierungsstrategie
      val output = view.generateOutputStringGamePlayState(controller)
      output should include("Current Pot:")
      output should include("P1")
      output should include("P2")
    }

    "render game over with correct scores" in {
      val controller = new GameController()
      val view = new GameView(controller)

      controller.handleInput("2")
      controller.handleInput("2")
      controller.handleInput("P1")
      controller.handleInput("P2")
      controller.handleInput("1")

      for (_ <- 0 until 52) controller.handleInput("0")

      val output = view.generateStateStringGameOverState(controller)
      output should include("GAME OVER")
      controller.getAllPlayers.foreach(player =>
        output should include(player.name)
      )

    }
  }
  "stay in GetPlayerNamesState if not all names entered yet" in {
  val controller = new GameController()
  controller.handleInput("3") // total players
  controller.handleInput("2") // human players
  controller.handleInput("Alice") // one name

  controller.getCurrentState() should include("GetPlayerNamesState")
}

  "render game over when no players exist" in {
    val controller = new GameController()
    val view = new GameView(controller)

    val output = view.generateStateStringGameOverState(controller)
    output should include("Keine Spieler vorhanden.")
  }
}
