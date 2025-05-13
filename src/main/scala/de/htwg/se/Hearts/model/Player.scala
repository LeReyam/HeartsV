package de.htwg.se.Hearts.model
import scala.collection.mutable.ListBuffer



class Player(val name: String, private var _hand: List[Card]) {
  var points = 0
  def hand: List[Card] = _hand

  def removeCard(card: Card): Unit = {
    _hand = _hand.filterNot(_ == card)
  }
}

