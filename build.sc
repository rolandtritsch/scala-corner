package build

import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import mill.scalalib.publish._

import $ivy.`com.goyeau::mill-scalafix::0.4.0`
import com.goyeau.mill.scalafix.ScalafixModule

import $ivy.`com.lihaoyi::mill-contrib-scoverage:`
import mill.contrib.scoverage.ScoverageModule

object main
  extends ScalaModule 
  with ScalafmtModule 
  with ScalafixModule 
  with ScoverageModule 
  with PublishModule
{
  def artifactName = "scala-corner"
  def publishVersion = "1.0.1"
  def pomSettings = PomSettings(
    description = "A simple corner counter",
    organization = "org.tritsch",
    url = "https://github.com/rolandtritsch/scala-corner",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("rolandtritsch", "scala-corner"),
    developers = Seq(Developer("rolandtritsch", "Roland Tritsch", "https://github.com/rolandtritsch"))
  )
  def scalaVersion = "3.4.3"
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
