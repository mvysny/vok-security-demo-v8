import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    kotlin("jvm") version "1.4.31"
    // need to use Gretty here because of https://github.com/johndevs/gradle-vaadin-plugin/issues/317
    id("org.gretty") version "3.0.3"
    id("com.devsoap.plugin.vaadin") version "2.0.0.beta2"
}

defaultTasks("clean", "build")

allprojects {
    group = "com.example.vok"
    version = "1.0-SNAPSHOT"

    repositories {
    	mavenCentral()
        jcenter()
    }

    tasks {
        // Heroku
        val stage by registering {
            dependsOn("build")
        }
    }
}

subprojects {

    apply { plugin("kotlin") }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            // to see the exceptions of failed tests in Travis-CI console.
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
