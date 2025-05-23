package de.htwg.se.Hearts.model

object PlayerFactory {
  def createPlayer(kind: String, name: String, hand: List[Card]): Player = kind.toLowerCase match {
    case "human" => new HumanPlayer(name, hand)
    case "bot"   => new BotPlayer(name, hand)
    case _       => throw new IllegalArgumentException(s"Unbekannter Spielertyp: $kind")
  }
}
