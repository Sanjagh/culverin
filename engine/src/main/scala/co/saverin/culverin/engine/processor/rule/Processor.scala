package co.saverin.culverin.engine.processor.rule

import akka.NotUsed
import akka.stream.scaladsl.Flow

import scala.util.parsing.combinator.RegexParsers

/**
  * @author S.Hosein Ayat
  */
trait Processor {
  self : RegexParsers =>

  def modularize[T](p: Parser[T]): Flow[String, T, NotUsed] = Flow[String].map { s => parse(p, s) }.filter(_.successful).
    map(_.get)
}
