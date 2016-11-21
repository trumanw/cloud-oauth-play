package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext}

import services.HealthCheck

@Singleton
class HealthCheckController @Inject() (healthCheck: HealthCheck)(implicit ec: ExecutionContext) extends Controller {

  def simplyHealthCheck = Action.async {
    healthCheck.status().map { status =>
      Ok(status)
    }
  }

  def detailedHealthCheck = Action.async {
    healthCheck.status().map { status =>
      Ok(status)
    }
  }

}
