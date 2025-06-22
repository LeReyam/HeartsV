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
import scalafx.scene.Node
import scala.util.{Try, Success, Failure}

class Gui(controller: GameController) extends Observer {
  val Placeholder = "[🂠🂠🂠🂠🂠🂠🂠🂠]\nPlaceholder"
   val mainPane = new BorderPane(){
    id = "mainPane"
   }

  def scene: Scene = new Scene(1000, 800) {
  root = mainPane

}


  controller.addObserver(this)
  showStartScreen()
  private def showStartScreen(): Unit = {
    mainPane.center = new VBox {
      spacing = 20
      alignment = Pos.Center
      id = "startPage_VBox"
      children = Seq(
        new Label("\u2665 Hearts \u2665") {
          font = Font("Arial", 36)
          style = "-fx-text-fill: darkred;"
          id = "startPage_Label_Überschrift"
        },
        new Button("Neues Spiel starten") {
          id = "startPage_startButton"
          onAction = _ => controller.handleInput("start")

        }
      )
    }
  }
  override def update(): Unit = {
    Platform.runLater {
      controller.getCurrentState() match {
        case state if state.startsWith("StartState") =>
          showStartScreen()
        case state if state.startsWith("GetPlayerNumberState") =>
          showPlayerNumberInput()
        case state if state.startsWith("GetHumanPlayerCountState") =>
          showHumanPlayerCountInput()
        case state if state.startsWith("GetPlayerNamesState") =>
          showPlayerNameInputs()
        case state if state.startsWith("GetSortStrategyState") =>
          renderSortStrategyChoice()

        case state if state.startsWith("GamePlayState") =>
          showGamePlayScreen()
        case state if state.startsWith("GameOverState") =>
          showGameOverScreen()
        case _ =>
          println("Unbekannter State in GUI")
      }
    }
  }

