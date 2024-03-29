package com.example.vok

import com.github.mvysny.karibudsl.v8.AutoView
import com.github.mvysny.karibudsl.v8.label
import com.github.mvysny.karibudsl.v8.verticalLayout
import com.vaadin.navigator.View
import com.vaadin.ui.Composite
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import eu.vaadinonkotlin.security.AllowAllUsers
import eu.vaadinonkotlin.vaadin8.Session

/**
 * The view will simply show the profile of the currently logged in user. Therefore, we can simply allow all users to see this view, since
 * every user will only be able to see his details.
 */
@AutoView("profile")
@AllowAllUsers
class UserProfileView : Composite(), View {
    init {
        val user: User = Session.loginManager.user!!
        verticalLayout {
            label("${user.username}'s profile") {
                styleName = ValoTheme.LABEL_H1
            }
            label("User name: ${user.username}")
        }
    }
}
