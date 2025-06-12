package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.controller.GameController
import de.htwg.se.Hearts.model.Observer
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.text.Font
import scalafx.scene.input._

class Gui(controller: GameController) extends Observer {
  val Placeholder = "[🂠🂠🂠🂠🂠🂠🂠🂠]\nPlaceholder"
  private val mainPane = new BorderPane()

  def scene: Scene = new Scene(1000, 800) {
  root = mainPane

}


  controller.addObserver(this)
  showStartScreen()
  private def showStartScreen(): Unit = {
    mainPane.center = new VBox {
      spacing = 20
      alignment = Pos.Center
      children = Seq(
        new Label("\u2665 Hearts \u2665") {
          font = Font("Arial", 36)
          style = "-fx-text-fill: darkred;"
        },
        new Button("Neues Spiel starten") {
          onAction = _ => controller.handleInput("start")
        }
      )
    }
  }
  override def update(): Unit = {
    Platform.runLater {
      val stateText = controller.getCurrentState()
      if (stateText.startsWith("GetPlayerNumberState")) {
        renderInput("Anzahl Spieler (3-4):", "4")
      } else if (stateText.startsWith("GetHumanPlayerCountState")) {
        renderInput("Wie viele Menschen spielen mit?", "2")
      }else if (stateText.startsWith("GetPlayerNamesState")) {
        controller.getInternalPlayerNameStateInfo match {
          case Right((currentIndex, totalHumans)) =>
            renderInput(s"Name des ${currentIndex + 1}. Spielers:", "Spieler")
          case _ =>
            renderInput("Spielername:", "Spieler")
        }
      } else if (stateText.startsWith("GetSortStrategyState")) {
        renderSortStrategyChoice()
      } else if (stateText.startsWith("GamePlayState")) {
        renderGamePlay()
      } else if (stateText.startsWith("GameOverState")) {
        renderGameOver()
      } else {
        renderMessage("Unbekannter Zustand", stateText)
      }
    }
  }

