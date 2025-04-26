package de.htwg.se.Hearts.model

class PlayedCard(player: Player, card: Card):
  override def toString: String = s"${player.name}: $card"
  def getPlayer: Player = player
  def getCard: Card = card
