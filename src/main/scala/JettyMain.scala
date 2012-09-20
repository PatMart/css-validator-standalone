package org.w3.cssvalidator

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler, ServletHolder }
import org.eclipse.jetty.util.component.LifeCycle
import java.util.concurrent.CountDownLatch
import java.io._
import java.nio.file._

/**
 * http://download.eclipse.org/jetty/stable-8/apidocs/
 * http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 */
class CSSValidator(port: Int) {

  val server: Server = new Server

  def stop(): Unit = {
    server.stop()
    server.join()
  }
  
  def start(): Unit = {

    server.setGracefulShutdown(500)
    server.setSendServerVersion(false)
    server.setSendDateHeader(true)
    server.setStopAtShutdown(true)

    val connector = new SelectChannelConnector
    connector.setPort(port)
    connector.setMaxIdleTime(90000)
    server.addConnector(connector)

    // the path to the war file
    // first, tries to see if this is running from sbt
    // if it's not, we assume we are in the jar and that the war is available there
    val warPath: String = {
      val date = "2012-09-20"
      val warName = "css-validator-" + date + ".war"
      val sbtPath = "src/main/resources/" + warName
      if (Paths.get(sbtPath).toFile.exists) {
        sbtPath
      } else {
        val tmpPath = Paths.get(java.lang.System.getProperty("java.io.tmpdir")).resolve(warName)
        // this extracts the war file and make it available in the tmp directory
        if (! tmpPath.toFile.exists) {
          val is: InputStream = getClass().getResourceAsStream("/" + warName)
          Files.copy(is, tmpPath)
        }
        tmpPath.toString
      }
    }

    val webapp = new WebAppContext
    webapp.setContextPath("/")
    webapp.setWar(warPath)
    server.setHandler(webapp)

    val cdl = new CountDownLatch(1)
    val listener = new LifeCycle.Listener {
      def lifeCycleFailure(event: LifeCycle, cause: Throwable): Unit = ()
      def lifeCycleStarted(event: LifeCycle): Unit = cdl.countDown()
      def lifeCycleStarting(event: LifeCycle): Unit = ()
      def lifeCycleStopped(event: LifeCycle): Unit = ()
      def lifeCycleStopping(event: LifeCycle): Unit = ()
   }

    server.addLifeCycleListener(listener)
    server.start()
    cdl.await()

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
