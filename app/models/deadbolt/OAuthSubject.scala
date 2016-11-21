package models.deadbolt

import be.objectify.deadbolt.scala.models.{Subject, Permission, Role}
import models.Account

import scalaoauth2.provider.AuthInfo

class OAuthSubject(authInfo: AuthInfo[Account]) extends Subject {

  val scopes = OAuthScope(authInfo.scope.get)

  override def identifier: String = authInfo.user.id.toString

  override def permissions: List[Permission] = scopes

  override def roles: List[Role] = scopes
  
}
