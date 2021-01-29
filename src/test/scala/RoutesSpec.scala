import org.http4s.{Query, Request, Status, Uri}
import org.scalatest.freespec.AnyFreeSpec

/** Tests for the routes on our service. */
class RoutesSpec extends AnyFreeSpec {
  "The routes" - {

    "when given a single unit name" - {
      "should return the correct conversion" in {
        val c = succeed("degree")
        assert(c.unitName == "rad")
        assertSpec(Math.PI / 180, c.multiplicationFactor)
      }
    }

    "when given a unit string in parentheses" - {
      "should return the conversion in parentheses" in {
        assert(succeed("(°)").unitName == "(rad)")
      }
    }

    "when given a unit string with * and /" - {
      "should return the corresponding SI units in the conversion" in {
        val c = succeed("ha*°/h")
        assert(c.unitName == "m²*rad/s")
        assertSpec(10000 * Math.PI / 180 / 3600, c.multiplicationFactor)
      }
    }

    "when given a unit string with multiple levels of parentheses" - {
      "should return the corresponding SI units in the parentheses" in {
        val c = succeed("(litre/(\"/t))")
        assert(c.unitName == "(m³/(rad/kg))")
        assertSpec(0.001 / (Math.PI / 648000 / 1000), c.multiplicationFactor)
      }
    }

  }

  /** Call the routes with the given units string. */
  private def run(units: String) =
    Main.routes.run(Request(uri = Uri(path = "/units/si", query = Query.fromPairs("units" -> s"$units"))))
      .unsafeRunSync()

  /** Call the routes with the given units string, expecting success. */
  private def succeed(units: String) = {
    val resp = run(units)
    assert(resp.status == Status.Ok)
    resp.as[Conversion].unsafeRunSync()
  }

  /** Assert that the actual value is within specification of the expected value. */
  private def assertSpec(expected: Double, actual: Double) =
    assert(expected == 0 || Math.abs(expected - actual) / expected <= 1e-14,
      s"\nExpected :$expected\nActual   :$actual")
}
