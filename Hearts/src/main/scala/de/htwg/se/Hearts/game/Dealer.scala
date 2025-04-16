package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.*

object Dealer:
  def createDeck(): List[Card] =
    for
      suit <- Suit.values.toList
      rank <- Rank.values.toList
    yield Card(rank, suit)

  def shuffleAndDeal(deck: List[Card], playerNames: List[String]): List[Player] =
    val shuffled = util.Random.shuffle(deck)
    val hands = shuffled.grouped(13).toList
    playerNames.zip(hands).map { case (name, hand) => Player(name, hand) }
