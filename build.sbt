lazy val core = project.in(file("."))
    .settings(commonSettings, releaseSettings)
    .settings(
      name := "cls-sch-pipe"
    ).enablePlugins(JavaAppPackaging, DockerPlugin, AutomateHeaderPlugin)

val catsV = "1.6.1"
val kittensV = "2.0.0"
val catsEffectV = "1.4.0"
val mouseV = "0.21"
val shapelessV = "2.3.3"
val fs2V = "1.0.5"
val http4sV = "0.20.20"
val circeV = "0.11.1"
val doobieV = "0.7.1"
val pureConfigV = "0.12.1"
val refinedV = "0.9.10"

val log4catsV = "0.3.0"
val catsParV = "0.2.1"
val catsTimeV = "0.2.0"
val fuuidV = "0.2.0"
val lineBackerV = "0.2.1"

val specs2V = "4.7.1"
val testcontainersSpecsV = "0.1.0"
val disciplineV = "0.11.1"
val scShapelessV = "1.2.3"

val kindProjectorV = "0.10.3"
val betterMonadicForV = "0.3.1"


lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

// check for library updates whenever the project is [re]load
onLoad in Global := { s =>
  "dependencyUpdates" :: s
}

// General Settings
lazy val commonSettings = Seq(
  organization := "io.chrisdavenport",

  scalaVersion := "2.12.7",
  crossScalaVersions := Seq(scalaVersion.value),
  scalacOptions += "-Yrangepos",
  maintainer := "chris@christopherdavenport.tech",
  organizationName := "Christopher Davenport",
  startYear := Some(2018),
  licenses += ("AGPL-3.0", url("https://www.gnu.org/licenses/agpl.html")),

  addCompilerPlugin("org.typelevel" % "kind-projector" % kindProjectorV cross CrossVersion.binary),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
  libraryDependencies ++= Seq(
    "org.typelevel"               %% "cats-core"                  % catsV,

    "org.typelevel"               %% "kittens"                    % kittensV,
    "org.typelevel"               %% "alleycats-core"             % catsV,
    "org.typelevel"               %% "mouse"                      % mouseV,

    "org.typelevel"               %% "cats-effect"                % catsEffectV,

    "com.chuusai"                 %% "shapeless"                  % shapelessV,

    "co.fs2"                      %% "fs2-core"                   % fs2V,
    "co.fs2"                      %% "fs2-io"                     % fs2V,

    "org.http4s"                  %% "http4s-dsl"                 % http4sV,
    "org.http4s"                  %% "http4s-blaze-server"        % http4sV,
    "org.http4s"                  %% "http4s-blaze-client"        % http4sV,
    "org.http4s"                  %% "http4s-circe"               % http4sV,

    "io.circe"                    %% "circe-core"                 % circeV,
    "io.circe"                    %% "circe-generic"              % circeV,
    "io.circe"                    %% "circe-parser"               % circeV,
    "io.circe"                    %% "circe-yaml"                 % "0.10.0",

    "com.monovore"                %% "decline"                    % "0.6.2",

    "org.tpolecat"                %% "doobie-core"                % doobieV,
    "org.tpolecat"                %% "doobie-h2"                  % doobieV,
    "org.tpolecat"                %% "doobie-hikari"              % doobieV,
    "org.tpolecat"                %% "doobie-postgres"            % doobieV,
    "org.tpolecat"                %% "doobie-specs2"              % doobieV       % Test,

    "io.chrisdavenport"           %% "log4cats-core"              % log4catsV,
    "io.chrisdavenport"           %% "log4cats-slf4j"             % log4catsV,
    "io.chrisdavenport"           %% "log4cats-extras"            % log4catsV,
    "ch.qos.logback"              %  "logback-classic"            % "1.2.3",
    "io.chrisdavenport"           %% "log4cats-testing"           % log4catsV     % Test,

    "io.chrisdavenport"           %% "cats-par"                   % catsParV,
    "io.chrisdavenport"           %% "cats-time"                  % catsTimeV,

    "io.chrisdavenport"           %% "system-effect"              % "0.1.0",
    "io.chrisdavenport"           %% "monoids"                    % "0.1.0",

    "org.flywaydb"                % "flyway-core"                 % "6.0.4",

    "io.chrisdavenport"           %% "linebacker"                 % lineBackerV,

    "io.chrisdavenport"           %% "fuuid"                      % fuuidV,

    "com.github.pureconfig"       %% "pureconfig"                 % pureConfigV,

    "eu.timepit"                  %% "refined"                    % refinedV,
    "eu.timepit"                  %% "refined-scalacheck"         % refinedV      % Test,

    "org.specs2"                  %% "specs2-core"                % specs2V       % Test,
    "org.specs2"                  %% "specs2-scalacheck"          % specs2V       % Test,
    "io.chrisdavenport"           %% "testcontainers-specs2"      % testcontainersSpecsV % Test,
    "org.typelevel"               %% "discipline"                 % disciplineV   % Test,
    "com.github.alexarchambault"  %% "scalacheck-shapeless_1.14"  % scShapelessV  % Test
  )
)

