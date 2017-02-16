package co.saverin.culverin.engine.processor

import scala.util.parsing.combinator.RegexParsers



trait Processor[T] extends RegexParsers{
  def rule: Parser[T]
}
