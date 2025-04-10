package com.github.azimsh3r.core.remote

import com.github.azimsh3r.core.exceptions.RepositoryNotFoundException
import com.github.azimsh3r.core.exceptions.UnauthorizedException
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GitHubRemoteApiClientTest {
    private val gitHubRemoteGitApiClient = GitHubRemoteGitApiClient()

    @Test
    fun `test UnauthorizedException due to wrong access token`() {
        val mockEngine = MockEngine { _ ->
            respond(
                "{\"message\":\"Bad credentials\",\"documentation_url\":\"https://docs.github.com/rest\",\"status\":\"401\"}",
                HttpStatusCode.Unauthorized
            )
        }
        gitHubRemoteGitApiClient.httpClient=HttpClient(mockEngine)

        assertThrows<UnauthorizedException> {
            runBlocking {
                gitHubRemoteGitApiClient.getChangedFilesSinceMergeBase("azimsh3r", "testRepo", "mergeBase", "master", "token")
            }
        }
    }

    @Test
    fun `test RepositoryNotFoundException due to wrong Owner or Repo`() {
        val mockEngine = MockEngine { _ ->
            respond(
                "{\"message\":\"Not Found\",\"documentation_url\":\"https://docs.github.com/rest/commits/commits#compare-two-commits\",\"status\":\"404\"}",
                HttpStatusCode.NotFound
            )
        }
        gitHubRemoteGitApiClient.httpClient = HttpClient(mockEngine)

        assertThrows<RepositoryNotFoundException> {
            runBlocking {
                gitHubRemoteGitApiClient.getChangedFilesSinceMergeBase("azimsh3r", "testRepo", "mergeBase", "master", "token")
            }
        }
    }

    @Test
    fun `test extractFilePaths`() {
        val diffText = """
            diff --git a/file1.txt b/file1.txt
            --- a/file1.txt
            +++ b/file1.txt
            @@ -0,0 +1,1 @@
            +File content here
        """.trimIndent()

        val filePaths = gitHubRemoteGitApiClient.extractFilePaths(diffText)

        assertEquals(listOf("file1.txt"), filePaths)
    }
}
