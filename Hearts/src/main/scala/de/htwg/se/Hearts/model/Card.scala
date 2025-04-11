package de.htwg.se.Hearts.model

//Wert + Farbe
case class Card(rank: String, suit: Suit):
	override def toString(): String = s"$rank${suit.symbol}"

	
enum Suit(val symbol: String):
	case Hearts extends Suit("\u2665")
	case Diamonds extends Suit("\u2666")
	case Spades extends Suit("\u2660")
	case Clubs extends Suit("\u2663")