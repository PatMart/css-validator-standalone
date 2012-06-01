import sbt._
import sbt.Keys._
// import com.github.siasia._
// import WebPlugin._
// import PluginKeys._
import sbtassembly.Plugin._
import AssemblyKeys._

/**
 * build configuration for CSS Validator Standalone
 */
object CSSValidator extends Build {

  lazy val cssValidator = Project(
    id = "css-validator-standalone",
    base = file("."),
    settings = Defaults.defaultSettings /*++ webSettings*/ ++ assemblySettings ++ Seq(
      organization := "org.w3",
      version := "1.0-SNAPSHOT",
      scalaVersion := "2.9.2",
      crossScalaVersions := Seq("2.9.1", "2.9.2"),
      javacOptions ++= Seq("-Xlint:unchecked"),
      mainClass in assembly := Some("org.w3.cssvalidator.JettyMain"),
      jarName in assembly := "css-validator-standalone.jar",
      test in assembly := {},
//      assembleArtifact in packageScala := false,
      licenses := Seq("W3C License" -> url("http://opensource.org/licenses/W3C")),
      homepage := Some(url("https://github.com/w3c/css-validator-standalone")),
      publishTo <<= version { (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT")) 
          Some("snapshots" at nexus + "content/repositories/snapshots") 
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      publishArtifact in Test := false,
      pomIncludeRepository := { _ => false },
      pomExtra := (
        <scm>
          <url>git@github.com:w3c/css-validator-standalone.git</url>
          <connection>scm:git:git@github.com:w3c/css-validator-standalone.git</connection>
        </scm>
        <developers>
          <developer>
            <id>betehess</id>
            <name>Alexandre Bertails</name>
            <url>http://bertails.org</url>
          </developer>
        </developers>
      ),
      
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "apache-repo-releases" at "http://repository.apache.org/content/repositories/releases/",
      libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "compile", //  % "compile,container",
      libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided",
      libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.4"
    )
  )

}
