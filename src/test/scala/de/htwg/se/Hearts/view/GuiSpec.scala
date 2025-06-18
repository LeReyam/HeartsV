package de.htwg.se.Hearts.view

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller.GameController
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField, RadioButton}
import scalafx.scene.layout.{VBox, HBox}
import javafx.embed.swing.JFXPanel

class GuiSpec extends AnyWordSpec with Matchers {

  new JFXPanel() // JavaFX initialisieren
  val controller = new GameController()
  val gui = new Gui(controller)

  "A Gui" should {

    "create a valid Scene" in {
      gui.scene shouldBe a[Scene]
    }

    "start with title screen containing label and start button" in {
      val center = gui.getCurrentCenterNode.asInstanceOf[VBox]
      val children = center.children.toList

      val labelExists = children.exists { node =>
        node.isInstanceOf[javafx.scene.control.Label] &&
          new Label(node.asInstanceOf[javafx.scene.control.Label]).text.value.contains("Hearts")
      }

      val buttonExists = children.exists { node =>
        node.isInstanceOf[javafx.scene.control.Button] &&
          new Button(node.asInstanceOf[javafx.scene.control.Button]).text.value.contains("Neues Spiel starten")
      }

      labelExists shouldBe true
      buttonExists shouldBe true
    }

    "show name input after clicking 'Neues Spiel starten'" in {
      controller.handleInput("start")
      controller.handleInput("4")
      controller.handleInput("2")

      val center = gui.getCurrentCenterNode.asInstanceOf[VBox]
      val children = center.children.toList

      val nameLabelExists = children.exists { node =>
        node.isInstanceOf[javafx.scene.control.Label] &&
          new Label(node.asInstanceOf[javafx.scene.control.Label]).text.value.contains("Name des")
      }

      val textFieldExists = children.exists(_.isInstanceOf[javafx.scene.control.TextField])

      val confirmButtonExists = children.exists { node =>
        node.isInstanceOf[javafx.scene.control.Button] &&
          new Button(node.asInstanceOf[javafx.scene.control.Button]).text.value.contains("Bestätigen")
      }

      nameLabelExists shouldBe true
      textFieldExists shouldBe true
      confirmButtonExists shouldBe true
    }

    "show sort strategy selection after entering player names" in {
      controller.handleInput("Spieler 1")
      controller.handleInput("Spieler 2")

      val center = gui.getCurrentCenterNode.asInstanceOf[VBox]
      val children = center.children.toList

      val radioButtonExists = children.exists(_.isInstanceOf[javafx.scene.control.RadioButton])

      val strategyButtonExists = children.exists { node =>
        node.isInstanceOf[javafx.scene.control.Button] &&
          new Button(node.asInstanceOf[javafx.scene.control.Button]).text.value == "Sortierstrategie wählen"
      }

      radioButtonExists shouldBe true
      strategyButtonExists shouldBe true
    }

    "show game UI after selecting sort strategy" in {
      controller.handleInput("1")
      val center = gui.getCurrentCenterNode
      val buttons = center.lookupAll(".button")
      buttons should not be empty
    }

    "show game over screen after simulated game" in {
      controller.handleInput("0")
      controller.handleInput("0")
      controller.handleInput("0")
      controller.handleInput("gameOver") // Spielende simulieren

      val center = gui.getCurrentCenterNode.asInstanceOf[VBox]
      val children = center.children.toList

      val gameOverTextExists = children.exists { node =>
        node.isInstanceOf[javafx.scene.control.Label] &&
          new Label(node.asInstanceOf[javafx.scene.control.Label]).text.value.contains("Spiel beendet")
      }

      val buttonTexts = children.flatMap { node =>
        if (node.isInstanceOf[javafx.scene.layout.HBox]) {
          val hbox = new HBox(node.asInstanceOf[javafx.scene.layout.HBox])
          hbox.children.toList.collect {
            case b if b.isInstanceOf[javafx.scene.control.Button] =>
              new Button(b.asInstanceOf[javafx.scene.control.Button]).text.value
          }
        } else Nil
      }

      gameOverTextExists shouldBe true
      buttonTexts should contain("Neues Spiel")
      buttonTexts should contain("Beenden")
    }
  }
}
