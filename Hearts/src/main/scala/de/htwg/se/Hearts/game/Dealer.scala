package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.*

def createDeck(): List[Card] =
for {
    suit <- Suit.values.toList
    rank <- Rank.values.toList
} yield Card(rank, suit)

def shuffleAndDeal(deck: List[Card], playerNames: List[String]): List[Player] = {
    val shuffled = scala.util.Random.shuffle(deck)
    val handSize = deck.length / playerNames.length
    val hands = shuffled.grouped(handSize).toList

    playerNames.zip(hands).map {
        case (name, hand) => Player(name, hand)
    }
}
