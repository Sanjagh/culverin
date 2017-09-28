package co.saverin.culverin.agent.api

/**
  * @author S.Hosein Ayat
  */
trait FileScanner {

  def registerSingleTime(name: String, path: String, endCallback: String => Unit) : Unit

}
