resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.6.4")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")