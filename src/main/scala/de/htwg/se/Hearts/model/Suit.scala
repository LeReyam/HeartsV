package de.htwg.se.Hearts.model

enum Suit extends Ordered[Suit]:
  case Hearts, Spades, Diamonds, Clubs

  override def compare(that: Suit): Int =
    this.ordinal.compare(that.ordinal)

  override def toString: String = this match
    case Hearts   => "\u2665"
    case Spades   => "\u2660"
    case Diamonds => "\u2666"
    case Clubs    => "\u2663"
