import com.github.azimsh3r.utils.CommandRunner
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class MergeBaseDiffTest {
    private lateinit var mergeBaseDiff: MergeBaseDiff
    private val mockHttpClient: HttpClient = mockk()
    private lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        tempDir = Files.createTempDirectory("repo")

        val mockEngine = MockEngine { _ ->
            respond(
                """diff --git a/file1.txt b/file1.txt
                   --- a/file1.txt
                   +++ b/file1.txt
                   @@ -0,0 +1,1 @@
                   +File content here
                """.trimIndent(),
                HttpStatusCode.OK
            )
        }

        mergeBaseDiff = MergeBaseDiff().apply {
            this.httpClient = mockHttpClient
        }
    }

    @AfterEach
    fun tearDown() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `test findChangedFiles success`() {
        every { CommandRunner.runCommand(listOf("git", "merge-base", "branchA", "branchB"), tempDir.toString()) } returns "mergeBaseHash\n"

        val localFiles = listOf("file1.txt")
        every { mergeBaseDiff.getLocalChangedFiles(tempDir.toString(), "mergeBaseHash", "branchB") } returns localFiles

        val expectedRemoteFiles = listOf("file1.txt")
        coEvery { mergeBaseDiff.getRemoteChangedFiles("owner", "repo", "mergeBaseHash", "branchA", "token") } returns expectedRemoteFiles

        val result = mergeBaseDiff.findChangedFiles(
            owner = "owner",
            repo = "repo",
            accessToken = "token",
            localRepoPath = tempDir.toString(),
            branchA = "branchA",
            branchB = "branchB"
        )

        assertEquals(listOf("file1.txt"), result)
    }

    @Test
    fun `test findChangedFiles failure due to CommandRunner error`() {
        every { CommandRunner.runCommand(listOf("git", "merge-base", "branchA", "branchB"), tempDir.toString()) } throws RuntimeException("Command failed")

        val result = mergeBaseDiff.findChangedFiles(
            owner = "owner",
            repo = "repo",
            accessToken = "token",
            localRepoPath = tempDir.toString(),
            branchA = "branchA",
            branchB = "branchB"
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test findChangedFiles failure due to HTTP client error`() {
        coEvery { mergeBaseDiff.getRemoteChangedFiles("owner", "repo", "mergeBaseHash", "branchA", "token") } throws IOException("Network error")

        val result = mergeBaseDiff.findChangedFiles(
            owner = "owner",
            repo = "repo",
            accessToken = "token",
            localRepoPath = tempDir.toString(),
            branchA = "branchA",
            branchB = "branchB"
        )

        assertTrue(result.isEmpty())
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

        val filePaths = mergeBaseDiff.extractFilePaths(diffText)

        assertEquals(listOf("file1.txt"), filePaths)
    }
}
