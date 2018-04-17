package com.example.vok

import com.github.vok.framework.Session
import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.label
import com.github.vok.security.AllowAllUsers
import com.vaadin.navigator.View
import com.vaadin.ui.VerticalLayout

/**
 * The view will simply show the profile of the currently logged in user. Therefore, we can simply allow all users to see this view, since
 * every user will only be able to see his details.
 */
@AutoView("profile")
@AllowAllUsers
class UserProfileView : VerticalLayout(), View {
    init {
        val user: User = Session.loginManager.user!!
        label("User name: ${user.username}")
    }
}
