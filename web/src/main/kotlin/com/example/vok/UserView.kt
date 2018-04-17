package com.example.vok

import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.label
import com.github.vok.security.AllowRoles
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.VerticalLayout

@AutoView
@AllowRoles("user", "admin")
class UserView : VerticalLayout(), View {
    init {
        label("User")
    }
    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }
}
