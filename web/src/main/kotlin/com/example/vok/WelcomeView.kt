package com.example.vok

import com.github.mvysny.karibudsl.v8.*
import com.vaadin.navigator.View
import com.vaadin.shared.Version
import com.vaadin.ui.Alignment
import com.vaadin.ui.Composite
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import eu.vaadinonkotlin.security.AllowAllUsers

/**
 * A view which can be seen by all logged-in users.
 */
@AutoView("")
@AllowAllUsers
class WelcomeView : Composite(), View {
    private val root = verticalLayout {
        setSizeFull()
        isMargin = false
        verticalLayout {
            alignment = Alignment.MIDDLE_CENTER
            isMargin = false; isSpacing = true; defaultComponentAlignment = Alignment.MIDDLE_CENTER
            label("Yay! You're on Vaadin-on-Kotlin!") {
                styleName = ValoTheme.LABEL_H1
            }
            label("This is a welcome view for all users; all logged-in users can see this content")
            label { html("<strong>Vaadin version: </strong> ${Version.getFullVersion()}") }
            label { html("<strong>Kotlin version: </strong> ${KotlinVersion.CURRENT}") }
            label { html("<strong>JVM version: </strong> $jvmVersion") }
        }
    }
}

val jvmVersion: String get() = System.getProperty("java.version")
