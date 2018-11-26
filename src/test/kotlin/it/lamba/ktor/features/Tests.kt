package it.lamba.ktor.features

import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.headers
import io.ktor.client.request.port
import io.ktor.client.response.readBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat
import it.lamba.utils.getResource
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class Tests {

    private val serverAddress = "127.0.0.1"
    private val serverPort = 8080
    private val spaRoute = "test"
    private val folderPath = "spa"
    private val defaultPage = "lol.html"

    private val httpClient = HttpClient(Apache)
    private val resourceServer by lazy {
        embeddedServer(Tomcat, port = serverPort, host = serverAddress){
            install(SinglePageApplication){
                defaultPage = this@Tests.defaultPage
                folderPath = this@Tests.folderPath
                spaRoute = this@Tests.spaRoute
            }
        }
    }

    @Test
    fun testResources() = runBlocking {
        resourceServer.start()

        val expect404Response = httpClient
            .call("http://$serverAddress"){ port = serverPort }
            .response.status
        assertEquals(HttpStatusCode.NotFound.value, expect404Response.value)

        val expect404Response2 = httpClient
            .call("http://$serverAddress/$spaRoute"){
                port = serverPort
                headers {
                    set(HttpHeaders.Accept, ContentType.Application.Json.toString())
                }
            }
            .response.status
        assertEquals(HttpStatusCode.NotFound.value, expect404Response2.value)

        val mainPage = getResource("$folderPath/$defaultPage").readText()
        val mainPageResponse = httpClient
            .call("http://$serverAddress/$spaRoute"){ port = serverPort }
            .response
            .readBytes()
            .let { String(it) }
        assertEquals(mainPage, mainPageResponse)

        val mainPage2 = getResource("$folderPath/$defaultPage").readText()
        val mainPageResponse2 = httpClient
            .call("http://$serverAddress/$spaRoute/LOLOL"){ port = serverPort }
            .response
            .readBytes()
            .let { String(it) }
        assertEquals(mainPage2, mainPageResponse2)

        val staticResource = getResource("$folderPath/static/test.html").readText()
        val staticResourceResponse = httpClient
            .call("http://$serverAddress/$spaRoute/static/test.html"){ port = serverPort }
            .response
            .readBytes()
            .let { String(it) }
        assertEquals(staticResource, staticResourceResponse)

        httpClient.close()
        resourceServer.stop(5, 5, TimeUnit.SECONDS)
        return@runBlocking
    }

}