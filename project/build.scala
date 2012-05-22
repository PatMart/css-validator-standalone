import sbt._
import sbt.Keys._
import com.github.siasia._
import WebPlugin._
import PluginKeys._

/**
 * build configuration for CSS Validator Standalone
 */
object CSSValidator extends Build {

  lazy val cssValidator = Project(
    id = "css-validator-standalone",
    base = file("."),
    settings = Defaults.defaultSettings ++ webSettings ++ Seq(
      organization := "org.w3",
      version := "1.0-SNAPSHOT",
      scalaVersion := "2.9.2",
      javacOptions ++= Seq("-Xlint:unchecked"),
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "apache-repo-releases" at "http://repository.apache.org/content/repositories/releases/",
      libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "compile,container",
      libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided",
      libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.4"
    )
  )

}
