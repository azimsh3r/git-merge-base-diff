package com.github.azimsh3r.api

import com.github.azimsh3r.core.exceptions.GitLocalException
import com.github.azimsh3r.core.exceptions.GitRemoteException
import com.github.azimsh3r.core.remote.GitHubRemoteGitApiClient
import com.github.azimsh3r.core.remote.RemoteGitApiClient
import com.github.azimsh3r.core.local.LocalGitClient
import kotlinx.coroutines.runBlocking

class GitMergeBaseDiffer {
    private val localGitClient = LocalGitClient()
    private val remoteGitApiClient : RemoteGitApiClient = GitHubRemoteGitApiClient()

    @Throws(GitLocalException::class, GitRemoteException::class)
    fun findChangedFiles(
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

//fun main() {
//    val gitMergeBaseDiffer = GitMergeBaseDiffer()
//
//    try {
//        val changedFiles = gitMergeBaseDiffer.findChangedFiles(
//            "azimsh3r",
//            "testRepo",
//            null,
//            "C:\\Users\\Azim\\Desktop\\testRepoHere",
//            "master",
//            "azim"
//        )
//        println(changedFiles)
//    } catch (e : Exception) {
//        e.printStackTrace()
//    }
//}
