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
    implementation("eu.vaadinonkotlin:vok-framework-vokdb:${properties["vok_version"]}")

    // logging
    // currently we are logging through the SLF4J API to LogBack. See logback.xml file for the logger configuration
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.slf4j:slf4j-api:1.7.30")
    // this will configure Vaadin to log to SLF4J
    implementation("org.slf4j:jul-to-slf4j:1.7.30")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    implementation("com.vaadin:vaadin-themes:${properties["vaadin8_version"]}")
    implementation("com.vaadin:vaadin-server:${properties["vaadin8_version"]}")
    implementation("com.vaadin:vaadin-client-compiled:${properties["vaadin8_version"]}")
    providedCompile("javax.servlet:javax.servlet-api:3.1.0")

    // db
    implementation("com.zaxxer:HikariCP:3.4.2")
    implementation("org.flywaydb:flyway-core:6.2.4")
    implementation("com.h2database:h2:1.4.200")

    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // test
    testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.16")
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v8:1.1.26")

    // heroku app runner
    staging("com.heroku:webapp-runner:9.0.31.0")
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
