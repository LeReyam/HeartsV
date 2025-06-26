/*package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.controller.GameController
import org.scalatest.BeforeAndAfter
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scalafx.application.Platform
import scalafx.scene.Node
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.Promise
import scalafx.Includes._

def runOnFxThread[T](body: => T): T = {
  if (Platform.isFxApplicationThread) {
    body
  } else {
    val p = Promise[T]()
    Platform.runLater {
      try {
        p.success(body)
      } catch {
        case t: Throwable => p.failure(t)
      }
    }
    Await.result(p.future, Duration.Inf)
  }
}

class GuiSpec extends AnyWordSpec with Matchers with BeforeAndAfter {

  var controller: GameController = _
  var gui: Gui = _

  before {
    controller = new GameController()
    gui = runOnFxThread(new Gui(controller))
  }

  "Gui" should {

    "show the start screen initially" in {
      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.layout.VBox]
      centerNode.id.value shouldBe "startPage_VBox"

      val vbox = centerNode.asInstanceOf[scalafx.scene.layout.VBox]
      // Convert children from JavaFX to ScalaFX
      val childrenScalaFx = vbox.children.map(jfxNode2sfx)

      val label = childrenScalaFx.head
      label shouldBe a[scalafx.scene.control.Label]
      label.asInstanceOf[scalafx.scene.control.Label].text.value should include("Hearts")
    }

    "show player number input screen after controller state changes" in {
      controller.handleInput("start")
      runOnFxThread(gui.update())

      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.layout.VBox]

      val vbox = centerNode.asInstanceOf[scalafx.scene.layout.VBox]
      val childrenScalaFx = vbox.children.map(jfxNode2sfx)

      val labels = childrenScalaFx.collect {
        case l: scalafx.scene.control.Label => l.text.value
      }
      labels.exists(_.contains("Bitte eine Spieleranzahl eingeben.")) shouldBe true
    }

    "show human player count input screen after controller state changes" in {
      controller.handleInput("start")
      controller.handleInput("4")
      runOnFxThread(gui.update())

      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.layout.VBox]

      val vbox = centerNode.asInstanceOf[scalafx.scene.layout.VBox]
      val childrenScalaFx = vbox.children.map(jfxNode2sfx)

      val labels = childrenScalaFx.collect {
        case l: scalafx.scene.control.Label => l.text.value
      }
      labels.exists(_.contains("Wie viele menschliche Spieler?")) shouldBe true
    }

    "show player name inputs screen after controller state changes" in {
      controller.handleInput("start")
      controller.handleInput("4")
      controller.handleInput("1")
      runOnFxThread(gui.update())

      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.layout.VBox]

      val vbox = centerNode.asInstanceOf[scalafx.scene.layout.VBox]
      val childrenScalaFx = vbox.children.map(jfxNode2sfx)

      val labels = childrenScalaFx.collect {
        case l: scalafx.scene.control.Label => l.text.value
      }
      labels.exists(_.contains("Gib den Namen für Spieler")) shouldBe true
    }

    "show sort strategy choice screen after controller state changes" in {
      controller.handleInput("start")
      controller.handleInput("4")
      controller.handleInput("1")
      controller.handleInput("P1")
      runOnFxThread(gui.update())

      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.layout.VBox]
      centerNode.id.value shouldBe "gameSetup_Sortstrat_VBox"

      val vbox = centerNode.asInstanceOf[scalafx.scene.layout.VBox]
      val childrenScalaFx = vbox.children.map(jfxNode2sfx)

      val label = childrenScalaFx.head
      label shouldBe a[scalafx.scene.control.Label]
      label.asInstanceOf[scalafx.scene.control.Label].text.value should include("Sortierstrategie")
    }

    "show gameplay screen after controller state changes" in {
      controller.handleInput("start")
      controller.handleInput("4")
      controller.handleInput("1")
      controller.handleInput("P1")
      controller.handleInput("1")
      runOnFxThread(gui.update())

      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.control.ScrollPane]
      centerNode.id.value shouldBe "gamePlay_ScrollPane"
    }

    "show game over screen after controller state changes" in {
      controller.handleInput("start")
      controller.handleInput("4")
      controller.handleInput("1")
      controller.handleInput("P1")
      controller.handleInput("1")
      for (_ <- 0 to 52) {
        controller.handleInput("0")
      }
      runOnFxThread(gui.update())

      val centerNode = runOnFxThread(gui.getMainPane.center())
      centerNode shouldBe a[scalafx.scene.layout.VBox]

      val vbox = centerNode.asInstanceOf[scalafx.scene.layout.VBox]
      val childrenScalaFx = vbox.children.map(jfxNode2sfx)

      val labels = childrenScalaFx.collect {
        case l: scalafx.scene.control.Label => l.text.value
      }
      labels.exists(_.contains("Spiel beendet")) shouldBe true
    }
  }
}
*/