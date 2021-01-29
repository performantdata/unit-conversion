import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.{HttpApp, HttpRoutes}

import scala.concurrent.ExecutionContext.global

/** Our HTTP server. */
object Main extends IOApp {
  import org.http4s.implicits._
  private val dsl = new Http4sDsl[IO]{}
  import dsl._

  private object UnitsParamMatcher extends OptionalQueryParamDecoderMatcher[String]("units")

  val routes: HttpApp[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "units" / "si" :? UnitsParamMatcher(name) => name match {
      case Some(n) => Convertor.convertUnits(n) match {
        case Right(value) => Ok(value)
        case Left(value) => BadRequest(value)
      }
      case None =>  BadRequest("\"units\" parameter missing")
    }
  }.orNotFound

  private val httpApp = Logger.httpApp(logHeaders = true, logBody = true)(routes)

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
