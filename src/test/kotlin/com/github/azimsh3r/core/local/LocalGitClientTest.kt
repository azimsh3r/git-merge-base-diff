package com.github.azimsh3r.core.local

import com.github.azimsh3r.core.exceptions.LocalGitRepoNotAccessibleException
import com.github.azimsh3r.utils.CommandRunner
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.nio.file.Path

class LocalGitClientTest {
    private val localGitClient = LocalGitClient()
    private lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        tempDir = Files.createTempDirectory("testGitMerge")
    }

    @AfterEach
    fun tearDown() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `test LocalGitRepoNotAccessibleException due to wrong path`() {
        assertThrows<LocalGitRepoNotAccessibleException> {
            localGitClient.getMergeBase(
                tempDir.toAbsolutePath().toString(),
                "branchA",
                "branchB"
            )
        }
    }

    @Test
    fun `test LocalGitRepoNotAccessibleException due to command error`() {
        mockkObject(CommandRunner)

        every {
            CommandRunner.runCommand(any(), any())
        } throws Exception("mocked")

        assertThrows<LocalGitRepoNotAccessibleException> {
            localGitClient.getMergeBase(
                tempDir.toAbsolutePath().toString(),
                "branchA",
                "branchB"
            )
        }
    }
}
