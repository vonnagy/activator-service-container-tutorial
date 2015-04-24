package api

import com.github.vonnagy.service.container.http.routing.RoutingHandler
import model.Widget
import org.specs2.mutable.SpecificationLike
import spray.http.HttpHeaders.{Accept, `Content-Type`}
import spray.http.StatusCodes._
import spray.http.{ContentTypes, MediaTypes}
import spray.testkit.Specs2RouteTest

class WidgetServiceSpec extends Specs2RouteTest with RoutingHandler with SpecificationLike {

  sequential
  implicit val fact = system
  val service = new WidgetService

  import service.marshaller

  "The widget routing infrastructure should support" should {

    "a call to create a widget at /widgets" in {
      Post("/widgets", Widget(None, "foo")).withHeaders(Accept(MediaTypes.`application/json`), `Content-Type`(ContentTypes.`application/json`)) ~> sealRoute(service.route) ~> check {
        contentType === ContentTypes.`application/json`
        status === Created
      }
    }

    "a call to fetch all widgets at /widgets" in {
      Get("/widgets") ~> service.route ~> check {
        mediaType === MediaTypes.`application/json`
        status === OK
      }
    }

    "a call to fetch a widget at /widgets/{id}" in {
      Get("/widgets/101") ~> sealRoute(service.route) ~> check {
        mediaType === MediaTypes.`application/json`
        status === OK
      }
    }

    "a call to fetch a widget at /widgets/{id} with an invalid number" in {
      Get("/widgets/100") ~> sealRoute(service.route) ~> check {
        mediaType === MediaTypes.`application/json`
        status === NotFound
      }
    }

    "a call to update a widget at /widgets/{id}" in {
      Put("/widgets/101", Widget(Some(101), "foo2")).withHeaders(`Content-Type`(ContentTypes.`application/json`)) ~> sealRoute(service.route) ~> check {
        status === NoContent
      }
    }

    "a call to update a widget at /widgets/{id} with an invalid number" in {
      Put("/widgets/100", Widget(Some(100), "foo2")).withHeaders(`Content-Type`(ContentTypes.`application/json`)) ~> sealRoute(service.route) ~> check {
        status === NotFound
      }
    }

    "a call to delete a widget at /widgets/{id}" in {
      Delete("/widgets/101") ~> sealRoute(service.route) ~> check {
        status === NoContent
      }
    }

    "a call to delete a widget at /widgets/{id} with an invalid number" in {
      Delete("/widgets/100") ~> sealRoute(service.route) ~> check {
        status === NotFound
      }
    }
  }

}
