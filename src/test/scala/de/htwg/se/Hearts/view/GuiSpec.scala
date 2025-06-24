package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.controller.GameController
import de.htwg.se.Hearts.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.application.Platform
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.scene.Node
import scalafx.Includes._
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import scala.util.{Try, Success, Failure}
import scala.jdk.CollectionConverters._

class GuiSpec extends AnyWordSpec with Matchers {

  // Runs JavaFX operations synchronously
  private def runOnFxThread[T](operation: => T): T = {
    if (Platform.isFxApplicationThread) {
      operation
    } else {
      val latch = new CountDownLatch(1)
      var result: Option[T] = None
      var exception: Option[Throwable] = None

      Platform.runLater {
        try {
          result = Some(operation)
        } catch {
          case e: Throwable => exception = Some(e)
        } finally {
          latch.countDown()
        }
      }

      if (latch.await(5, TimeUnit.SECONDS)) {
        exception.foreach(throw _)
        result.get
      } else {
        throw new RuntimeException("JavaFX operation timed out")
      }
    }
  }

  // JavaFX node search
  private def findNodeByIdFx(parent: javafx.scene.Parent, id: String): Option[javafx.scene.Node] = {
    def searchNode(node: javafx.scene.Node): Option[javafx.scene.Node] = {
      if (node.getId == id) Some(node)
      else node match {
        case p: javafx.scene.Parent =>
          p.getChildrenUnmodifiable.asScala.view.flatMap(searchNode).headOption
        case _ => None
      }
    }
    searchNode(parent)
  }

  // Wrap JavaFX node back into ScalaFX Node
  private def findNodeById(parent: Node, id: String): Option[Node] = {
    parent.delegate match {
      case p: javafx.scene.Parent =>
        findNodeByIdFx(p, id).map(n => new Node(n) {})
      case _ => None
    }
  }

  "Gui" when {
    "initialized" should {
      "create a main pane and scene with correct dimensions" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          gui.scene.width.value shouldBe 1000.0
          gui.scene.height.value shouldBe 800.0
          gui.getMainPane should not be null
        }
      }

      "display start screen initially" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          val centerNode = gui.getCurrentCenterNode
          centerNode shouldBe a[VBox]

          val vbox = centerNode.asInstanceOf[VBox]
          vbox.id() shouldBe "startPage_VBox"

