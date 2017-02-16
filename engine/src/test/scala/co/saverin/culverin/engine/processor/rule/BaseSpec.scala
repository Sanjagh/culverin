package co.saverin.culverin.engine.processor.rule

import org.scalatest._

class BaseSpec extends FlatSpec with Matchers with Inside {

  import RuleTester._

  "base rules" should "pass ip test" in {
    val result = parse(ip, "192.168.1.1")
    inside(result) {
      case Success(ip, _) => ip shouldBe "192.168.1.1"
    }
  }

  it should "pass boolean test" in {
    var result = parse(boolean, "True")
    inside(result) {
      case Success(b, _) => b shouldBe true
    }

    result = parse(boolean, "false")
    inside(result) {
      case Success(b, _) => b shouldBe false
    }
  }

  it should "pass primitie types test" in {
    val sample = "1 2222 2.5 2.5 true"
    val result = parse(baseTest, sample)
    inside(result) {
      case Success(r, _) => r shouldBe sample
    }
  }
}

object RuleTester extends Base {

  val baseTest: Parser[String] = int ~ long ~ float ~ double ~ boolean ^^ { case i ~ l ~ f ~ d ~ b => s"$i $l $f $d $b" }
}
