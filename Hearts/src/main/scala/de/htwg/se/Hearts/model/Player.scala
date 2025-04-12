package de.htwg.se.Hearts.model
import de.htwg.se.Hearts.model.Card
import scala.collection.mutable.ListBuffer

case class Player(name: String, hand: List[Card]){
    var points: Int = 0











    override def toString(): String = {
    val handStr = hand.map(card => s"  - $card").mkString("\n")
    s"$name's hand:\n$handStr"
   }
}
