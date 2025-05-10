package de.htwg.se.Hearts.util

import scala.collection.mutable.Queue

class MockUserInterface extends UserInterface {
  private val inputQueue: Queue[String] = Queue()
  private val outputBuffer: StringBuilder = new StringBuilder

  // Methode zum Hinzufügen von simulierten Benutzereingaben für Tests
  def addInput(input: String): Unit = {
    inputQueue.enqueue(input)
  }

  // Methode zum Abrufen aller Ausgaben für Überprüfungen in Tests
  def getOutput: String = outputBuffer.toString()

  // Methode zum Zurücksetzen der Ausgabe für mehrere Tests
  def clearOutput(): Unit = {
    outputBuffer.clear()
  }

  override def readInput(): String = {
    if (inputQueue.isEmpty) {
      "0" // Standardwert, falls keine Eingabe vorhanden ist
    } else {
      inputQueue.dequeue()
    }
  }

  override def displayMessage(message: String): Unit = {
    outputBuffer.append(message)
  }
}