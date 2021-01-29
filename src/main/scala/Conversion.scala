/** A conversion to SI units.
 *
 * @param unitName SI units to which this unit should be converted
 * @param multiplicationFactor multiplication factor needed to convert to SI units
 */
case class Conversion(unitName: String, multiplicationFactor: Double) {
  /** Return a new conversion that represents this one times the given one.  */
  def *(c: Conversion): Conversion =
    Conversion(s"$unitName*${c.unitName}", multiplicationFactor * c.multiplicationFactor)

  /** Return a new conversion that represents this one divided by the given one.  */
  def /(c: Conversion): Conversion =
    Conversion(s"$unitName/${c.unitName}", multiplicationFactor / c.multiplicationFactor)

  /** Return a new conversion that represents this one with its unit name wrapped in parentheses. */
  def wrapParens: Conversion = Conversion(s"($unitName)", multiplicationFactor)
}

/** Scope for Circe implicits. */
object Conversion {
  import cats.Applicative
  import cats.effect.Sync
  import io.circe.generic.semiauto._
  import io.circe._
  import org.http4s.circe._
  import org.http4s._

  implicit val conversionDecoder: Decoder[Conversion] = deriveDecoder[Conversion]
  implicit def conversionEntityDecoder[F[_]: Sync]: EntityDecoder[F, Conversion] = jsonOf

  implicit val conversionEncoder: Encoder[Conversion] = deriveEncoder[Conversion]
  implicit def conversionEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Conversion] = jsonEncoderOf
}
