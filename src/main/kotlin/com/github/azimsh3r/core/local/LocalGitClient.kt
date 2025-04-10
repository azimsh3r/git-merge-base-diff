package com.github.azimsh3r.core.local

import com.github.azimsh3r.core.exceptions.LocalGitRepoNotAccessible
import com.github.azimsh3r.utils.CommandRunner
import kotlinx.io.IOException

class LocalGitClient {
    fun getMergeBase(localRepoPath: String, branchA: String, branchB: String): String {
        return try {
            CommandRunner
                .runCommand(listOf("git", "merge-base", branchA, branchB), localRepoPath)
                .trim()
        } catch (e: Exception) {
            throw LocalGitRepoNotAccessible(localRepoPath)
        }
    }

    fun getLocalChangedFiles(localRepoPath: String, mergeBase: String, branchB: String): List<String> {
        return try {
            CommandRunner.runCommand(listOf("git", "diff", "--name-only", mergeBase, branchB), localRepoPath)
                .trim()
                .lines()
                .filter { it.isNotBlank() }
        } catch (e: IOException) {
            throw LocalGitRepoNotAccessible(localRepoPath)
        }
    }
}
