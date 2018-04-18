package com.example.vok

import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.label
import com.github.vok.security.AllowRoles
import com.vaadin.navigator.View
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * Demoes a view intended for both users and admins.
 */
@AutoView
@AllowRoles("user", "admin")
class UserView : VerticalLayout(), View {
    init {
        label("Important content for users") {
            styleName = ValoTheme.LABEL_H1
        }
        label("A page intended for users only. Only users and admins can see this view.")
    }
}
