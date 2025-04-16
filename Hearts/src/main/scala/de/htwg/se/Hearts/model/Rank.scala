package de.htwg.se.Hearts.model

enum Rank(val value: Int) extends Ordered[Rank]:
  case Two   extends Rank(1)
  case Three extends Rank(2)
  case Four  extends Rank(3)
  case Five  extends Rank(4)
  case Six   extends Rank(5)
  case Seven extends Rank(6)
  case Eight extends Rank(7)
  case Nine  extends Rank(8)
  case Ten   extends Rank(9)
  case Jack  extends Rank(10)
  case Queen extends Rank(11)
  case King  extends Rank(12)
  case Ace   extends Rank(13)

  override def toString: String = this match
    case Two   => "2"
    case Three => "3"
    case Four  => "4"
    case Five  => "5"
    case Six   => "6"
    case Seven => "7"
    case Eight => "8"
    case Nine  => "9"
    case Ten   => "10"
    case Jack  => "J"
    case Queen => "Q"
    case King  => "K"
    case Ace   => "A"

  // Vergleichen für maxBy etc.
  override def compare(that: Rank): Int =
    this.value.compare(that.value)
