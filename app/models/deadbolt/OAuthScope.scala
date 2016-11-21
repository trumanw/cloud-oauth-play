package models.deadbolt

import be.objectify.deadbolt.scala.models.{Permission, Role}

class OAuthScope(scope: String) extends Role with Permission {

  override def name: String = scope

  override def value: String = scope
  
}

object OAuthScope {

  def apply(scopeString: String): List[OAuthScope] = scopeString.split(",").toList.map(s => new OAuthScope(s.trim))

}
