package bootstrap.liftweb

import net.liftweb.http._
import net.liftweb.http.LiftRules
import net.liftweb.http.Req
import net.liftweb.util.Props.RunModes
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc.EarlyResponse
import scala.util._

class Boot 
{
  def errorPageResponse(req: Req, code: Int) = {
    println("req:" + req)
    println("code:" + code)
    val content = S.render(<lift:embed what={code.toString} />, req.request)
    XmlResponse(content.head, code, "text/html", req.cookies)
  }

  def redirectToXG = EarlyResponse { () =>
    val connectionTry = Try {
      val httpConnection = new java.net.URL("http://221.4.141.146")
                              .openConnection()
                              .asInstanceOf[sun.net.www.protocol.http.HttpURLConnection]

      httpConnection.setConnectTimeout(60 * 1000)
      httpConnection.setReadTimeout(60 * 1000)
      httpConnection.getResponseCode
    }

    connectionTry match {
      case Success(200) => S.redirectTo("http://221.4.141.146")
      case _            => S.redirectTo("http://dashboardXG.zhenhai.com.tw")
    }
  }

  def redirectToSZ = EarlyResponse { () =>

    val connectionTry = Try {

      val httpConnection = new java.net.URL("http://218.4.250.102")
                              .openConnection()
                              .asInstanceOf[sun.net.www.protocol.http.HttpURLConnection]

      httpConnection.setConnectTimeout(60 * 1000)
      httpConnection.setReadTimeout(60 * 1000)
      httpConnection.getResponseCode
    }

    connectionTry match {
      case Success(200) => S.redirectTo("http://221.4.141.146")
      case _            => S.redirectTo("http://dashboardXG.zhenhai.com.tw")
    }
  }

  def boot 
  {
    val siteMap = SiteMap(
      Menu("Home") / "index",
      Menu("RedirectToXG") / "redirectToXG" >> redirectToXG,
      Menu("RedirectToSZ") / "redirectToSZ" >> redirectToSZ
    )

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    LiftRules.addToPackages("code")
    LiftRules.setSiteMap(siteMap)

    LiftRules.exceptionHandler.prepend {
      case (runMode, req, exception) if runMode == RunModes.Production =>
        println(runMode)
        println(s"========== ${req.uri} =============")
        println(s"DateTime: ${new java.util.Date}")
        exception.printStackTrace()
        println(s"===================================")
        errorPageResponse(req, 500)
    }

  }
}
