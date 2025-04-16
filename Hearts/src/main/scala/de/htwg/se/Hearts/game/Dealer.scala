package de.htwg.se.Hearts.game
import  de.htwg.se.Hearts
import de.htwg.se.Hearts.model.*

object Dealer:
  def createDeck(): List[Card] =
    for
      suit <- Suit.values.toList
      rank <- Rank.values.toList
    yield Card(rank, suit)

  def shuffleAndDeal(deck: List[Card], playerNames: List[String]): List[Player] =
    var shuffled = util.Random.shuffle(deck)
    if (playerNames.length == 3){
      shuffled = shuffled.filterNot(_ == shuffled.head)
    }
    val hands = shuffled.grouped(deck.length/playerNames.length).toList
    playerNames.zip(hands).map { case (name, hand) => Player(name, hand) }
