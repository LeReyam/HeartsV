package de.htwg.se.Hearts.model

enum Suit:
  case Hearts, Spades, Diamonds, Clubs

  override def toString: String = this match
    case Hearts   => "\u2665"
    case Spades   => "\u2660"
    case Diamonds => "\u2666"
    case Clubs    => "\u2663"