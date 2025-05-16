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
      val result = generateOutputStringGetPlayerNumberState(controller)

      result should include("HEARTS GAME SETUP")
      result should include("Enter the number of players (2-4):")
    }
  }

  "generateOutputStringGetPlayerNamesState" should {
    "return the correct prompt for player name input" in {
      val controller = new GameController()
      val result = generateOutputStringGetPlayerNamesState(controller)

      result should include("HEARTS GAME SETUP")
      result should include("Enter name for Player:")
    }
  }

  "generateOutputStringGamePlayState" should {
    "return a formatted game state string including player info and pot" in {

    }
  }

  "generateStateStringGameOverState" should {
    "return final scores sorted by lowest points" in {
      
    }
  }
}
