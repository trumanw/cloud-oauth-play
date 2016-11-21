import com.google.inject.AbstractModule
import services.{PostgresSQLHealthCheck, HealthCheck}
import models.deadbolt.OAuthDeadboltHandlerCache
import be.objectify.deadbolt.scala.cache.HandlerCache


/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    // Set PostgresSQLHealthCheck as the implementation for HealthCheck.
    bind(classOf[HealthCheck]).to(classOf[PostgresSQLHealthCheck])
    // Deadbolt cache handler
    bind(classOf[HandlerCache]).to(classOf[OAuthDeadboltHandlerCache])
  }

}
