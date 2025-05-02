package de.htwg.se.Hearts.model

import scala.math.Ordered

// A Card has a rank and a suit
case class Card(rank: Rank, suit: Suit) extends Ordered[Card] {
  override def toString: String = s"${rank.toString}${suit.toString}"

  // Compare cards based on their rank only
  override def compare(that: Card): Int = this.rank.compare(that.rank)
}




