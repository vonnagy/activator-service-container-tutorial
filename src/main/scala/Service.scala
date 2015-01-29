import api.WidgetService
import com.github.vonnagy.service.container.ContainerBuilder

object Service extends App {

  // Here we establish the container and build it while
  // applying extras.
  val service = new ContainerBuilder()
    // Register the API routes
    .withRoutes(classOf[WidgetService]).build

  service.start
}