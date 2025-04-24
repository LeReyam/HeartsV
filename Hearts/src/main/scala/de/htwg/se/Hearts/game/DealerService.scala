package de.htwg.se.Hearts.game

import de.htwg.se.Hearts.model.Card
import de.htwg.se.Hearts.model.Player

trait DealerService:
  def createDeck(): List[Card]
  def shuffle(deck: List[Card]): List[Card]
  def deal(deck: List[Card], names: List[String]): List[Player]

