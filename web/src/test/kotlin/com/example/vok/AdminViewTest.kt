package com.example.vok

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._expectNone
import com.github.karibu.testing._get
import com.github.mvysny.dynatest.DynaTest
import com.github.vok.framework.LoginForm
import com.github.vok.framework.Session
import com.github.vok.karibudsl.navigateToView
import com.github.vokorm.deleteAll

fun login(username: String) {
    MockVaadin.setup({
        Session.loginManager.login(User.findByUsername(username)!!)
        MyUI()
    })
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
}

class AdminViewTest : DynaTest({
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }

    test("Admin should see AdminView properly") {
        login("admin")
        navigateToView<AdminView>()
        _get<AdminView>()
    }

    test("User should not see AdminView") {
        login("user")
        navigateToView<AdminView>()
    }
})
