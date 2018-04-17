package com.example.vok

import com.github.vok.framework.LoginForm
import com.github.vok.framework.Session
import com.github.vok.framework.VokSecurity
import com.github.vok.karibudsl.*
import com.github.vok.security.AccessRejectedException
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
 * This app offers no contents for logged-out users. Therefore we can simply ask the user to log in right away after the UI is created.
 * See the [init] method for more details.
 * @author mvy
 */
@Theme("valo")
@Title("Vaadin-on-Kotlin Security Demo")
@PushStateNavigation
class MyUI : UI() {

    private lateinit var content: ValoMenu

    override fun init(request: VaadinRequest?) {
        if (!Session.loginManager.isLoggedIn) {
            // If no user is logged in, then simply show the LoginView (a full-screen login form) and bail out.
            // When the user logs in, we will simply reload the page, which recreates the UI instance; since the user is stored in a session
            // and therefore logged in, the code will skip this block and will initialize the UI properly.
            setContent(LoginView())
            return
        }

        content = valoMenu {
            appTitle = "<strong>Penny's Shop</strong>"
            userMenu {
                item(Session.loginManager.user!!.username) {
                    item("Edit Profile", menuSelected = { navigateToView<UserProfileView>() })
                    item("Preferences")
                    addSeparator()
                    item("Sign Out", menuSelected = { Session.loginManager.logout() })
                }
                menuButton("Home", view = WelcomeView::class.java)
                menuButton("User", view = UserView::class.java)
                menuButton("Admin", view = AdminView::class.java)
            }
        }
        setContent(content)
        navigator = Navigator(this, content as ViewDisplay)
        navigator.addProvider(autoViewProvider)
        VokSecurity.install()

        setErrorHandler { e ->
            log.error("Vaadin UI uncaught exception ${e.throwable}", e.throwable)

            // often the original exception (say, AccessRejectedException) is wrapped in InvocationTargetException and RpcInvocationException. Unwrap.
            val cause: Throwable = e.throwable!!.originalCause()

            // if the exception is indeed the AccessRejectedException, handle it in a special way: show an Access Denied error message.
            if (cause is AccessRejectedException) {
                Notification(
                    "Access Denied",
                    cause.message,
                    Notification.Type.ERROR_MESSAGE
                ).apply {
                    styleName = "${ValoTheme.NOTIFICATION_CLOSABLE} ${ValoTheme.NOTIFICATION_ERROR}"
                    position = Position.MIDDLE_CENTER
                    show(Page.getCurrent())
                }
            } else {
                // just a generic error. Show a generic Oops error notification.
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
                    usernameField.componentError = UserError("The user does not exist")
                    return
                }
                if (!user.passwordMatches(password)) {
                    passwordField.componentError = UserError("Invalid password")
                    return
                }
                Session.loginManager.login(user)
            }
        }
        addComponent(loginForm)
        loginForm.alignment = Alignment.MIDDLE_CENTER
    }
}

fun Throwable.originalCause(): Throwable = cause?.originalCause() ?: this
