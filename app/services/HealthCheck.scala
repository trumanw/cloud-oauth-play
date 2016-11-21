package services

import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import javax.inject._
import scala.concurrent.Future
import scalikejdbc._
import scalikejdbc.config._

import models.{OauthAccessToken, OauthAuthorizationCode, Account, OauthClient}

trait HealthCheck {
  def status(): Future[JsValue]
  def isReadable(): Future[Boolean]
  def isWritable(): Future[Boolean]
}

@Singleton
class PostgresSQLHealthCheck extends HealthCheck {
  // Setup database
  DBs.setupAll()

  override def status(): Future[JsValue] = DB.localTx { implicit session =>
    val totalRowsOauthAccessToken = OauthAccessToken.count().toString()
    val totalRowsOauthAuthorizationCode = OauthAuthorizationCode.count().toString()
    val totalRowsAccount = Account.count().toString()
    val totalRowsOauthClient = OauthClient.count().toString()
    Future.successful(Json.obj("status" -> 200))
  }

  override def isReadable(): Future[Boolean] = DB.readOnly { implicit session =>
    val totalRowsOauthAccessToken = OauthAccessToken.count().toString()
    val totalRowsOauthAuthorizationCode = OauthAuthorizationCode.count().toString()
    val totalRowsAccount = Account.count().toString()
    val totalRowsOauthClient = OauthClient.count().toString()
    Future.successful(true)
  }

  override def isWritable(): Future[Boolean] = DB.localTx { implicit session =>
    Future.successful(true)
  }
}
