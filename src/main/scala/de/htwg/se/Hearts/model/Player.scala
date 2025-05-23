package de.htwg.se.Hearts.model
import scala.collection.mutable.ListBuffer

trait Player {
  def name: String
  def hand: List[Card]
  var points: Int
  def removeCard(card: Card): Unit
}

class HumanPlayer(val name: String, private var _hand: List[Card]) extends Player {
  var points: Int = 0
  def hand: List[Card] = _hand
  def removeCard(card: Card): Unit = {
    _hand = _hand.filterNot(_ == card)
  }
}

case class BotPlayer(val name: String, private var _hand: List[Card]) extends Player {
  var points: Int = 0
  def hand: List[Card] = _hand
  def removeCard(card: Card): Unit = _hand = _hand.filterNot(_ == card)
}