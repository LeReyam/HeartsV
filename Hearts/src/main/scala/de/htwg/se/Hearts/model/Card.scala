package de.htwg.se.Hearts.model



// A Card has a rank and a suit
case class Card(rank: Rank, suit: Suit) {
  override def toString: String = s"${rank.toString.head}${suit.toString.head}"
}




