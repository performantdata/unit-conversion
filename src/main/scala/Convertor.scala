/** Convertor for the unit string language. */
object Convertor {
  import fastparse._, NoWhitespace._, Parsed.{Failure,Success}

  private def unit[_:P] =
    P( StringIn("minute", "hour", "day", "degree", "arcminute", "arcsecond", "hectare", "litre", "tonne").! )
      .map(CommonUnit.withName(_).conversion)

  private def symbol[_:P] =
    P( StringIn("min", "h", "d", "°",  "'",  "\"", "ha", "L", "t").! ).map(CommonUnit.symbolToUnit(_).conversion)

  private def parens[_:P] = P( "(" ~/ divMul ~ ")" ).map(_.wrapParens)
  private def factor[_:P]: P[Conversion] = P( parens | unit | symbol )

  private def divMul[_:P] = P( factor ~ (CharIn("*/").! ~/ factor).rep ).map {
    case (z, seq) => seq.foldLeft(z){
      case (a, ("*", b)) => a * b
      case (a, ("/", b)) => a / b
    }
  }

  private def expr[_:P] = P( divMul ~ End )

  /** Convert the given units string to SI units.
   *
   * @return the conversion, or an error message on failure
   */
  def convertUnits(unitString: String): Either[String, Conversion] = parse(unitString, expr(_)) match {
    case Success(value, _) => Right(value)
    case f: Failure => Left(f.trace().longAggregateMsg)
  }
}
