package co.saverin.culverin.engine.counter

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}



/** A counter that is guaranteed to accept C as metric and O as output */
trait Counter[C, O] {
  self: Actor =>

  def f: PartialFunction[Any, C]
  
  def calc(c: C): Unit

  def receive: Receive = f andThen calc

  
}

object CounterGen {
  def gen[C](clas: Class[_ <: Counter[C, _]], f: PartialFunction[Any,C], name: String)(implicit factory: ActorRefFactory): ActorRef = factory.actorOf(Props(clas, f), name)
}
