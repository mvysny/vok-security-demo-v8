package com.example.vok

import com.github.mvysny.karibudsl.v8.*
import com.vaadin.navigator.View
import com.vaadin.ui.Composite
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import eu.vaadinonkotlin.security.AllowRoles
import eu.vaadinonkotlin.vaadin8.generateFilterComponents
import eu.vaadinonkotlin.vaadin8.sql2o.dataProvider

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@AutoView
@AllowRoles("admin")
class AdminView : Composite(), View {
    private val root = verticalLayout {
        setSizeFull()

        label("Administration pages") {
            styleName = ValoTheme.LABEL_H1
        }
        grid<User>(caption = "Users", dataProvider = User.dataProvider) {
            expandRatio = 1f; setSizeFull()

            addColumnFor(User::id)
            addColumnFor(User::username)
            addColumnFor(User::roles)
            addColumnFor(User::hashedPassword)

            appendHeaderRow().generateFilterComponents(this, User::class)
        }
    }
}
