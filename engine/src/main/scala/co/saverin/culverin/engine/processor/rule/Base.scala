package co.saverin.culverin.engine.processor.rule

import scala.util.parsing.combinator.RegexParsers

trait Base extends RegexParsers {

  val int: Parser[Int] = """\d+""".r ^^ { _.toInt }
  val long: Parser[Long] = """\d+""".r ^^ { _.toLong }
  val float: Parser[Float] = """\d+\.\d+""".r ^^ { _.toFloat }
  val double: Parser[Double] = """\d+\.\d+""".r ^^ { _.toDouble }
  val ip: Parser[String] = """\d\d?\d?\.\d\d?\d?\.\d\d?\d?\.\d\d?\d?""".r ^^ { _.toString }
  /** Just alphabet */
  val word: Parser[String] = """[a-zA-Z]+""".r ^^ { _.toString }
  /** Alphanumeric with digits and underline */
  val identifier: Parser[String] = """[a-zA-Z0-9_]+""".r ^^ { _.toString }
  val boolean: Parser[Boolean] = """[Tt]rue|[Ff]alse""".r ^^ { _.toString.toLowerCase.toBoolean }
  /** Alphanumeric with digits, dash and underline */
  val id: Parser[String] = """[a-zA-Z0-9_-]""".r ^^ { _.toString }

  
}
