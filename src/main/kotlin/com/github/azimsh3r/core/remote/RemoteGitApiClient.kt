package com.github.azimsh3r.core.remote

interface RemoteGitApiClient {
    suspend fun getChangedFilesSinceMergeBase(owner: String, repo: String, mergeBase: String, branch: String, token: String?) : List<String>
}
