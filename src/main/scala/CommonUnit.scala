import enumeratum.EnumEntry.Lowercase
import enumeratum._

/** A "widely used" unit of measurement.
 *
 * @param symbol short string used in place the full name
 * @param conversion conversion to SI units
 */
sealed abstract class CommonUnit(val symbol: String, val conversion: Conversion)
  extends EnumEntry with Lowercase

object CommonUnit extends Enum[CommonUnit] {
//  import SiUnit._

  override val values: IndexedSeq[CommonUnit] = findValues

  /** Map from a unit's symbol to the unit itself. */
  val symbolToUnit: Map[String, CommonUnit] = values.map(u => u.symbol -> u).toMap

  case object Minute extends CommonUnit("min", Conversion("s", 60))
  case object Hour   extends CommonUnit("h",   Conversion("s", 3600))
  case object Day    extends CommonUnit("d",   Conversion("s", 86400))
  case object Degree    extends CommonUnit("°",  Conversion("rad", Math.PI / 180))
  case object Arcminute extends CommonUnit("'",  Conversion("rad", Math.PI / 10800))
  case object Arcsecond extends CommonUnit("\"", Conversion("rad", Math.PI / 648000))
  case object Hectare extends CommonUnit("ha", Conversion("m²", 10000))
  case object Litre   extends CommonUnit("L",  Conversion("m³", 0.001))
  case object Tonne   extends CommonUnit("t",  Conversion("kg", 1000))
}
