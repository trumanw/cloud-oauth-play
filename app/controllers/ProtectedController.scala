package controllers

import javax.inject._

import play.api.mvc.Controller
import models.oauth.OAuthDataHandler
import be.objectify.deadbolt.scala._

import scala.concurrent.{Future, ExecutionContext}
import scalaoauth2.provider.OAuth2Provider

@Singleton
class ProtectedController @Inject() (actionBuilder: ActionBuilders, dataHandler: OAuthDataHandler)(implicit ec: ExecutionContext) extends Controller with OAuth2Provider {

  def tokenPresent = actionBuilder.SubjectPresentAction().defaultHandler() { implicit request =>
    Future(Ok)
  }

}
