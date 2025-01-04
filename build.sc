package build

import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import mill.scalalib.publish._

import $ivy.`com.goyeau::mill-scalafix::0.4.0`
import com.goyeau.mill.scalafix.ScalafixModule

import $ivy.`com.lihaoyi::mill-contrib-scoverage:`
import mill.contrib.scoverage.ScoverageModule

import $ivy.`com.lihaoyi::mill-contrib-sonatypecentral:`
import mill.contrib.sonatypecentral.SonatypeCentralPublishModule

object Configuration {
  val crossVersions = Seq("2.13.12", "3.4.3")

  val moduleOrganization = "org.tritsch"
  val moduleName = "scala-corner"
  val moduleVersion = "1.0.4"
  val moduleDescription = "A simple corner counter"
  val moduleUrl = "https://github.com/rolandtritsch/scala-corner"
  val moduleDeveloperUsername = "rolandtritsch"
  val moduleDeveloper = "Roland Tritsch"
  val moduleDeveloperUrl = "https://github.com/rolandtritsch"
}

object main extends Cross[MainModule](Configuration.crossVersions)

trait MainModule extends CrossScalaModule 
  with ScalafmtModule 
  with ScalafixModule 
  with ScoverageModule
  with SonatypeCentralPublishModule
  with PublishModule
{
  def crossScalaVersion = crossValue
  def artifactName = Configuration.moduleName
  def publishVersion = Configuration.moduleVersion
  def pomSettings = PomSettings(
    description = Configuration.moduleDescription,
    organization = Configuration.moduleOrganization,
    url = Configuration.moduleUrl,
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github(
      Configuration.moduleDeveloperUsername, 
      Configuration.moduleName
      ),
    developers = Seq(
      Developer(
        Configuration.moduleDeveloperUsername, 
        Configuration.moduleDeveloper, 
        Configuration.moduleDeveloperUrl
      )
    )
  )
  
  def scalacOptions = Seq("-Wunused:imports", "-deprecation", "-Xfatal-warnings")
  def scoverageVersion = "2.2.1"
  def ivyDeps = Agg(
    ivy"com.typesafe.scala-logging::scala-logging:3.9.5",
    ivy"ch.qos.logback:logback-classic:1.3.5",
  )
  
  def scalafixIvyDeps = Agg(
    ivy"com.github.xuwei-k::scalafix-rules:0.4.3",
  )

  object test extends ScoverageTests with TestModule.Munit {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::1.0.0",
      ivy"org.scalameta::munit-scalacheck:1.0.0",
      ivy"org.typelevel::spire:0.18.0",
    )
  }
}
