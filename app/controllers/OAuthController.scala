package controllers

import models._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, Controller}
import scalikejdbc._
import scalikejdbc.config._

import scala.concurrent.Future
import scalaoauth2.provider._
import scalaoauth2.provider.OAuth2ProviderActionBuilders._

import models.oauth.{OAuthDataHandler, OAuthTokenEndpoint}

@Singleton
class OAuthController @Inject() (dataHandler: OAuthDataHandler) extends Controller with OAuth2Provider {

  implicit val authInfoWrites = new Writes[AuthInfo[Account]] {
    def writes(authInfo: AuthInfo[Account]) = {
      Json.obj(
        "account" -> Json.obj(
          "email" -> authInfo.user.email
        ),
        "clientId" -> authInfo.clientId,
        "redirectUri" -> authInfo.redirectUri
      )
    }
  }

  override val tokenEndpoint = new OAuthTokenEndpoint

  def accessToken = Action.async { implicit request =>
    issueAccessToken(dataHandler)
  }

}
