[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Heroku](https://heroku-badge.herokuapp.com/?app=vok-sec-demo&style=flat&svg=1)](https://vok-sec-demo.herokuapp.com/)

# Vaadin-on-Kotlin Security Demo

Demonstrates the security aspect of the Vaadin-on-Kotlin framework. For a general information on
VoK Security please head to the [vok-security module documentation](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/README.md).

**Deprecated**: uses old VoK version and Vaadin 8. Please see the [vok-security-demo](https://github.com/mvysny/vok-security-demo) project.

## Getting Started

To quickly start the app, make sure that you have Java 8 JDK installed. Then, just type this into your terminal:

```bash
git clone https://github.com/mvysny/vok-security-demo-v8
cd vok-security-demo
./gradlew build web:appRun
```

The app will be running on [http://localhost:8080/](http://localhost:8080/).

The app is running live on Heroku at [https://vok-sec-demo-v8.herokuapp.com](https://vok-sec-demo-v8.herokuapp.com).

## About the application

The application uses the username+password authorization, with users stored in an in-memory H2 SQL database
(the [User](web/src/main/kotlin/com/example/vok/User.kt) class). There are no
views that could be accessed publicly - the user must always log in first, in order to see any part of the app.

There are two users pre-created by the [Bootstrap](web/src/main/kotlin/com/example/vok/Bootstrap.kt) class:

* The 'user' user with the password of 'user' and the role of `user`
* The 'admin' user with the password of 'admin' and two roles: `admin` and `user`

The [MyUI](web/src/main/kotlin/com/example/vok/MyUI.kt) is configured to show a full-screen
login form (provided for us by the Vaadin-on-Kotlin as [LoginForm](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-util-vaadin8/src/main/kotlin/com/github/vok/framework/VokSecurity.kt) class).
The username and password are compared against the database. The `User` class takes advantage
of the [HasPassword](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/src/main/kotlin/com/github/vok/security/simple/HasPassword.kt)
mixin which makes sure to store the passwords in a hashed form.

If the login succeeds, the user is then stored into the session (or, rather, the `LoginManager` class
is stored in the session along with the currently logged-in user. This way, we can group all
login/logout functionality into single class). Then, the page is refreshed. This forces Vaadin
to create a new instance of the UI. Since a non-null user is now in the session, the UI will show
the application layout instead of the login form.

There are four views:

* The [WelcomeView](web/src/main/kotlin/com/example/vok/WelcomeView.kt) which is accessible by all logged-in users;
* The [UserView](web/src/main/kotlin/com/example/vok/UserView.kt) which is accessible by all users with roles `user` and `admin`
* The [AdminView](web/src/main/kotlin/com/example/vok/AdminView.kt) which is accessible by users with the `admin` role only
* The [UserProfileView](web/src/main/kotlin/com/example/vok/UserProfileView.kt) which shows info about the currently logged-in user and is therefore accessible by
  all logged-in users.

The `MyUI` class contains proper logic for catching and displaying `AccessRejectedException`s, by the means
of installing the proper `ErrorHandler` into the UI. You can check the security being enforced, simply
by running the project, logging in as an admin (user `admin`, password `admin`) and clicking on the "Admin"
page (or visiting the [http://localhost:8080/admin](http://localhost:8080/admin) link).

Visit the [web module docs](web/) for more documentation on the individual project files.

## Dissection of project files

Let's look at all files that this project is composed of, and what are the points where you'll add functionality:

| Files | Meaning
| ----- | -------
| [build.gradle](build.gradle) | [Gradle](https://gradle.org/) build tool configuration files. Gradle is used to compile your app, download all dependency jars and build a war file
| [gradlew](gradlew), [gradlew.bat](gradlew.bat), [gradle/](gradle) | Gradle runtime files, so that you can build your app from command-line simply by running `./gradlew`, without having to download and install Gradle distribution yourself.
| [.travis.yml](.travis.yml) | Configuration file for [Travis-CI](http://travis-ci.org/) which tells Travis how to build the app. Travis watches your repo; it automatically builds your app and runs all the tests after every commit.
| [Procfile](Procfile) | Configuration file for [Heroku](https://www.heroku.com/) which hosts the app. Heroku will wait for Travis to verify the build, then it will build a Tomcat bundle and run it inside of docker.
| [.gitignore](.gitignore) | Tells [Git](https://git-scm.com/) to ignore files that can be produced from your app's sources - be it files produced by Gradle, Intellij project files etc.
| [web/](web/) | The web Gradle module which will host the web application itself. You can add more Gradle modules as your project will grow. Visit the [web module docs](web/) for more documentation.

# Development with Intellij IDEA Ultimate

The easiest way (and the recommended way) to develop Karibu-DSL-based web applications is to use Intellij IDEA Ultimate.
It includes support for launching your project in any servlet container (Tomcat is recommended)
and allows you to debug the code, modify the code and hot-redeploy the code into the running Tomcat
instance, without having to restart Tomcat.

1. First, download Tomcat and register it into your Intellij IDEA properly: https://www.jetbrains.com/help/idea/2017.1/defining-application-servers-in-intellij-idea.html
2. Then just open this project in Intellij, simply by selecting `File / Open...` and click on the
   `build.gradle` file. When asked, select "Open as Project".
2. You can then create a launch configuration which will launch the `web` module as `exploded` in Tomcat with Intellij: just
   scroll to the end of this tutorial: https://kotlinlang.org/docs/tutorials/httpservlets.html
3. Start your newly created launch configuration in Debug mode. This way, you can modify the code
   and press `Ctrl+F9` to hot-redeploy the code. This only redeploys java code though, to
   redeploy resources just press `Ctrl+F10` and select "Update classes and resources"
