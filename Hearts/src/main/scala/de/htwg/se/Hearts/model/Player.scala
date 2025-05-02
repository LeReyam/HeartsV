package de.htwg.se.Hearts.model
import scala.collection.mutable.ListBuffer



class Player(val name: String, private var _hand: List[Card]) extends Observable {
  def hand: List[Card] = _hand

  def playCard(card: Card): Unit = {
    _hand = _hand.filterNot(_ == card)
    //println(s"$name spielt: $card")
    notifyObservers()
  }
}

