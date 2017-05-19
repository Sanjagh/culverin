package co.saverin.culverin.engine.draft

import akka.NotUsed
import akka.stream.scaladsl.Flow
import co.saverin.culverin.engine.processor.rule.BaseRule

import scala.util.parsing.combinator.RegexParsers

/**
  * @author S.Hosein Ayat
  */
object ProcessorDraft extends BaseRule with RegexParsers {


  def parse[T](p: Parser[T]): Flow[String, T, NotUsed] = Flow[String].map { s => parse(p, s) }.filter(_.successful).map(_.get)

}



