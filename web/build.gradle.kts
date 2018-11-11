plugins {
    war
    id("org.gretty")
    id("com.devsoap.plugin.vaadin")
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

vaadin {
    version = properties["vaadin8_version"] as String
}

val staging by configurations.creating

dependencies {
    compile("eu.vaadinonkotlin:vok-framework-sql2o:${ext["vok_version"]}")

    // logging
    // currently we are logging through the SLF4J API to LogBack. See logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.slf4j:slf4j-api:1.7.25")
    // this will configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:1.7.25")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${ext["vaadin8_version"]}")
    compile("com.vaadin:vaadin-server:${ext["vaadin8_version"]}")
    compile("com.vaadin:vaadin-client-compiled:${ext["vaadin8_version"]}")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // db
    compile("org.flywaydb:flyway-core:5.2.0")
    compile("com.h2database:h2:1.4.197")

    // Kotlin
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // test
    testCompile("com.github.mvysny.dynatest:dynatest-engine:0.12")
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v8:1.0.0")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner:9.0.11.0")
}

// Heroku
tasks {
    val copyToLib by registering(Copy::class) {
        into("$buildDir/server")
        from(staging) {
            include("webapp-runner*")
        }
    }
    "stage" {
        dependsOn("build", copyToLib)
    }
}
