Service Container
===========================

[![Build Status](https://travis-ci.org/vonnagy/activator-service-container-tutorial.png?branch=master)](https://travis-ci.org/vonnagy/activator-service-container-tutorial)

# Overview
This is a Typesafe Activator Template for demonstrating how to build a micro-service using the
[Service Container](https://github.com/vonnagy/service-container). The example contained within the template constructs
a simple REST API for the CRUD operation of a Widget. It simulates the persistence layer, bug can easily be replaced with your favorite bit of technology.

## Application
The main application entry point in `Service.scala`. In this file, it builds up an instance of the service and starts
the service. During the builder operation is registers the API endpoints that are contained in the class `WidgetService`.

By default, the service runs on **http://localhost:9092** and thus all endpoints can be accessed using this base address. There are several built-in endpoints that the container exposes and those are documented here: [Built-in Endpoints](https://github.com/vonnagy/service-container/blob/master/docs/Endpoints.md)

## API Endpoints
The API endpoints are defined in the class `WidgetService`. This class defines the routes to be used by applying standard standard [Spray](http://spray.io) routing techniques.

## Health Checks
The **Service Container** allows one to define sub-checks within the system in order to help determine the state of the service. Any number of registered healths checks can be used and there is support for registration by using traits (`RegisteredHealthCheck` and `RegisteredHealthCheckActor`) or by registering when building the service.

In this template the class `WidgetPersistence` registers itself as a health check to evaluate the state of the persistence.

## Metrics
The **Service Container** supports the management of metrics. For more details about metrics can be obtained here: [Metrics](https://github.com/vonnagy/service-container/blob/master/docs/Metrics.md)

In this template the class `WidgetPersistence` registers calls to it's functions and records the number of times they have been called. Each metrics is created like the following:

```scala
val get = Counter("db.widgets.get")
```

Per the comments above regarding built-in endpoints, the new metrics can be viewed by calling the endpoint at **http://localhost:9092/metrics**.