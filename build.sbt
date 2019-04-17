name := "epic-seng301-project"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq (
  "io.cucumber" % "cucumber-core" % "4.2.0" % " test ",
  "io.cucumber" % "cucumber-jvm" % "4.2.0" % " test ",
  "io.cucumber" % "cucumber-junit" % "4.2.0" % " test ",
  "io.cucumber" % "cucumber-java" % "4.2.0",
  "org.xerial" % "sqlite-jdbc" %  "3.25.2",
  "org.mockito" % "mockito-core" % "2.25.1" % " test "
)