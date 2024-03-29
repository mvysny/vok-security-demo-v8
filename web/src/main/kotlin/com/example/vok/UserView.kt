package com.example.vok

import com.github.mvysny.karibudsl.v8.AutoView
import com.github.mvysny.karibudsl.v8.label
import com.github.mvysny.karibudsl.v8.verticalLayout
import com.vaadin.navigator.View
import com.vaadin.ui.Composite
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import eu.vaadinonkotlin.security.AllowRoles

/**
 * Demoes a view intended for both users and admins.
 */
@AutoView
@AllowRoles("user", "admin")
class UserView : Composite(), View {
    private val root = verticalLayout {
        label("Important content for users") {
            styleName = ValoTheme.LABEL_H1
        }
        label("A page intended for users only. Only users and admins can see this view.")
    }
}
