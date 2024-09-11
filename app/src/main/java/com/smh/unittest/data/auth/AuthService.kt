package com.smh.unittest.data.auth

enum class Role {
    USER, ADMIN
}

class AuthService {
    private val validUsers = mapOf(
        "user1" to "Password1!",
        "admin" to "AdminPassword123!"
    )

    private val userRoles = mapOf(
        "user1" to Role.USER,
        "admin" to Role.ADMIN
    )

    fun authenticate(username: String, password: String): Boolean {
        return validUsers[username] == password
    }

    fun authorize(username: String, roleRequired: Role): Boolean {
        val userRole = userRoles[username] ?: return false
        return userRole >= roleRequired
    }
}
