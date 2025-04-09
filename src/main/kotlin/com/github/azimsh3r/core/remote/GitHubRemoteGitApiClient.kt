package com.github.azimsh3r.core.remote

import com.github.azimsh3r.core.exceptions.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.regex.Pattern

class GitHubRemoteGitApiClient : RemoteGitApiClient {
    private var httpClient = HttpClient(CIO)

    override suspend fun getChangedFilesSinceMergeBase(
        owner: String,
        repo: String,
        mergeBase: String,
        branch: String,
        token: String?
    ): List<String> {
        val response = httpClient.get("https://api.github.com/repos/$owner/$repo/compare/$mergeBase...$branch") {
            headers {
                token?.let { append(HttpHeaders.Authorization, "token $it") }
                append(HttpHeaders.Accept, "application/vnd.github.diff")
            }
        }

        when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException()
            HttpStatusCode.NotFound -> throw RepositoryNotFoundException()
            HttpStatusCode.BadRequest -> throw BadRequestException()
            else -> if (!response.status.isSuccess()) {
                throw UnexpectedResponseException(response.status)
            }
        }

        return extractFilePaths(response.bodyAsText())
    }

    private fun extractFilePaths(responseBody: String): List<String> {
        return Pattern.compile("\\+\\+\\+ b/(.*?)\n")
            .matcher(responseBody)
            .let { matcher ->
                mutableListOf<String>().apply {
                    while (matcher.find())
                        add(matcher.group(1))
                }
            }
    }
}