lazy val releaseSettings = {
  import ReleaseTransformations._
  Seq(
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      // For non cross-build projects, use releaseStepCommand("publishSigned")
      // releaseStepCommandAndRemaining("+publishSigned"),
      setNextVersion,
      // releaseStepCommand("sonatypeReleaseAll"),
      commitNextVersion,
      pushChanges
    ),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    credentials ++= (
      for {
        username <- Option(System.getenv().get("SONATYPE_USERNAME"))
        password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
      } yield
        Credentials(
          "Sonatype Nexus Repository Manager",
          "oss.sonatype.org",
          username,
          password
        )
    ).toSeq,
    publishArtifact in Test := false,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ChristopherDavenport/cls-sch-pipe"),
        "git@github.com:ChristopherDavenport/cls-sch-pipe.git"
      )
    ),
    homepage := Some(url("https://github.com/ChristopherDavenport/cls-sch-pipe")),
    publishMavenStyle := true,
    pomIncludeRepository := { _ =>
      false
    },
    pomExtra := {
      <developers>
        {for ((username, name) <- contributors) yield
        <developer>
          <id>{username}</id>
          <name>{name}</name>
          <url>http://github.com/{username}</url>
        </developer>
        }
      </developers>
    }
  )
}

lazy val mimaSettings = {
  import sbtrelease.Version

  def semverBinCompatVersions(major: Int, minor: Int, patch: Int): Set[(Int, Int, Int)] = {
    val majorVersions: List[Int] = List(major)
    val minorVersions : List[Int] = 
      if (major >= 1) Range(0, minor).inclusive.toList
      else List(minor)
    def patchVersions(currentMinVersion: Int): List[Int] = 
      if (minor == 0 && patch == 0) List.empty[Int]
      else if (currentMinVersion != minor) List(0)
      else Range(0, patch - 1).inclusive.toList

    val versions = for {
      maj <- majorVersions
      min <- minorVersions
      pat <- patchVersions(min)
    } yield (maj, min, pat)
    versions.toSet
  }

  def mimaVersions(version: String): Set[String] = {
    Version(version) match {
      case Some(Version(major, Seq(minor, patch), _)) =>
        semverBinCompatVersions(major.toInt, minor.toInt, patch.toInt)
          .map{case (maj, min, pat) => maj.toString + "." + min.toString + "." + pat.toString}
      case _ =>
        Set.empty[String]
    }
  }
  // Safety Net For Exclusions
  lazy val excludedVersions: Set[String] = Set()

  // Safety Net for Inclusions
  lazy val extraVersions: Set[String] = Set()

  Seq(
    mimaFailOnProblem := mimaVersions(version.value).toList.headOption.isDefined,
    mimaPreviousArtifacts := (mimaVersions(version.value) ++ extraVersions)
      .filterNot(excludedVersions.contains(_))
      .map{v => 
        val moduleN = moduleName.value + "_" + scalaBinaryVersion.value.toString
        organization.value % moduleN % v
      },
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      import com.typesafe.tools.mima.core.ProblemFilters._
      Seq()
    }
  )
}

lazy val skipOnPublishSettings = Seq(
  skip in publish := true,
  publish := (()),
  publishLocal := (()),
  publishArtifact := false,
  publishTo := None
)
