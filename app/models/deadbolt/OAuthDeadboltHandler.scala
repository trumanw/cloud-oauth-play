package models.deadbolt

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.{AuthenticatedRequest, DeadboltHandler, DynamicResourceHandler}
import com.google.inject.Inject
import play.api.mvc.{Request, Result, Results}
import models._
import models.oauth.{OAuthDataHandler}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaoauth2.provider.{AuthInfo, OAuth2ProtectedResourceProvider}

class OAuthDeadboltHandler @Inject() (dataHandler: OAuthDataHandler) extends DeadboltHandler with OAuth2ProtectedResourceProvider {

  val dynamicHandler: Option[DynamicResourceHandler] = Option.empty

  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future(None)

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = Future.successful(dynamicHandler)

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] =
    request.subject match {
      case Some(subject) => Future.successful(request.subject)
      case _ => protectedResource.handleRequest(request, dataHandler).map {
        case Left(e) => None
        case Right(authInfo: AuthInfo[Account]) => Some(new OAuthSubject(authInfo))
      }
    }

  override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = Future(Results.Unauthorized)
}
