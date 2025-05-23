package de.htwg.se.Hearts.controller
import de.htwg.se.Hearts.model.*

object Dealer:
  def createDeck(): List[Card] =
    for
      suit <- Suit.values.toList
      rank <- Rank.values.toList
    yield Card(rank, suit)

  def shuffle(deck: List[Card]): List[Card] =
    util.Random.shuffle(deck)


  def deal(deck: List[Card],playerNames: List[String]): Game =
    val leftovers = deck.length /playerNames.length
    var reducedDeck = deck.filterNot(_ == deck.take(leftovers))

    val hands = deck.grouped(deck.length/playerNames.length).toList
    val players = playerNames.zip(hands).map { case (name, hand) => Player(name, hand) }
    Game(players)
