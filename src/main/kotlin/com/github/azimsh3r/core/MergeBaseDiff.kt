package com.github.azimsh3r.core

import com.github.azimsh3r.utils.CommandRunner
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.util.regex.Pattern

class MergeBaseDiff {
    private val httpClient = HttpClient(CIO)

    fun findChangedFiles(
        owner: String,
        repo: String,
        accessToken: String?,
        localRepoPath: String,
        branchA: String,
        branchB: String
    ): List<String> {
        return runCatching {
            val mergeBase = getMergeBase(localRepoPath, branchA, branchB)
            val localChangedFiles = getLocalChangedFiles(localRepoPath, mergeBase, branchB)
            val remoteChangedFiles = runBlocking { getRemoteChangedFiles(owner, repo, mergeBase, branchA, accessToken) }

            localChangedFiles.intersect(remoteChangedFiles.toSet()).toList()
        }.getOrElse {
            println("Error: ${it.message}")
            emptyList()
        }
    }

    private fun getMergeBase(localRepoPath: String, branchA: String, branchB: String): String {
        return runCatching {
            CommandRunner.runCommand(listOf("git", "merge-base", branchA, branchB), localRepoPath).trim()
        }.getOrElse {
            throw RuntimeException("Failed to get merge base: ${it.message}")
        }
    }

    private fun getLocalChangedFiles(localRepoPath: String, mergeBase: String, branchB: String): List<String> {
        return runCatching {
            CommandRunner.runCommand(listOf("git", "diff", "--name-only", mergeBase, branchB), localRepoPath)
                .trim()
                .lines()
                .filter { it.isNotBlank() }
        }.getOrElse {
            throw RuntimeException("Failed to get local changed files: ${it.message}")
        }
    }

    private suspend fun getRemoteChangedFiles(owner: String, repo: String, mergeBase: String, branchA: String, token: String?): List<String> {
        return runCatching {
            httpClient.get("https://api.github.com/repos/$owner/$repo/compare/$mergeBase...$branchA") {
                headers {
                    token?.let { append(HttpHeaders.Authorization, "token $it") }
                    append(HttpHeaders.Accept, "application/vnd.github.diff")
                }
            }.bodyAsText().let { extractFilePaths(it) }
        }.getOrElse {
            throw RuntimeException("Failed to get remote changed files: ${it.message}")
        }
    }

    private fun extractFilePaths(diff: String): List<String> {
        return Pattern.compile("\\+\\+\\+ b/(.*?)\n")
            .matcher(diff)
            .let { matcher ->
                mutableListOf<String>().apply {
                    while (matcher.find()) add(matcher.group(1))
                }
            }
    }
}
