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
    compile("eu.vaadinonkotlin:vok-framework-vokdb:${properties["vok_version"]}")
    compile("com.zaxxer:HikariCP:3.4.1")

    // logging
    // currently we are logging through the SLF4J API to LogBack. See logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.slf4j:slf4j-api:1.7.30")
    // this will configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:1.7.30")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${properties["vaadin8_version"]}")
    compile("com.vaadin:vaadin-server:${properties["vaadin8_version"]}")
    compile("com.vaadin:vaadin-client-compiled:${properties["vaadin8_version"]}")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // db
    compile("org.flywaydb:flyway-core:6.2.4")
    compile("com.h2database:h2:1.4.200")

    // Kotlin
    compile(kotlin("stdlib-jdk8"))

    // test
    testCompile("com.github.mvysny.dynatest:dynatest-engine:0.15")
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v8:1.1.19")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner:9.0.27.1")
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
