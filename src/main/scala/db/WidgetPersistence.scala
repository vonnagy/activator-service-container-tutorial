package db

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorSystem
import com.github.vonnagy.service.container.health.{HealthInfo, HealthState, RegisteredHealthCheck}
import com.github.vonnagy.service.container.metrics.Counter
import model.Widget

import scala.collection.JavaConversions._
import scala.concurrent.Future

object WidgetPersistence {

  def apply()(implicit system: ActorSystem) = new WidgetPersistence()

  // This is a pretend persistence resource
  val nextId = {
    var i = 100;
    () => {
      i += 1;
      i
    }
  }
  val widgets = new ConcurrentHashMap[Int, Widget]()
}

class WidgetPersistence(implicit val system: ActorSystem) extends RegisteredHealthCheck {

  import system.dispatcher

  /**
   * Fetch the health for this registered checker.
   * @return returns a future to the health information
   */
  def getHealth: Future[HealthInfo] = Future {
    HealthInfo("db", HealthState.OK, s"The persistence is running and currently contains ${WidgetPersistence.widgets.size} Widgets")
  }

  val get = Counter("db.widgets.get")
  val getAll = Counter("db.widgets.get.all")
  val create = Counter("db.widgets.create")
  val update = Counter("db.widgets.update")
  val delete = Counter("db.widgets.delete")

  def getWidgets[T](id: Option[Int] = None): Seq[Widget] = {
    get.incr
    id match {
      case Some(id) =>
        // Return a specific widget
        if (WidgetPersistence.widgets.containsKey(id)) {
          Seq(WidgetPersistence.widgets.get(id))
        } else {
          Nil
        }
      case _ =>
        // Return all widgets
        WidgetPersistence.widgets.values.toSeq
    }
  }

  def updateWidget(id: Int, widget: Widget): Boolean = {
    update.incr
    // Update a specific widget
    WidgetPersistence.widgets.containsKey(id) match {
      case true =>
        WidgetPersistence.widgets.put(id, widget.copy(id = Some(id)))
        true
      case false =>
        false
    }
  }

  def createWidget[T](widget: Widget): Option[Widget] = {
    create.incr
    // Create a specific widget
    WidgetPersistence.widgets.values.count(w => w.name.equals(widget.name)) match {
      case 0 =>
        val newId = WidgetPersistence.nextId()
        val newWidget = widget.copy(id = Some(newId))
        WidgetPersistence.widgets.put(newId, newWidget)
        println("PUT")
        Some(newWidget)
      case _ =>
        None
    }
  }

  def deleteWidget(id: Int): Boolean = {
    delete.incr
    // Delete a specific widget
    WidgetPersistence.widgets.containsKey(id) match {
      case true =>
        WidgetPersistence.widgets.remove(id)
        true
      case false =>
        false
    }
  }
}
