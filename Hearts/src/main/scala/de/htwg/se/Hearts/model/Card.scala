package de.htwg.se.Hearts.model



// A Card has a rank and a suit
case class Card(rank: Rank, suit: Suit) extends Ordered[Card]:
	override def toString: String = s"$rank$suit"

	def compare(that: Card): Int = this.rank.compare(that.rank)



