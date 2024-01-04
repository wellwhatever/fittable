package cz.cvut.fit.fittable.shared.authorization.domain

import cz.cvut.fit.fittable.shared.FakeData
import cz.cvut.fit.fittable.shared.authorization.data.local.AuthorizationLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.local.UsernameLocalDataSource
import cz.cvut.fit.fittable.shared.authorization.data.remote.AuthorizationRoute
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

class SaveUsernameUseCaseTest : FunSpec({

    test("updates username in local data source") {
        // Mock dependencies
        val authorizationRoute = mockk<AuthorizationRoute>()
        val authorizationLocalDataSource = mockk<AuthorizationLocalDataSource>()
        val usernameLocalDataSource = mockk<UsernameLocalDataSource>()

        // Create an instance of the use case
        val saveUsernameUseCase = SaveUsernameUseCase(
            authorizationRoute,
            authorizationLocalDataSource,
            usernameLocalDataSource
        )

        val fakeTokenInformation = FakeData.tokenInformation

        // Stub authorizationLocalDataSource to return a flow with a token
        coEvery {
            authorizationLocalDataSource.authorizationTokenFlow
        } returns MutableStateFlow(FakeData.token)

        // Stub authorizationRoute to return a username
        runTest {
            coEvery {
                authorizationRoute.getTokenInformation(FakeData.token)
            } returns fakeTokenInformation
        }

        // Stub usernameLocalDataSource to verify the updateUsername method is called
        coEvery {
            usernameLocalDataSource.updateUsername(FakeData.userName)
        } returns Unit

        // Call the use case
        runBlocking {
            saveUsernameUseCase()
        }

        // Verify that the updateUsername method was called with the expected username
        coVerify {
            usernameLocalDataSource.updateUsername(FakeData.userName)
        }
    }
})