package com.github.azimsh3r.api

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GitMergeBaseDifferTest {
    private lateinit var differ: GitMergeBaseDiffer

    @BeforeEach
    fun setUp() {
        differ = GitMergeBaseDiffer()
        differ.remoteGitApiClient = mockk()
        differ.localGitClient = mockk()
    }

    @Test
    fun `should return common changed files`() {
        val owner = "azimsh3r"
        val repo = "my-repo"
        val accessToken = "ghp_token"
        val localRepoPath = "/path/to/local/repo"
        val branchA = "main"
        val branchB = "feature"
        val mergeBase = "abc123"

        val localFiles = listOf("src/Main.kt", "README.md")
        val remoteFiles = listOf("README.md", "build.gradle")

        every { differ.localGitClient.getMergeBase(localRepoPath, branchA, branchB) } returns mergeBase
        every { differ.localGitClient.getLocalChangedFiles(localRepoPath, mergeBase, branchB) } returns localFiles
        coEvery {
            differ.remoteGitApiClient.getChangedFilesSinceMergeBase(owner, repo, mergeBase, branchA, accessToken)
        } returns remoteFiles

        val result = differ.findCommonChangedFiles(owner, repo, accessToken, localRepoPath, branchA, branchB)

        assertEquals(listOf("README.md"), result)
    }
}
