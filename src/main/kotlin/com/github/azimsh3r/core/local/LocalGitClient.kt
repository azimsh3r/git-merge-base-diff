package com.github.azimsh3r.core.local

import com.github.azimsh3r.core.exceptions.LocalGitRepoNotAccessibleException
import com.github.azimsh3r.utils.CommandRunner

class LocalGitClient {
    fun getMergeBase(localRepoPath: String, branchA: String, branchB: String): String {
        try {
            val result = CommandRunner
                .runCommand(listOf("git", "merge-base", branchA, branchB), localRepoPath)
                .trim()

            if (result.startsWith("fatal:")) {
                throw LocalGitRepoNotAccessibleException("Invalid Git repository or branch names: $branchA, $branchB at $localRepoPath")
            }

            return result
        } catch (e: Exception) {
            throw LocalGitRepoNotAccessibleException("Error accessing local Git repository at $localRepoPath")
        }
    }

    fun getLocalChangedFiles(localRepoPath: String, mergeBase: String, branchB: String): List<String> {
        return try {
            CommandRunner.runCommand(listOf("git", "diff", "--name-only", mergeBase, branchB), localRepoPath)
                .trim()
                .lines()
                .filter { it.isNotBlank() }
        } catch (e: Exception) {
            throw LocalGitRepoNotAccessibleException(localRepoPath)
        }
    }
}
