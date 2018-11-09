package com.shalimov.movieland.service.impl

import com.shalimov.movieland.entity.User
import com.shalimov.movieland.service.SecurityService
import com.shalimov.movieland.service.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import javax.servlet.http.HttpSession

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner.class)
class DefaultSecurityServiceTest {

    @InjectMocks
    DefaultSecurityService securityService
    @Mock
    private HttpSession session

    @Test
    void authenticate() {
        def expectedUser = new User(email: 'ronald.reynolds66@example.com', password: 'ffcb11cd036e610a089a4413b4c8ffaae04ecf4d', salt: "51ecdf44-6809-4733-85f3-2d366005031d")
        def userService = { getByLogin -> Optional.of(expectedUser) } as UserService
        securityService = new DefaultSecurityService(userService)
        def actualUser = securityService.authenticate('ronald.reynolds66@example.com', 'paco', session)
        assertTrue(actualUser.isPresent())
    }

    @Test
    void authenticateInvalidCredentials() {
        def expectedUser = new User(email: 'ronald.reynolds66@example.com', password: 'ffcb11cd036e610a089a4413b4c8ffaae04ecf4d', salt: "51ecdf44-6809-4733-85f3-2d366005031d")
        def userService = { getByLogin -> Optional.of(expectedUser) } as UserService
        SecurityService securityService = new DefaultSecurityService(userService)
        def actualUser = securityService.authenticate('ronald.reynolds66@example.com', 'wrongPass', session)
        assertFalse(actualUser.isPresent())
    }
}
