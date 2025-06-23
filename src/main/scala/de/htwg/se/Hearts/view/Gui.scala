package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.controller.GameController
import de.htwg.se.Hearts.model._
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
import scalafx.scene.image.{Image, ImageView}

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
            val nameInput = text.value.trim
            val fallbackName = controller.getInternalPlayerNameStateInfo match {
              case Right((index, _)) => s"Player ${index + 1}"
              case _ => "Player"
            }
            val finalName = if (nameInput.isEmpty) fallbackName else nameInput
            controller.handleInput(finalName)
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

    def opponentView(index: Int, name: String, rotation: Double, nameFirst: Boolean, orientation: String): Region = {
      val nameLbl = nameLabel(name)
      val opponentHand = controller.getSortedHandForPlayer(index % players.length)
      val cardsView = renderOverlappingCards(opponentHand, showFront = false, rotation)

      orientation match {
        case "horizontal" =>
          val nameBox = new VBox {
            alignment = Pos.Center
            children = Seq(nameLbl)
          }

          if (nameFirst)
            HBox.setMargin(nameBox, Insets(0, 20, 0, 0))
          else
            HBox.setMargin(nameBox, Insets(0, 0, 0, 20))

          new HBox {
            id = s"gamePlayer_HBox_${if (rotation > 0) "Left" else "Right"}Player"
            spacing = 10
            alignment = if (rotation > 0) Pos.CenterLeft else Pos.CenterRight
            children = if (nameFirst) Seq(nameBox, cardsView) else Seq(cardsView, nameBox)
          }

        case "vertical" =>
          new VBox {
            id = "gamePlayer_VBox_TopPlayer"
            spacing = 10
            alignment = Pos.TopCenter
            children = Seq(nameLbl, cardsView)
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
      children = Seq(
        new Label("Aktueller Stich:") {
          style = "-fx-font-size: 14px; -fx-text-fill: white;"
        },
        renderPotInLayout(pot)
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
            Seq(
              undoRedoBox,
              opponentView(
                (current + 3) % players.length,
                playerName((current + 3) % players.length),
                180, nameFirst = true, orientation = "vertical"
              )
            )
          else Seq(undoRedoBox)
        }
      }

      left = opponentView(
        (current + 1) % players.length,
        playerName((current + 1) % players.length),
        90, nameFirst = true, orientation = "horizontal"
      )

      right = opponentView(
        (current + 2) % players.length,
        playerName((current + 2) % players.length),
        -90, nameFirst = false, orientation = "horizontal"
      )

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
              renderOverlappingCards(hand, showFront = true),
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

  def renderOverlappingCards(cards: List[Card], showFront: Boolean, rotation: Double = 0): StackPane = {
    val stack = new StackPane {
      alignment = Pos.Center
    }

    val totalOffset = (cards.length - 1) * 20 / 2.0

    for ((card, i) <- cards.zipWithIndex) {
      val imageView = if (showFront) renderCard(card) else renderBack()
      imageView.rotate = rotation

      val offset = i * 20 - totalOffset

      rotation match {
        case 90 | -90 =>
          imageView.translateY = offset
        case _ =>
          imageView.translateX = offset
      }

      val node = if (showFront) {
        new Button {
          graphic = imageView
          onAction = _ => controller.handleInput(i.toString)
          style = "-fx-background-color: transparent;"
        }
      } else imageView

      rotation match {
        case 90 | -90 =>
          node.translateY = offset
        case _ =>
          node.translateX = offset
      }

      stack.children.add(node)
    }

    stack
  }

  def renderBack(): ImageView = new ImageView(
    new Image(getClass.getResourceAsStream("/cards/backside.png"))
  )

  def renderCard(card: Card): ImageView = {
  val path = s"/cards/${card.rank.fileName}_of_${card.suit.fileName}.png"
  val stream = getClass.getResourceAsStream(path)
  if (stream == null) {
    println(s"[FEHLER] Bild nicht gefunden: $path")
    return new ImageView(new Image(getClass.getResourceAsStream("/cards/backside.png")))
  }
  new ImageView(new Image(stream))
  }

  def renderPotInLayout(pot: List[Card]): StackPane = {
    val top = (0.0, -80.0, 180.0)
    val bottom = (0.0, 80.0, 0.0)
    val left = (-80.0, 0.0, 90.0)
    val right = (80.0, 0.0, -90.0)

    val stack = new StackPane {
      alignment = Pos.Center
      prefWidth = 300
      prefHeight = 300
    }

    for ((card, i) <- pot.zipWithIndex) {
      val cardView = renderCard(card)

      val playerCount = controller.getAllPlayers.length

      val (x, y, rotation) = (playerCount, i) match {
        
        case (4, 0) => bottom
        case (4, 1) => left
        case (4, 2) => top
        case (4, 3) => right

        case (3, 0) => bottom
        case (3, 1) => left
        case (3, 2) => right
      }

      cardView.rotate = rotation
      cardView.translateX = x
      cardView.translateY = y
      stack.children.add(cardView)
    }

    stack
  }


  // === Test-Getter Start ===

  def getMainPane: BorderPane = mainPane
  def getCurrentCenterNode: Node = mainPane.center()
  // === Test-Getter Ende ===

}
