package api.handler

import akka.actor.ActorSystem
import com.github.vonnagy.service.container.http.routing.Rejection.{DuplicateRejection, NotFoundRejection}
import db.WidgetPersistence
import model.Widget
import spray.http.StatusCodes._
import spray.httpx.marshalling.{Marshaller, ToResponseMarshaller}
import spray.routing._

trait WidgetHandler {

  implicit val system: ActorSystem
  val persistor = WidgetPersistence()

  def getWidget[T](id: Option[Int] = None)(implicit marshaller: Marshaller[T]): Route = ctx => {
    id match {
      case Some(widgetId) =>
        // Return a specific widget
        val data = persistor.getWidgets(id)
        if (data.size > 0) {
          implicit def x = marshaller.asInstanceOf[Marshaller[Widget]]
          ctx.complete(data.head)
        } else {
          ctx.reject(NotFoundRejection("The widget could not be located"))
        }

      case _ =>
        // Return all widgets
        implicit def x = marshaller.asInstanceOf[Marshaller[Iterable[Widget]]]
        ctx.complete(persistor.getWidgets(None))
    }
  }

  def updateWidget(id: Int, widget: Widget): Route = ctx => {
    // Update a specific widget
    persistor.updateWidget(id, widget) match {
      case true =>
        ctx.complete(NoContent)
      case false =>
        ctx.reject(NotFoundRejection("The widget could not be located"))
    }
  }

  def createWidget[T](widget: Widget)(implicit marshaller: Marshaller[Widget]): Route = ctx => {
    // Create a specific widget
    persistor.createWidget(widget) match {
      case Some(w) =>
        ctx.complete(w)(ToResponseMarshaller.fromMarshaller(Created))
      case None =>
        ctx.reject(DuplicateRejection("The widget could not be located"))
    }
  }

  def deleteWidget(id: Int): Route = ctx => {
    // Delete a specific widget
    persistor.deleteWidget(id) match {
      case true =>
        ctx.complete(NoContent)
      case false =>
        ctx.reject(NotFoundRejection("The widget could not be located"))
    }
  }

}