package com.smh.unittest

import com.smh.unittest.data.auth.AuthService
import com.smh.unittest.data.auth.Role
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthServiceTest {

    private val authService = mockk<AuthService>()

    @Test
    fun `test authentication success`() {
        every { authService.authenticate("user1", "Password1!") } returns true

        val isAuthenticated = authService.authenticate("user1", "Password1!")
        assertEquals(true, isAuthenticated)
    }

    @Test
    fun `test authentication failure`() {
        every { authService.authenticate("user1", "wrongpassword") } returns false

        val isAuthenticated = authService.authenticate("user1", "wrongpassword")
        assertEquals(false, isAuthenticated)
    }

    @Test
    fun `test authorization success for admin`() {
        every { authService.authorize("admin", Role.ADMIN) } returns true

        val isAuthorized = authService.authorize("admin", Role.ADMIN)
        assertEquals(true, isAuthorized)
    }

    @Test
    fun `test authorization failure for user attempting admin access`() {
        every { authService.authorize("user1", Role.ADMIN) } returns false

        val isAuthorized = authService.authorize("user1", Role.ADMIN)
        assertEquals(false, isAuthorized)
    }
}
