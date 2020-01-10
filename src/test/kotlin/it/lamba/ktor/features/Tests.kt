package it.lamba.ktor.features

import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import org.slf4j.event.Level
import kotlin.test.assertEquals

class Tests {

    private val spaRoute = "test"
    private val folderPath = "spa"
    private val defaultPage = "lol.html"

    private fun <R> withSPATestApplication(test: TestApplicationEngine.() -> R) = withTestApplication({
        install(SinglePageApplication) {
            defaultPage = this@Tests.defaultPage
            folderPath = this@Tests.folderPath
            spaRoute = this@Tests.spaRoute
        }
        install(CallLogging) {
            level = Level.DEBUG
        }
    }, test)

    @Test
    fun `test root address 404`() = withSPATestApplication {
        with(handleRequest(Get, "/") {
            this.protocol
        }) {
            assertEquals(HttpStatusCode.NotFound, response.status())
        }
    }

    @Test
    fun `test spa root 200`() = withSPATestApplication {
        with(handleRequest(Get, "/$spaRoute")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}