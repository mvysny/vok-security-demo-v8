package com.example.vok

import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinServlet
import eu.vaadinonkotlin.VaadinOnKotlin
import eu.vaadinonkotlin.security.LoggedInUserResolver
import eu.vaadinonkotlin.security.loggedInUserResolver
import eu.vaadinonkotlin.sql2o.dataSource
import eu.vaadinonkotlin.sql2o.dataSourceConfig
import eu.vaadinonkotlin.vaadin8.Session
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.servlet.annotation.WebServlet

/**
 * Boots the app:
 *
 * * Makes sure that the database is up-to-date, by running migration scripts with Flyway. This will work even in cluster as Flyway
 *   automatically obtains a cluster-wide database lock.
 * * Initializes the VaadinOnKotlin framework.
 * * Configures the loggedInUserResolver so that the security framework knows whether there is an user logged in and which one
 * * Pre-creates two users: user/user and admin/admin
 * @author mvy
 */
@WebListener
class Bootstrap: ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        log.info("Starting up")

        // this will configure your database. For demo purposes, an in-memory embedded H2 database is used. To use a production-ready database:
        // 1. fill in the proper JDBC URL here
        // 2. make sure to include the database driver into the classpath, by adding a dependency on the driver into the build.gradle file.
        VaadinOnKotlin.dataSourceConfig.apply {
            driverClassName = Driver::class.java.name
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
        }

        // Initializes the VoK framework
        log.info("Initializing VaadinOnKotlin")
        VaadinOnKotlin.init()

        // Makes sure the database is up-to-date
        log.info("Running DB migrations")
        val flyway = Flyway.configure()
            .dataSource(VaadinOnKotlin.dataSource)
            .load()
        flyway.migrate()

        // setup security
        VaadinOnKotlin.loggedInUserResolver = object : LoggedInUserResolver {
            override fun isLoggedIn(): Boolean = Session.loginManager.isLoggedIn
            override fun getCurrentUserRoles(): Set<String> = Session.loginManager.getCurrentUserRoles()
        }
        User(username = "admin", roles = "admin,user").apply { setPassword("admin"); save() }
        User(username = "user", roles = "user").apply { setPassword("user"); save() }

        log.info("Initialization complete")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        log.info("Shutting down");
        log.info("Destroying VaadinOnKotlin")
        VaadinOnKotlin.destroy()
        log.info("Shutdown complete")
    }

    companion object {
        private val log = LoggerFactory.getLogger(Bootstrap::class.java)

        init {
            // let java.util.logging log to slf4j
            SLF4JBridgeHandler.removeHandlersForRootLogger()
            SLF4JBridgeHandler.install()
        }
    }
}

@WebServlet(urlPatterns = ["/*"], name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI::class, productionMode = false)
class MyUIServlet : VaadinServlet()

