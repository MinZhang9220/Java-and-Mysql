name := "epic-seng301-project"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq (
  "io.cucumber" % "cucumber-core" % "4.2.0" % " test ",
  "io.cucumber" % "cucumber-jvm" % "4.2.0" % " test ",
  "io.cucumber" % "cucumber-junit" % "4.2.0" % " test ",
  "io.cucumber" % "cucumber-java" % "4.2.0",
  "org.xerial" % "sqlite-jdbc" %  "3.25.2",
  "org.mockito" % "mockito-core" % "2.25.1" % " test ",
)

libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.4.0" % Test
libraryDependencies += "org.junit.vintage" % "junit-vintage-engine" % "4.12.0-RC3" % Test
libraryDependencies += "org.junit.platform" % "junit-platform-runner" % "1.0.0-RC3" % Test
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
// https://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "commons-io" % "commons-io" % "2.6"