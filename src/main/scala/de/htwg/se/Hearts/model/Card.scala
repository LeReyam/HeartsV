package de.htwg.se.Hearts.model

import scala.math.Ordered

case class Card(rank: Rank, suit: Suit) extends Ordered[Card] {
  override def toString: String = s"${rank.toString}${suit.toString}"
  override def compare(that: Card): Int = this.rank.compare(that.rank)
}




