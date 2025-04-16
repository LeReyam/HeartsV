package de.htwg.se.Hearts.model
import de.htwg.se.Hearts.model.Card
import scala.collection.mutable.ListBuffer



case class Player(name: String, var hand: List[Card]) {
  var points: Int = 0  // Punkte für das Spiel

  // Spieler legt eine Karte ab
  def playCard(card: Card): Unit = {
    hand = hand.filterNot(_ == card)  // Entfernt die gespielte Karte aus der Hand
    println(s"$name spielt: $card")
  }

  def showHandString(): String = {
  s"$name's hand: ${hand.mkString(", ")}\n"
  }


}

