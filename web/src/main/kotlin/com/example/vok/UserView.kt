package com.example.vok

import com.github.vok.karibudsl.AutoView
import com.github.vok.security.HasRoles
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.VerticalLayout

@AutoView
@HasRoles("user")
class UserView : VerticalLayout(), View {
    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }
}
