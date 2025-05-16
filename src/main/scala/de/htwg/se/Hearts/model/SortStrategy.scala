package de.htwg.se.Hearts.model

trait SortStrategy {
  def sort(hand: List[Card]): List[Card]
}

class SortBySuitThenRank extends SortStrategy {
   override def sort(hand: List[Card]): List[Card] =
    hand.sortBy(card => (card.suit.ordinal, card.rank.ordinal))
}

class SortByRankOnly extends SortStrategy {
  override def sort(hand: List[Card]): List[Card] =
    hand.sortBy(_.rank.ordinal)
}

class RandomSort extends SortStrategy {
  override def sort(hand: List[Card]): List[Card] =
    scala.util.Random.shuffle(hand)
}
