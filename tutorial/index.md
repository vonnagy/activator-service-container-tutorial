Service Container
===========================

# Overview
This is a Typesafe Activator Template for demonstrating how to build a microservice using the
[Service Container](https://github.com/vonnagy/service-container). The example contained within the template constructs
a simple REST API for the CRUD operation of a Widget. It simulates the persistence layer, bug can easily be replaced with your favorite bit of technology.

## Application
The main application entry point in `Service.scala`. In this file, it builds up an instance of the service and starts
the service. During the builder operation is registers the API endpoints that are contained in the class `WidgetService`.

```scala
// Here we establish the container and build it while
// applying extras.
val service = new ContainerBuilder()
  // Register the API routes
  .withRoutes(classOf[WidgetService]).build

service.start
```

By default, the service runs on [http://localhost:9092](http://localhost:9092) and thus all endpoints can be accessed using this base address. There are several built-in endpoints that the container exposes and those are documented here: [Built-in Endpoints](https://github.com/vonnagy/service-container/blob/master/docs/Endpoints.md)

## Configuration
Configuration of the **Service Container** uses the standard [Typesafe Config](https://github.com/typesafehub/config) rollup patterns. For more detailed information, refer to the [Configuration](https://github.com/vonnagy/service-container/blob/master/docs/Configuration.md)

This template contains an `application.conf` file that overrides the default settings which are contained in the dependent libraries. This file can be edited or configuration can also be set by the following:

 - A JVM system property called `config.file` can be used (e.g. `-Dconfig.file=/myconfig.conf`)
 - Place an `application.conf` file in a **conf** sub-directory off the process root
 - Configure the service with a config during the build process (e.g. `val service = new ContainerBuilder().withConfig(ConfigFactory.parseFile("/somefile.conf"))`)

## API Endpoints
The API endpoints are defined in the class `WidgetService`. This class defines the routes to be used by applying standard standard [Spray](http://spray.io) routing techniques.

## Health Checks
The **Service Container** allows one to define sub-checks within the system in order to help determine the state of the service. Any number of registered healths checks can be used and there is support for registration by using traits (`RegisteredHealthCheck` and `RegisteredHealthCheckActor`) or by registering when building the service.

In this template the class `WidgetPersistence` registers itself as a health check to evaluate the state of the persistence.

```scala
class WidgetPersistence(implicit val system: ActorSystem) extends RegisteredHealthCheck {

  import system.dispatcher

  /**
   * Fetch the health for this registered checker.
   * @return returns a future to the health information
   */
  def getHealth: Future[HealthInfo] = Future {
    HealthInfo("db", HealthState.OK, s"The persistence is running and currently contains ${WidgetPersistence.widgets.size} Widgets")
  }
```

## Metrics
The **Service Container** supports the management of metrics. For more details about metrics can be obtained here: [Metrics](https://github.com/vonnagy/service-container/blob/master/docs/Metrics.md)

In this template the class `WidgetPersistence` registers calls to it's functions and records the number of times they have been called. Each metrics is created like the following:

```scala
val get = Counter("db.widgets.get")
```

Per the comments above regarding built-in endpoints, the new metrics can be viewed by calling the endpoint at [http://localhost:9092/metrics](http://localhost:9092/metrics).

### Reporting
Once metrics are gathered they can be reported to other sources. Reporters are defined in the configuration files and follow a standard pattern. They are defined under `container.metrics.reporters` and have there own named sections. For more detailed information, refer to the [Metrics](https://github.com/vonnagy/service-container/blob/master/docs/Metrics.md) documentation for the Service Container.

In this template we will enable the default reporter which is writes to the log file using `Slf4j`. The default configuration section looks like the following:

```hocon
container {
	metrics {
	    reporters {
	      Slf4j {
	        # The name of the reporter class
	        class = "com.github.vonnagy.service.container.metrics.reporting.Slf4jReporter"
	        # Is the reporter enabled
	        enabled = on
	        # What is the interval to report on
	        reporting-interval = 60s
	        # What is the logger
	        logger = "com.github.vonnagy.service.container.metrics"
	      }
	    }
	}
}
```

The configuration for this tutorial also contains an example of how to wire up the `StatsD` reporter, but is disabled by default. The `StatsD` reporter is contained in the **Service Container Metrics Reporting** library.

```hocon
statsD {
	# The name of the reporter class
	 class = "com.github.vonnagy.service.container.metrics.reporting.StatsDReporter"
	 # Is the reporter enabled
	 enabled = off
	 # What is the interval to report on
	 reporting-interval = 5s
	 # The StatsD host
	 host = "localhost"
	 # The statsd port
	 port = 8125
	 # An optional prefix to append to metrics being sent to StatsD
	 metric-prefix = ""
}
```