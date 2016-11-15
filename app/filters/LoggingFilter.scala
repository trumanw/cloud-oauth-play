package filters

import play.api.Logger
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder

import javax.inject.Inject
import akka.stream.Materializer
import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

@Singleton
class LoggingFilter @Inject()(
  implicit override val mat: Materializer,
  exec: ExecutionContext
) extends Filter {
  val runtime = Runtime.getRuntime
  val totalMem = runtime.totalMemory
  // val fmt = ISODateTimeFormat.dateTime()
  // %{MONTHDAY}/%{MONTH}/%{YEAR}:%{TIME} %{INT}
  // [21/Jul/2015:09:19:10 +0000]

  val fmt = new DateTimeFormatterBuilder().
                    appendPattern("dd/MMM/yyyy:HH:mm:ss Z").
                    toFormatter()

  def apply(nextFilter: (RequestHeader) => Future[Result])
          (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    val requestID = UUID.randomUUID().toString
    val requestHeaderWithID = requestHeader.copy(tags = requestHeader.tags + ("X-Request-ID" -> requestID))
    nextFilter(requestHeaderWithID).map { result =>
      val remoteIpAddress: String = {
        // see http://johannburkard.de/blog/programming/java/x-forwarded-for-http-header.html
        val result = requestHeader.headers.get("X-Forwarded-For").map(_.split(",").head).getOrElse(
                       requestHeader.headers.get("Remote_Addr").getOrElse(
                         requestHeader.remoteAddress))
        Logger.debug(s"""requestHeader.headers.get("X-Forwarded-For")=${requestHeader.headers.get("X-Forwarded-For")}
                        |requestHeader.headers.get("Remote_Addr")=${requestHeader.headers.get("Remote_Addr")}
                        |requestHeader.remoteAddress=${requestHeader.remoteAddress}
                        |result=$result""".stripMargin)
        result
      }
      val userName = "-"

      val endTime = System.currentTimeMillis
      val elapsedMillis = endTime - startTime

      val dt = new DateTime(startTime)
      val timeStamp = dt.toString(fmt)

      val requestSummary = {
        val query = if (requestHeader.rawQueryString.nonEmpty) s"?${requestHeader.rawQueryString}" else ""
        val protocol = requestHeader.headers.get("Content-Length").getOrElse("-")
        val httpVersion = requestHeader.version
        s"${requestHeader.method} ${requestHeader.path}$query $httpVersion"
      }

      val statusCode = result.header.status
      val contentLength = result.header.headers.get("Content-Length").getOrElse("-")
      val referrer = requestHeader.headers.get("referer").getOrElse("-")
      val userAgent = requestHeader.headers.get("User-Agent").getOrElse("-")
      val usedMem = (totalMem - runtime.freeMemory) / 1000000
      val freeMem = runtime.freeMemory / 1000000

      val enhancedAccessLogEntry =
          s"""$remoteIpAddress - $userName [$timeStamp] "$requestSummary" $statusCode $contentLength "$referrer" "$userAgent" """

      play.Logger.of("access").info(enhancedAccessLogEntry)
      result.withHeaders("Request-Time" -> elapsedMillis.toString)
    }
  }
}
