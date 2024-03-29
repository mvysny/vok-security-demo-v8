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
    // don't use api/implementation: com.devsoap.plugin.vaadin will then cause them not to be packaged in the WAR archive!
    // Karibu-DSL dependency
    compile("eu.vaadinonkotlin:vok-framework-vokdb:${properties["vok_version"]}")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    compile("org.slf4j:slf4j-simple:1.7.30")
    compile("org.slf4j:slf4j-api:1.7.30")
    // this will configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:1.7.30")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${properties["vaadin8_version"]}")
    compile("com.vaadin:vaadin-server:${properties["vaadin8_version"]}")
    compile("com.vaadin:vaadin-client-compiled:${properties["vaadin8_version"]}")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // db
    compile("com.zaxxer:HikariCP:3.4.5")
    compile("org.flywaydb:flyway-core:7.1.1")
    compile("com.h2database:h2:1.4.200")

    // Kotlin
    compile(kotlin("stdlib-jdk8"))

    // test
    testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.19")
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v8:1.2.12")

    // heroku app runner
    staging("com.heroku:webapp-runner:9.0.41.0")
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
