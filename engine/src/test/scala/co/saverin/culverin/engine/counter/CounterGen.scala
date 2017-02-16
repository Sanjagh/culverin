package co.saverin.culverin.engine.counter

import akka.actor.{ Actor, ActorRef, ActorRefFactory, ActorSystem, Props }

/** A counter that is guaranteed to accept C as metric */
trait Counter[C] {
  self: Actor =>
}

object CounterGen {
  def gen[T, C](clas: Class[_ <: Counter[C]], f: T => C, name: String)(implicit factory: ActorRefFactory): ActorRef = factory.actorOf(Props(clas, f), name)
}
