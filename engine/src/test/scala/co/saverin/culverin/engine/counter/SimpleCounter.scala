package co.saverin.culverin.engine.counter

import akka.actor.Actor

class SimpleCounter(val f: PartialFunction[Any, Int]) extends Actor with Counter[Int, Int] {

  var counter = 0

  def calc(c: Int): Unit = {
    counter = counter + c
  }
}
