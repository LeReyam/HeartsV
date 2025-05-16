package de.htwg.se.Hearts.view

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*

import scala.collection.mutable.ListBuffer

class GameViewSpec extends AnyWordSpec with Matchers {

  "generateOutputStringGetPlayerNumberState" should {
    "return the correct prompt for player number input" in {
      val controller = new GameController()
      val view = new GameView(controller)
      val result = view.generateOutputStringGetPlayerNumberState(controller)

      result should include("HEARTS GAME SETUP")
      result should include("Enter the number of players (2-4):")
    }
  }

  "generateOutputStringGetPlayerNamesState" should {
    "return the correct prompt for player name input" in {
      val controller = new GameController()
      val view = new GameView(controller)
      val result = view.generateOutputStringGetPlayerNamesState(controller)

      result should include("HEARTS GAME SETUP")
      result should include("Enter name for Player:")
    }
  }

  "generateOutputStringGamePlayState" should {
    "return a formatted game state string including player info and pot" in {
      val controller = new GameController()
      val view = new GameView(controller)
      controller.handleInput("2")
      controller.handleInput("P1")
      controller.handleInput("P2")
      val result = view.generateOutputStringGamePlayState(controller)
      result should include("P1")
      result should include("P2")
      Dealer.createDeck().foreach { s =>
        result should include(s.toString)
      }
      result should include ("Empty")
      controller.handleInput("1")
      controller.handleInput("0")
      val result2 = view.generateOutputStringGamePlayState(controller)
      result2 should not include ("Empty")
      result2 should include regex """Current Pot:\s*\|\s*.{3}\s*\|"""
    }
  }

  "generateStateStringGameOverState" should {
    "return final scores sorted by lowest points" in {
      val controller = new GameController()
      val view = new GameView(controller)
      controller.handleInput("2")
      controller.handleInput("P1")
      controller.handleInput("P2")
      for(i <- 0 to 52){
        controller.handleInput("0")
      }
      val result = view.generateStateStringGameOverState(controller)
      result should include("GAME OVER")
      result should include("P1")
      result should include("P2")
    }
  }
}
