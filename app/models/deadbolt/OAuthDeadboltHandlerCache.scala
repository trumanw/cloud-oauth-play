package models.deadbolt

import javax.inject.{Inject, Singleton}

import be.objectify.deadbolt.scala.cache.HandlerCache
import be.objectify.deadbolt.scala.{DeadboltHandler, HandlerKey}

@Singleton
class OAuthDeadboltHandlerCache @Inject() (defaultHandler: OAuthDeadboltHandler) extends HandlerCache {

  override def apply(): DeadboltHandler = defaultHandler

  override def apply(handlerKey: HandlerKey): DeadboltHandler = defaultHandler
}
