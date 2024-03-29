# The Web module

This Gradle module hosts your web application and is able to build a WAR file which
you can run in your favourite Servlet container.

## Dissection of project files

Let's look at all files that this project is composed of, and what are the points where you'll add functionality:

| Files | Meaning
| ----- | -------
| [build.gradle](build.gradle) | [Gradle](https://gradle.org/) build tool configuration files. Gradle is used to compile your app, download all dependency jars and build a war file
| [src/main/resources/](src/main/resources) | A bunch of static files not compiled by Kotlin in any way; see below for explanation.
| [logback.xml](src/main/resources/logback.xml) | We're using [Slf4j](https://www.slf4j.org/) for logging and this is the configuration file for Slf4j
| [db/migration/](src/main/resources/db/migration) | Database upgrade instructions for the [Flyway](https://flywaydb.org/) framework. Database is upgraded on every server boot, to ensure it's always up-to-date. See the [Migration Naming Guide](https://flywaydb.org/documentation/migrations#naming) for more details.
| [webapp/](src/main/webapp) | static files provided as-is to the browser. This project stores nothing important here
| [src/main/kotlin/](src/main/kotlin) | The main Kotlin sources of your web app. You'll be mostly editing files located in this folder.
| [Bootstrap.kt](src/main/kotlin/com/example/vok/Bootstrap.kt) | When Servlet Container (such as Tomcat) starts your app, it will run the `Bootstrap.contextInitialized()` function before any calls to your app are made. We need to bootstrap the Vaadin-on-Kotlin framework, in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date. After that's done, your app is ready to be serving client browsers. Also creates two sample users, user/user and admin/admin
| [MyUI.kt](src/main/kotlin/com/example/vok/MyUI.kt) | The main UI of the app; typically contains a template UI code which guarantees unified look-and-feel of your app. You then typically provide a layout which will host the views as the user navigates througout the app. Shows the `LoginForm` if there is no user logged in.
| [WelcomeView.kt](src/main/kotlin/com/example/vok/WelcomeView.kt) | The view accessible by all logged-in users, shown when the user browses the root page.
| [UserView.kt](src/main/kotlin/com/example/vok/UserView.kt) | The view accessible by users with roles of `user` and `admin`
| [AdminView.kt](src/main/kotlin/com/example/vok/AdminView.kt) | The view accessible by users with roles of `admin` only.
| [UserProfileView.kt](src/main/kotlin/com/example/vok/UserProfileView.kt) | The view accessible by all logged-in users. Shows information about the user himself.
