package org.w3.cssvalidator

import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.{ Server }
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler, ServletHolder }
import java.io._
import java.nio.file._

/**
 * http://download.eclipse.org/jetty/stable-8/apidocs/
 * http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 */
class CSSValidator(port: Int) {

  val server: Server = new Server

  def stop(): Unit = server.stop()
  
  def start(): Unit = {

    server setGracefulShutdown 500
    server setSendServerVersion false
    server setSendDateHeader true
    server setStopAtShutdown true

    val connector = new SelectChannelConnector
    connector setPort port
    connector setMaxIdleTime 90000
    server addConnector connector

    val warPath: String = {
      val sbtPath = "src/main/resources/css-validator.war"
      if (Paths.get(sbtPath).toFile.exists) {
        sbtPath
      } else {
        val tmpPath = Paths.get(java.lang.System.getProperty("java.io.tmpdir")).resolve("css-validator.war")
        val is: InputStream = getClass().getResourceAsStream("/css-validator.war")
        Files.copy(is, tmpPath)
        tmpPath.toString
      }
    }

    val webapp = new WebAppContext
    webapp setContextPath "/"
    webapp setWar warPath
    server setHandler webapp

    server.start()

  }
}

object JettyMain {

  def main(args: Array[String]): Unit = {

    val port = args.toList.headOption.map(_.toInt) getOrElse 8080

    val cssval = new CSSValidator(port)

    cssval.start()

    println(">> press Enter to stop")
    scala.Console.readLine()

    cssval.stop()
  }
}
