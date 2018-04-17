package com.example.vok

import com.github.karibu.testing.*
import com.github.mvysny.dynatest.DynaTest
import com.github.vok.framework.Session
import com.github.vok.karibudsl.autoDiscoverViews
import com.github.vokorm.deleteAll
import com.vaadin.ui.Button
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import kotlin.test.expect

class MyUITest : DynaTest({
    beforeGroup { autoDiscoverViews("com.example.vok"); Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup({ MyUI() }) }

    test("unsuccessful login") {
        _get<LoginView>() // check that initially the LoginView is displayed
        _get<TextField> { caption = "Username" }._value = "invaliduser"
        _get<PasswordField> { caption = "Password" }._value = "invalidpass"
        _get<Button> { caption = "Sign In" }._click()
        expect(false) { Session.loginManager.isLoggedIn }
        expect("The&#32;user&#32;does&#32;not&#32;exist") { _get<TextField> { caption = "Username" }.componentError.formattedHtmlMessage }
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
