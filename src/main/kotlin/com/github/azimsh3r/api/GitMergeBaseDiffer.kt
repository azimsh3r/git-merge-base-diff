package com.github.azimsh3r.api

import com.github.azimsh3r.core.remote.GitHubRemoteGitApiClient
import com.github.azimsh3r.core.remote.RemoteGitApiClient
import com.github.azimsh3r.core.local.LocalGitClient
import kotlinx.coroutines.runBlocking

class GitMergeBaseDiffer {
    var localGitClient = LocalGitClient()
    var remoteGitApiClient : RemoteGitApiClient = GitHubRemoteGitApiClient()

    @Throws(RuntimeException::class)
    fun findCommonChangedFiles(
        owner: String,
        repo: String,
        accessToken: String?,
        localRepoPath: String,
        branchA: String,
        branchB: String
    ): List<String> {
        val mergeBase : String = localGitClient.getMergeBase(localRepoPath, branchA, branchB)

        val localChangedFiles : List<String> = localGitClient.getLocalChangedFiles(localRepoPath, mergeBase, branchB)
        val remoteChangedFiles : List<String> = runBlocking { remoteGitApiClient.getChangedFilesSinceMergeBase(owner, repo, mergeBase, branchA, accessToken) }

        return localChangedFiles.intersect(remoteChangedFiles.toSet()).toList()
    }
}
