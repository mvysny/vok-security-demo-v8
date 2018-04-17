package com.example.vok

import com.github.vok.framework.LoginForm
import com.github.vok.framework.Session
import com.github.vok.framework.VokSecurity
import com.github.vok.karibudsl.*
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.PushStateNavigation
import com.vaadin.navigator.ViewDisplay
import com.vaadin.server.Page
import com.vaadin.server.UserError
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.Position
import com.vaadin.ui.Alignment
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import org.slf4j.LoggerFactory

/**
 * The Vaadin UI which demoes all the features. If not familiar with Vaadin, please check out the Vaadin tutorial first.
 * @author mvy
 */
@Theme("valo")
@Title("Vaadin-on-Kotlin Security Demo")
@PushStateNavigation
class MyUI : UI() {

    private lateinit var content: ValoMenu

    override fun init(request: VaadinRequest?) {
        if (!Session.loginManager.isLoggedIn) {
            setContent(LoginView())
            return
        }

        content = valoMenu {
            appTitle = "<strong>Penny's Shop</strong>"
            userMenu {
                item(Session.loginManager.user!!.username) {
                    item("Edit Profile")
                    item("Preferences")
                    addSeparator()
                    item("Sign Out", menuSelected = { Session.loginManager.logout() })
                }
                section("User+Admin")
                menuButton("User", badge = "3")
                section("Admin only")
                menuButton("Admin")
            }
        }
        setContent(content)
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
        VokSecurity.install()

        setErrorHandler { e ->
            log.error("Vaadin UI uncaught exception ${e.throwable}", e.throwable)
            // when the exception occurs, show a nice notification
            Notification(
                "Oops",
                "An error occurred, and we are really sorry about that. Already working on the fix!",
                Notification.Type.ERROR_MESSAGE
            ).apply {
                styleName = "${ValoTheme.NOTIFICATION_CLOSABLE} ${ValoTheme.NOTIFICATION_ERROR}"
                position = Position.TOP_CENTER
                show(Page.getCurrent())
            }
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(MyUI::class.java)
    }
}

class LoginView : VerticalLayout() {
    init {
        setSizeFull()
        val loginForm = object : LoginForm("VoK Security Demo") {
            override fun doLogin(username: String, password: String) {
                val user = User.findByUsername(username)
                if (user == null) {
                    this.username.componentError = UserError("The user does not exist")
                    return
                }
                if (!user.passwordMatches(password)) {
                    this.password.componentError = UserError("Invalid password")
                    return
                }
                Session.loginManager.login(user)
            }
        }
        addComponent(loginForm)
        loginForm.alignment = Alignment.MIDDLE_CENTER
    }
}