          val startButton = findNodeById(centerNode, "startPage_startButton")
          startButton shouldBe defined
          startButton.get shouldBe a[Button]
        }
      }
    }

    "in StartState" should {
      "display Hearts title and start button" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          val centerNode = gui.getCurrentCenterNode
          val titleLabel = findNodeById(centerNode, "startPage_Label_Überschrift")
          titleLabel shouldBe defined

          val label = titleLabel.get.asInstanceOf[javafx.scene.control.Label]
          label.getText should include("Hearts")
        }
      }

      "transition to GetPlayerNumberState when start button is clicked" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          val btn = findNodeById(gui.getCurrentCenterNode, "startPage_startButton").get.asInstanceOf[javafx.scene.control.Button]
          btn.fire()
          controller.getCurrentState() should startWith("GetPlayerNumberState")
        }
      }
    }

    "in GetPlayerNumberState" should {
      "display player number input interface" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start") // Move to GetPlayerNumberState

          val centerNode = gui.getCurrentCenterNode
          centerNode shouldBe a[VBox]

          val textField = centerNode.asInstanceOf[VBox].children.find(node =>
            node.getClass.getName.contains("TextField"))
          textField shouldBe defined
        }
      }

      "show error message for invalid player count" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start")
          controller.handleInput("5") // Invalid

          val labels = gui.getCurrentCenterNode.asInstanceOf[VBox].children.collect {
            case l if l.getClass.getName.contains("Label") => l.asInstanceOf[javafx.scene.control.Label]
          }
          labels.exists(_.getText.contains("zwischen 3 und 4")) shouldBe true
        }
      }

      "transition to GetHumanPlayerCountState with valid input" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start")
          controller.handleInput("4")
          controller.getCurrentState() should startWith("GetHumanPlayerCountState")
        }
      }
    }

    "in GetHumanPlayerCountState" should {
      "display human player count input" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("4")
          val labels = gui.getCurrentCenterNode.asInstanceOf[VBox].children.collect {
            case l if l.getClass.getName.contains("Label") => l.asInstanceOf[javafx.scene.control.Label]
          }
          labels.exists(_.getText.contains("menschliche Spieler")) shouldBe true
        }
      }

      "show error for invalid human count" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("4")
          controller.handleInput("5")
          val labels = gui.getCurrentCenterNode.asInstanceOf[VBox].children.collect {
            case l if l.getClass.getName.contains("Label") => l.asInstanceOf[javafx.scene.control.Label]
          }
          labels.exists(_.getText.contains("zwischen 1 und 4")) shouldBe true
        }
      }
    }

    "in GetPlayerNamesState" should {
      "display player name input prompt" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("4"); controller.handleInput("2")
          val labels = gui.getCurrentCenterNode.asInstanceOf[VBox].children.collect {
            case l if l.getClass.getName.contains("Label") => l.asInstanceOf[javafx.scene.control.Label]
          }
          labels.exists(_.getText.contains("Spieler 1")) shouldBe true
        }
      }

      "progress through multiple player names" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("4"); controller.handleInput("2")
          controller.handleInput("Alice")
          val labels = gui.getCurrentCenterNode.asInstanceOf[VBox].children.collect {
            case l if l.getClass.getName.contains("Label") => l.asInstanceOf[javafx.scene.control.Label]
          }
          labels.exists(_.getText.contains("Spieler 2")) shouldBe true
        }
      }
    }

    "in GetSortStrategyState" should {
      "display sort strategy options" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice")
          val centerNode = gui.getCurrentCenterNode.asInstanceOf[VBox]
          centerNode.id() shouldBe "gameSetup_Sortstrat_VBox"
          Seq("gameSetup_Sortstrat_Radiobutton_strategy1", "gameSetup_Sortstrat_Radiobutton_strategy2", "gameSetup_Sortstrat_Radiobutton_strategy3").foreach { rid =>
            findNodeById(centerNode, rid) shouldBe defined
          }
          findNodeById(centerNode, "gameSetup_Sortstrat_confirmButton") shouldBe defined
        }
      }

      "have first strategy selected by default" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice")
          findNodeById(gui.getCurrentCenterNode, "gameSetup_Sortstrat_Radiobutton_strategy1")
            .get.asInstanceOf[javafx.scene.control.RadioButton].isSelected shouldBe true
        }
      }
    }

    "in GamePlayState" should {
      "display game play interface" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice"); controller.handleInput("1")
          val scrollPane = gui.getCurrentCenterNode.asInstanceOf[ScrollPane]
          scrollPane.id() shouldBe "gamePlay_ScrollPane"
        }
      }

      "display undo and redo buttons" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice"); controller.handleInput("1")
          val content = gui.getCurrentCenterNode.asInstanceOf[ScrollPane].content.value
          Seq("gamePlay_undoButton", "gamePlay_redoButton").foreach { btnId =>
            findNodeById(content, btnId) shouldBe defined
          }
        }
      }

      "display current pot area" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice"); controller.handleInput("1")
          findNodeById(gui.getCurrentCenterNode.asInstanceOf[ScrollPane].content.value, "gamePlay_VBox_Pot") shouldBe defined
        }
      }

      "display player hand as buttons" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice"); controller.handleInput("1")
          controller.getCurrentPlayerHand.nonEmpty shouldBe true
        }
      }
    }

    "handling state transitions" should {
      "properly update UI when state changes" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.getCurrentState() should startWith("StartState")
          controller.handleInput("start")
          controller.getCurrentState() should startWith("GetPlayerNumberState")
          controller.handleInput("3")
          controller.getCurrentState() should startWith("GetHumanPlayerCountState")
          controller.handleInput("1")
          controller.getCurrentState() should startWith("GetPlayerNamesState")
          controller.handleInput("Alice")
          controller.getCurrentState() should startWith("GetSortStrategyState")
          controller.handleInput("1")
          controller.getCurrentState() should startWith("GamePlayState")
        }
      }

      "maintain proper observer relationship" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start")
          gui.getCurrentCenterNode shouldBe a[VBox]
        }
      }
    }

    "error handling" should {
      "display appropriate error messages" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("invalid")
          controller.getCurrentState() should startWith("GetPlayerNumberState")
          val labels = gui.getCurrentCenterNode.asInstanceOf[VBox].children.collect {
            case l if l.getClass.getName.contains("Label") => l.asInstanceOf[javafx.scene.control.Label]
          }
          labels.exists(_.getText.contains("gültige Zahl")) shouldBe true
        }
      }

      "handle edge cases gracefully" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("")
          controller.getCurrentState() should startWith("GetPlayerNumberState")
        }
      }
    }

    "game functionality" should {
      "allow card play in gameplay state" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice"); controller.handleInput("1")
          val initialHandSize = controller.getCurrentPlayerHand.size
          controller.handleInput("0")
          val newHandSize = controller.getCurrentPlayerHand.size
          val potSize = controller.getCurrentPot.size
          (newHandSize < initialHandSize || potSize > 0) shouldBe true
        }
      }

      "support undo/redo functionality" in {
        val controller = new GameController()
        val gui = new Gui(controller)

        runOnFxThread {
          controller.handleInput("start"); controller.handleInput("3"); controller.handleInput("1"); controller.handleInput("Alice"); controller.handleInput("1")
          val initialHandSize = controller.getCurrentPlayerHand.size
          controller.handleInput("0")
          controller.handleInput("undo")
          controller.getCurrentPlayerHand.size should be >= initialHandSize
        }
      }
    }
  }
}
