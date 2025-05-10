package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObservableSpec extends AnyWordSpec with Matchers {
  "An Observable" should {
    "notify observers when the state changes" in {
      val observable = new Observable {}
      var notified = false

      observable.addObserver(() => notified = true)
      observable.notifyObservers()
      notified shouldBe true
    }
  }

  "An Observable" should {
    "be able to add and remove observers" in {
      val observable = new Observable {}
      var notified1, notified2 = false

      val observer1 = new Observer { def update(): Unit = notified1 = true }
      val observer2 = new Observer { def update(): Unit = notified2 = true }

      observable.addObserver(observer1)
      observable.addObserver(observer2)
      observable.notifyObservers()

      notified1 shouldBe true
      notified2 shouldBe true
    }

    "not notify removed observers" in {
      val observable = new Observable {}
      var notified = false
    }
  }
}