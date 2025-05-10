package de.htwg.se.Hearts.util

trait UserInterface {
  def readInput(): String
  def displayMessage(message: String): Unit
}

// Standard-Implementierung für die Konsole
class ConsoleUserInterface extends UserInterface {
  override def readInput(): String = scala.io.StdIn.readLine().trim
  override def displayMessage(message: String): Unit = print(message)
}