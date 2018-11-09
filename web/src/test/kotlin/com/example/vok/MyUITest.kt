package com.example.vok

import com.github.mvysny.kaributesting.v8.*
import com.github.mvysny.dynatest.DynaTest
import com.github.vok.framework.Session
import com.github.vok.karibudsl.autoDiscoverViews
import com.github.vokorm.deleteAll
import com.vaadin.server.ErrorMessage
import com.vaadin.ui.Button
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import org.jsoup.Jsoup
import kotlin.test.expect

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class MyUITest : DynaTest({
    beforeGroup { autoDiscoverViews("com.example.vok"); Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup({ MyUI() }) }
    afterEach { MockVaadin.tearDown() }

    test("unsuccessful login") {
        _get<LoginView>() // check that initially the LoginView is displayed
        _get<TextField> { caption = "Username" }._value = "invaliduser"
        _get<PasswordField> { caption = "Password" }._value = "invalidpass"
        _get<Button> { caption = "Sign In" }._click()
        expect(false) { Session.loginManager.isLoggedIn }
        expect("The user does not exist or invalid password") { _get<TextField> { caption = "Username" }.componentError.message }
    }

    test("successful login") {
        _get<LoginView>() // check that initially the LoginView is displayed
        _get<TextField> { caption = "Username" }._value = "user"
        _get<PasswordField> { caption = "Password" }._value = "user"
        _get<Button> { caption = "Sign In" }._click()
        expect(true) { Session.loginManager.isLoggedIn }
        _expectNone<LoginView>()
        // after successful login the WelcomeView should be displayed
        _get<WelcomeView>()
    }
})

/**
 * Unescapes [ErrorMessage.getFormattedHtmlMessage] and converts it to sane string. E.g.
 * `The&#32;user&#32;does&#32;not&#32;exist` is converted to `The user does not exist`.
 */
val ErrorMessage.message: String get() = Jsoup.parse(formattedHtmlMessage).text()
