import sbtcrossproject.{crossProject, CrossType}

def v: String = "3.5.0"

enablePlugins(ScalaNativePlugin)

val nativeSettings = Seq(
  libraryDependencies -= "org.specs2" %%% "specs2-core" % "3.8.6" % "test",
  publishArtifact in (Compile, packageDoc) := false,
  publishArtifact in packageDoc := false,
  sources in (Compile, doc) := Seq.empty 
)

val commonSettings = Seq(
      version := v,
      organization := "com.github.scopt",
      scalaVersion := "2.11.8",
      crossScalaVersions := Seq("2.11.8", "2.10.6", "2.12.0"),
      homepage := Some(url("https://github.com/scopt/scopt")),
      licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
      git.remoteRepo := "git@github.com:scopt/scopt.git",
      description := """a command line options parsing library""",
      libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6" % "test",
      scalacOptions ++= Seq("-language:existentials"),
      resolvers += "sonatype-public" at "https://oss.sonatype.org/content/repositories/public"
    )

lazy val root = (crossProject(JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)  
  .settings(
    inThisBuild(commonSettings),
    name := "scopt"
  )
  .jvmSettings(
    // site
    // to preview, preview-site
    // to push, ghpages-push-site
    site.settings,
    site.includeScaladoc(s"$v/api"),
    ghpages.settings,
    // scaladoc fix
    unmanagedClasspath in Compile += Attributed.blank(new java.io.File("doesnotexist"))
  )
  .nativeSettings(nativeSettings: _*) in file(".")) 

lazy val rootJVM = root.jvm
lazy val rootNative = root.native

lazy val testNative = Project("testNative", file("test-native"))
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings: _*)
  .settings(nativeSettings: _*)
  .settings(normalizedName := "scopt-test-native")
  .dependsOn(rootNative)