  private def showPlayerNumberInput(): Unit = {
    val errorLabel = new Label {
      id = ""
      style = "-fx-text-fill: red;"
      text = controller.getLastPlayerCountTry match {
        case Failure(_: IndexOutOfBoundsException) =>
          "Spieleranzahl muss zwischen 3 und 4 liegen."
        case Failure(_: NumberFormatException) =>
          "Bitte gib eine gültige Zahl ein."
        case Failure(e) =>
          s"Fehler: ${e.getMessage}"
        case Success(_) => ""
      }
    }

    mainPane.center = new VBox {
      id = ""
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label("Bitte eine Spieleranzahl eingeben."),
        new Label("Möglich sind 3 oder 4:"),
        new TextField {
          promptText = "Anzahl (3 oder 4)"
          onAction = handle {
            controller.handleInput(text.value.trim)
          }
        },
        errorLabel
      )
    }
  }


  private def showHumanPlayerCountInput(): Unit = {
    val maxPlayers: Int = controller.getLastPlayerCountTry match {
      case Success(count) => count
      case _ => 4 // Fallback, falls keine Info verfügbar ist
    }

    val errorLabel = new Label {
      style = "-fx-text-fill: red;"
      text = controller.getLastHumanCountTry match {
        case Failure(_: IndexOutOfBoundsException) =>
          s"Zahl muss zwischen 1 und $maxPlayers liegen."
        case Failure(_: NumberFormatException) =>
          "Bitte gib eine gültige Zahl ein."
        case Failure(e) =>
          s"Fehler: ${e.getMessage}"
        case Success(_) => ""
      }
    }

    mainPane.center = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        new Label("Wie viele menschliche Spieler?"),
        new TextField {
          promptText = "z. B. 2"
          onAction = handle {
            controller.handleInput(text.value.trim)
          }
        },
        errorLabel
      )
    }
  }


  private def showPlayerNameInputs(): Unit = {
    val promptLabel = new Label {
      text = controller.getInternalPlayerNameStateInfo match {
        case Right((index, _)) => s"Gib den Namen für Spieler ${index + 1} ein:"
        case Left(_)           => "Gib einen Spielernamen ein:"
      }
    }

    mainPane.center = new VBox {
      spacing = 10
      alignment = Pos.Center
      children = Seq(
        promptLabel,
        new TextField {
          promptText = "Name"
          onAction = handle {
            controller.handleInput(text.value.trim)

          }
        }
      )
    }
  }

  private def showGamePlayScreen(): Unit = {
    renderGamePlay()
  }

  private def showGameOverScreen(): Unit = {
    renderGameOver()
  }


  private def renderInput(prompt: String, default: String): Unit = {
    val inputField = new TextField {
      promptText = default
    }

    val button = new Button("Bestätigen") {
      id = "Button_Bestätigen"
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
  val strategy1 = new RadioButton("1: Nach Farbe und Rang sortieren"){
    id = "gameSetup_Sortstrat_Radiobutton_strategy1"
  }
  val strategy2 = new RadioButton("2: Nur nach Rang sortieren"){
    id = "gameSetup_Sortstrat_Radiobutton_strategy2"
  }
  val strategy3 = new RadioButton("3: Zufällige Reihenfolge"){
    id = "gameSetup_Sortstrat_Radiobutton_strategy3"
  }

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
    id = "gameSetup_Sortstrat_confirmButton"
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
    id = "gameSetup_Sortstrat_VBox"
    spacing = 10
    alignment = Pos.Center
    children = Seq(
      new Label("Wähle eine Sortierstrategie:") { font = Font("Arial", 16)
      id = "gameSetup_Sortstrat_Label"
      },
      strategy1,
      strategy2,
      strategy3,
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
      id = "gamePlay_Label_Playername"
      wrapText = true
      maxWidth = 100
      maxHeight = 40
      alignment = Pos.Center
      style = "-fx-font-size: 12px; -fx-alignment: center;"
    }

    def opponentView(name: String, rotation: Double, nameFirst: Boolean, orientation: String): Region = {
      val nameLbl = nameLabel(name)
      val cardLbl = new Label("[🂠🂠🂠🂠🂠]") {
        id = "gamePlay_Label_PlaceholderCardsOponents"
        rotate = rotation
        style = "-fx-font-size: 16px;"
      }

      orientation match {
        case "horizontal" =>
          new HBox {
            id = "gamePlayer_HBox_"
              + (if (rotation > 0) "Left" else "Right")
              + "Player"

            spacing = 10
            alignment = if (rotation > 0) Pos.CenterLeft else Pos.CenterRight
            children = if (nameFirst) Seq(nameLbl, cardLbl) else Seq(cardLbl, nameLbl)
          }

        case "vertical" =>
          new VBox {
            spacing = 10
            alignment = Pos.TopCenter
            children = Seq(nameLbl, cardLbl)
            id = "gamePlayer_VBox_TopPlayer"
          }
      }
    }

    val undoButton = new Button("Undo") {
      id = "gamePlay_undoButton"
      onAction = _ => controller.handleInput("undo")
    }

    val redoButton = new Button("Redo") {
      id = "gamePlay_redoButton"
      onAction = _ => controller.handleInput("redo")
    }

    val undoRedoBox = new HBox {
      id = "gamePlay_HBox_undoredo"
      spacing = 10
      alignment = Pos.TopRight
      padding = Insets(10)
      children = Seq(undoButton, redoButton)
    }

    val centerPotView = new VBox {
      id = "gamePlay_VBox_Pot"
      alignment = Pos.Center
      spacing = 10
      padding = Insets(0, 0, 40, 0)
      children = Seq(
        new Label("Aktueller Stich:") {
          style = "-fx-font-size: 14px;"
        },
        new HBox {
          id = "gamePlay_HBox_PotDisplayCards"
          spacing = 20
          alignment = Pos.Center
          children = pot.map(card => new Label(card.toString))
        }
      )
    }

    val layout = new BorderPane {
      id = "gamePlay_BorderPane"
      padding = Insets(40)

      top = new VBox {
        id = "gamePlay_VBox_undoredoPosition"
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
        id = "gamePlay_StackPane_AktivePlayer"
        children = Seq(
          new VBox {
            id = "gamePlay_VBox_AktivePlayer"
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
                    id = "gamePlay_Button_HandkartButton"
                  }
                }
              },
              new Label("Du bist dran:") {
                style = "-fx-font-size: 14px;"
                id = "gamePlay_Label_AktivePlayerTurn"
              },
              nameLabel(currentPlayer.name)
            )
          }
        )
      }
    }

    mainPane.center = new ScrollPane {
      id = "gamePlay_ScrollPane"
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
      onAction = _ => controller.restartGame()
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
  // === Test-Getter Start ===

  def getMainPane: BorderPane = mainPane
  def getCurrentCenterNode: Node = mainPane.center()
  // === Test-Getter Ende ===

}
