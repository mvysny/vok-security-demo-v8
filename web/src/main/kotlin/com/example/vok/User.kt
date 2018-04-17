package com.example.vok

import com.github.vok.framework.Session
import com.github.vok.security.simple.HasPassword
import com.github.vokorm.Dao
import com.github.vokorm.Entity
import com.github.vokorm.findSpecificBy
import com.vaadin.server.Page
import java.io.Serializable

/**
 * Represents an user.
 * @property username user name, unique
 * @property roles comma-separated list of roles
 */
data class User(override var id: Long? = null,
                var username: String = "",
                override var hashedPassword: String = "",
                var roles: String = "") : Entity<Long>, HasPassword {
    companion object : Dao<User> {
        fun findByUsername(username: String): User? = findSpecificBy { User::username eq username }
    }
}

class LoginManager : Serializable {
    var user: User? = null
    private set

    val isLoggedIn: Boolean get() = user != null

    fun login(user: User) {
        this.user = user
        Page.getCurrent().reload()  // this will cause the UI to be re-created, but the user is now logged in so the MainLayout should be instantiated etc.
    }

    fun logout() {
        Session.current.close()
        Page.getCurrent().reload()
    }
}

val Session.loginManager: LoginManager get() = getOrPut { LoginManager() }
