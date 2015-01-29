package api

import akka.actor.{ActorRefFactory, ActorSystem}
import api.handler.WidgetHandler
import com.github.vonnagy.service.container.http.routing.RoutedEndpoints
import model.Widget
import spray.http.MediaTypes._

class WidgetService(implicit val system: ActorSystem,
                    actorRefFactory: ActorRefFactory) extends RoutedEndpoints with WidgetHandler {

  // Import the default Json marshaller and un-marshaller
  implicit val marshaller = jsonMarshaller
  implicit val unmarshaller = jsonUnmarshaller[Widget]

  val route = {
    pathPrefix("widgets") {
      get {
        // GET /widgets
        pathEnd {
          respondWithMediaType(`application/json`) {
            // Push the handling to another context so that we don't block
            getWidget()
          }
        } ~
          // GET /widgets/{id}
          path(IntNumber) { id =>
            respondWithMediaType(`application/json`) {
              // Push the handling to another context so that we don't block
              getWidget(Some(id))
            }
          }
      } ~
        // POST /widgets
        post {
          // Simulate the creation of a widget. This call is handled in-line and not through the per-request handler.
          entity(as[Widget]) { widget =>
            respondWithMediaType(`application/json`) {
              // Push the handling to another context so that we don't block
              createWidget(widget)
            }
          }
        } ~
        // PUT /widgets/{id}
        put {
          path(IntNumber) { id =>
            // Simulate the update of a product. This call is handled in-line and not through the per-request handler.
            entity(as[Widget]) { widget =>
              // Push the handling to another context so that we don't block
              updateWidget(id, widget)
            }
          }
        } ~
        // DELETE /widgets/{id}
        delete {
          path(IntNumber) { id =>
            // Delete the widget
            deleteWidget(id)
          }
        }

    }
  }
}