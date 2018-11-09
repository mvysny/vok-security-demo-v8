package com.example.vok

import com.github.mvysny.kaributesting.v8.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.github.vok.framework.LoginForm
import com.github.vok.framework.Session
import com.github.mvysny.karibudsl.v8.autoDiscoverViews
import com.github.mvysny.karibudsl.v8.navigateToView
import com.github.vok.security.AccessRejectedException
import com.github.vokorm.deleteAll
import com.vaadin.server.Page
import kotlin.test.expect

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    expect(true) { Session.loginManager.login(username, username) }
    Page.getCurrent().reload()
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
    // in fact, by default the WelcomeView should be displayed
    _get<WelcomeView>()
}

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class AdminViewTest : DynaTest({
    beforeGroup { autoDiscoverViews("com.example.vok"); Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup({ MyUI() }) }
    afterEach { MockVaadin.tearDown() }

    test("Admin should see AdminView properly") {
        login("admin")
        navigateToView<AdminView>()
        _get<AdminView>()
    }

    test("User should not see AdminView") {
        login("user")
        expectThrows(AccessRejectedException::class, "Can not access AdminView, you are not admin") {
            navigateToView<AdminView>()
        }
    }
})
