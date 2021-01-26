package com.github.lamba92.ktor.features

import io.ktor.application.*
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.acceptItems
import io.ktor.request.uri
import io.ktor.response.ApplicationSendPipeline
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.routing
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths

/**
 * The SPA configuration class.
 * @param configuration The object configured by the install lambda.
 */
class SinglePageApplication(private val configuration: Configuration) {

    companion object Feature : ApplicationFeature<Application, Configuration, SinglePageApplication> {

        override val key = AttributeKey<SinglePageApplication>("SinglePageApplication")

        override fun install(
            pipeline: Application,
            configure: Configuration.() -> Unit
        ): SinglePageApplication {

            val feature = SinglePageApplication(Configuration().apply(configure))

            pipeline.routing {
                static(feature.configuration.spaRoute) {
                    if (feature.configuration.useFiles)
                        files(feature.configuration.folderPath)
                    else
                        resources(feature.configuration.folderPath)
                }
            }

            pipeline.intercept(ApplicationCallPipeline.Fallback) {
                if (call.response.status() == null) {
                    call.respond(HttpStatusCodeContent(HttpStatusCode.NotFound))
                    finish()
                }
            }

            pipeline.sendPipeline.intercept(ApplicationSendPipeline.Before) { message ->
                feature.intercept(this, message)
            }

            return feature
        }

    }

    private suspend fun intercept(
        pipelineContext: PipelineContext<Any, ApplicationCall>,
        message: Any
    ) = with(pipelineContext) context@{

        val requestUrl = call.request.uri
        val regex = configuration.ignoreIfContains
        val stop by lazy {
            !((regex == null || requestUrl.notContains(regex))
                    && (requestUrl.startsWith(configuration.spaRoute)
                    || requestUrl.startsWith("/${configuration.spaRoute}")))
        }
        val is404 by lazy {
            if (message is HttpStatusCode)
                message == HttpStatusCode.NotFound
            else
                false
        }
        val acceptsHtml by lazy {
            call.request.acceptItems().any {
                ContentType.Text.Html.match(it.value)
            }
        }

        if (call.attributes.contains(StatusPages.key) || stop || !is404 || !acceptsHtml)
            return@context

        call.attributes.put(key, this@SinglePageApplication)

        if (configuration.useFiles) {
            val file = configuration.fullPath().toFile()
            if (file.notExists()) throw FileNotFoundException("${configuration.fullPath()} not found")
            call.respondFile(File(configuration.folderPath), configuration.defaultPage)
        } else {
            val indexPageApplication = call.resolveResource(configuration.fullPath().toString())
                ?: throw FileNotFoundException("${configuration.fullPath()} not found")
            call.respond(indexPageApplication)
        }
        finish()
    }

    data class Configuration(
        var spaRoute: String = "",
        var useFiles: Boolean = false,
        var folderPath: String = "",
        var defaultPage: String = "index.html",
        var ignoreIfContains: Regex? = null
    ) {
        fun fullPath(): Path = Paths.get(folderPath, defaultPage)
    }

}
