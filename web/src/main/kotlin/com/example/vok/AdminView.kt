package com.example.vok

import com.github.vok.framework.sql2o.vaadin.dataProvider
import com.github.vok.framework.sql2o.vaadin.generateFilterComponents
import com.github.vok.karibudsl.*
import com.github.vok.security.AllowRoles
import com.vaadin.navigator.View
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@AutoView
@AllowRoles("admin")
class AdminView : VerticalLayout(), View {
    init {
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
