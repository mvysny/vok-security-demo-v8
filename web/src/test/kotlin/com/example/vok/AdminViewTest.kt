package com.example.vok

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._expectNone
import com.github.karibu.testing._get
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.github.vok.framework.LoginForm
import com.github.vok.framework.Session
import com.github.vok.karibudsl.autoDiscoverViews
import com.github.vok.karibudsl.navigateToView
import com.github.vok.security.AccessRejectedException
import com.github.vokorm.deleteAll
import com.vaadin.server.Page

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    MockVaadin.setup({ MyUI() })
    Session.loginManager.login(User.findByUsername(username)!!)
    Page.getCurrent().reload()
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
    // in fact, by default the WelcomeView should be displayed
    _get<WelcomeView>()
}

class AdminViewTest : DynaTest({
    beforeGroup { autoDiscoverViews("com.example.vok"); Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }

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