  private def renderInput(prompt: String, default: String): Unit = {
    val inputField = new TextField {
      promptText = default
    }

    val button = new Button("Bestätigen") {
      onAction = _ => {
        val input = inputField.text.value.trim
        if (input.nonEmpty) controller.handleInput(input)
      }
    }

    inputField.onAction = _ => button.fire()

    mainPane.center = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label(prompt),
        inputField,
        button
      )
    }
  }

  private def renderSortStrategyChoice(): Unit = {
  val strategy1 = new RadioButton("1: Nach Farbe und Rang sortieren")
  val strategy2 = new RadioButton("2: Nur nach Rang sortieren")
  val strategy3 = new RadioButton("3: Zufällige Reihenfolge")

  val toggleGroup = new ToggleGroup()
  Seq(strategy1, strategy2, strategy3).foreach(_.toggleGroup = toggleGroup)
  strategy1.selected = true

  val confirmButton = new Button("Sortierstrategie wählen") {
    onAction = _ => {
      val input =
        if (strategy1.selected.value) "1"
        else if (strategy2.selected.value) "2"
        else if (strategy3.selected.value) "3"
        else "1"

      controller.handleInput(input)
    }
  }

  strategy1.onKeyPressed = key => {
    if (key.code == KeyCode.Enter) {
      controller.handleInput("1")
    }
  }
  strategy2.onKeyPressed = key => {
    if (key.code == KeyCode.Enter) {
      controller.handleInput("2")
    }
  }
  strategy3.onKeyPressed = key => {
    if (key.code == KeyCode.Enter) {
      controller.handleInput("3")
    }
  }

  val content = new VBox {
    spacing = 10
    alignment = Pos.Center
    children = Seq(
      new Label("Wähle eine Sortierstrategie:") { font = Font("Arial", 16) },
      strategy1,
      new Label("→ Sortiert nach Farbe (Kreuz, Pik, Herz, Karo) und Rang"),
      strategy2,
      new Label("→ Sortiert nur nach Rang (2-A), Farbe egal"),
      strategy3,
      new Label("→ Karten werden zufällig angezeigt"),
      confirmButton
    )
  }

  mainPane.center = content
}

  private def renderGamePlay(): Unit = {
    val players = controller.getAllPlayers
    val current = controller.getCurrentPlayerIndex
    val currentPlayer = players(current)
    val hand = controller.getSortedHand
    val pot = controller.getCurrentPot.toList

    def playerName(index: Int): String = players(index % players.size).name

    def nameLabel(name: String): Label = new Label(name) {
      wrapText = true
      maxWidth = 100
      maxHeight = 40
      alignment = Pos.Center
      style = "-fx-font-size: 12px; -fx-alignment: center;"
    }

    def opponentView(name: String, rotation: Double, nameFirst: Boolean, orientation: String): Region = {
      val nameLbl = nameLabel(name)
      val cardLbl = new Label("[🂠🂠🂠🂠🂠]") {
        rotate = rotation
        style = "-fx-font-size: 16px;"
      }

      orientation match {
        case "horizontal" =>
          new HBox {
            spacing = 10
            alignment = if (rotation > 0) Pos.CenterLeft else Pos.CenterRight
            children = if (nameFirst) Seq(nameLbl, cardLbl) else Seq(cardLbl, nameLbl)
          }

        case "vertical" =>
          new VBox {
            spacing = 10
            alignment = Pos.TopCenter
            children = Seq(nameLbl, cardLbl)
          }
      }
    }

    val undoButton = new Button("Undo") {
      onAction = _ => controller.handleInput("undo")
    }

    val redoButton = new Button("Redo") {
      onAction = _ => controller.handleInput("redo")
    }

    val undoRedoBox = new HBox {
      spacing = 10
      alignment = Pos.TopRight
      padding = Insets(10)
      children = Seq(undoButton, redoButton)
    }

    val centerPotView = new VBox {
      alignment = Pos.Center
      spacing = 10
      padding = Insets(0, 0, 40, 0)
      children = Seq(
        new Label("Aktueller Stich:") {
          style = "-fx-font-size: 14px;"
        },
        new HBox {
          spacing = 20
          alignment = Pos.Center
          children = pot.map(card => new Label(card.toString))
        }
      )
    }

    val layout = new BorderPane {
      padding = Insets(40)

      top = new VBox {
        spacing = 5
        children = {
          if (players.size == 4)
            Seq(undoRedoBox, opponentView(playerName(current + 3), 180, nameFirst = true, orientation = "vertical"))
          else
            Seq(undoRedoBox)
        }
      }

      left = opponentView(playerName(current + 1), 90, nameFirst = true, orientation = "horizontal")
      right = opponentView(playerName(current + 2), -90, nameFirst = false, orientation = "horizontal")
      center = centerPotView

      bottom = new StackPane {
        children = Seq(
          new VBox {
            alignment = Pos.BottomCenter
            spacing = 10
            padding = Insets(10)
            children = Seq(
              new HBox {
                spacing = 8
                alignment = Pos.Center
                children = hand.zipWithIndex.map { case (card, index) =>
                  new Button(card.toString) {
                    onAction = _ => controller.handleInput(index.toString)
                  }
                }
              },
              new Label("Du bist dran:") {
                style = "-fx-font-size: 14px;"
              },
              nameLabel(currentPlayer.name)
            )
          }
        )
      }
    }

    mainPane.center = new ScrollPane {
      content = layout
      hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
      vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
      fitToWidth = true
      fitToHeight = true
      padding = Insets(10)
    }
  }



  private def renderGameOver(): Unit = {
    val players = controller.getAllPlayers

    def formatPlayer(p: de.htwg.se.Hearts.model.Player): String =
      s"${p.name}  Score: ${p.points}"

    val sorted = players.sortBy(_.points)

    val ranks = sorted.foldLeft(List.empty[(Int, String, Int)]) {
      case (Nil, player) =>
        List((1, formatPlayer(player), player.points))
      case (acc, player) =>
        val (_, _, lastPoints) = acc.last
        val lastRank = acc.last._1
        val rank = if (player.points == lastPoints) lastRank else acc.size + 1
        acc :+ (rank, formatPlayer(player), player.points)
    }

    val scoreboard = new VBox {
      spacing = 5
      alignment = Pos.Center
      children = ranks.map { case (rank, line, _) =>
        new Label(s"$rank. $line")
      }
    }

    val newGameBtn = new Button("Neues Spiel") {
      onAction = _ => controller.handleInput("start")
    }

    val exitBtn = new Button("Beenden") {
      onAction = _ => Platform.exit()
    }

    val buttons = new HBox {
      spacing = 20
      alignment = Pos.Center
      children = Seq(newGameBtn, exitBtn)
    }

    mainPane.center = new VBox {
      spacing = 20
      alignment = Pos.Center
      children = Seq(
        new Label("🏁 Spiel beendet!") {
          font = Font("Arial", 24)
        },
        new Label("🏆 Endstand:") {
          font = Font("Arial", 18)
        },
        scoreboard,
        buttons
      )
    }
  }

  private def renderMessage(title: String, text: String): Unit = {
    mainPane.center = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label(title) { font = Font("Arial", 24) },
        new Label(text)
      )
    }
  }
}
