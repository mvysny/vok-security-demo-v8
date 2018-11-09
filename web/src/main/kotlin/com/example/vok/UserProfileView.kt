package com.example.vok

import com.github.vok.framework.Session
import com.github.mvysny.karibudsl.v8.AutoView
import com.github.mvysny.karibudsl.v8.label
import com.github.vok.security.AllowAllUsers
import com.vaadin.navigator.View
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * The view will simply show the profile of the currently logged in user. Therefore, we can simply allow all users to see this view, since
 * every user will only be able to see his details.
 */
@AutoView("profile")
@AllowAllUsers
class UserProfileView : VerticalLayout(), View {
    init {
        val user: User = Session.loginManager.user!!
        label("${user.username}'s profile") {
            styleName = ValoTheme.LABEL_H1
        }
        label("User name: ${user.username}")
    }
}
