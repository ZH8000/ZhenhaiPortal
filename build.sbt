name := "ZhenhaiPortal"

version := "0.0.1"

scalaVersion := "2.11.2"

seq(webSettings :_*)

scalacOptions := List("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "container"
)

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-webkit" % "2.6" % "compile->default"
)

port in container.Configuration := 8081

