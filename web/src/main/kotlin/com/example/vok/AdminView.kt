package com.example.vok

import com.github.vok.karibudsl.AutoView
import com.github.vok.karibudsl.label
import com.github.vok.security.HasRoles
import com.vaadin.ui.VerticalLayout

@AutoView
@HasRoles("admin")
class AdminView : VerticalLayout() {
    init {
        label("Admin")
    }
}