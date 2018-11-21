package it.lamba.ktor.features

import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.http.content.resolveResource
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import java.io.FileNotFoundException

class SinglePageApplication(private val configuration: Configuration) {

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, SinglePageApplication.Configuration, SinglePageApplication> {

        override val key = AttributeKey<SinglePageApplication>("SinglePageApplication")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): SinglePageApplication {
            val configuration = Configuration().apply(configure)
            val feature = SinglePageApplication(configuration)

            pipeline.intercept(ApplicationCallPipeline.Fallback){
                val requestUrl = call.request.uri
                val regex = configuration.ignoreIfContains
                if(regex == null || !requestUrl.contains(regex))
                    feature.intercept(this)
            }

            return feature
        }

    }

    private suspend fun intercept(
        pipelineContext: PipelineContext<Unit, ApplicationCall>
    ) = pipelineContext.apply {
        val indexPageApplication = call.resolveResource(configuration.defaultPage)
            ?: throw FileNotFoundException("${configuration.defaultPage} not found")
        call.respond(indexPageApplication)
        finish()
    }

    data class Configuration(var defaultPage: String = "index.html", var ignoreIfContains: Regex? = null)

}