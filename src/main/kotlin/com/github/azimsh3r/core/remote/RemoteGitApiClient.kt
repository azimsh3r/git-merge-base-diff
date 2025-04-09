package com.github.azimsh3r.core.remote

/** Application can be extended to more remote vcs hosts **/
interface RemoteGitApiClient {
    suspend fun getChangedFilesSinceMergeBase(owner: String, repo: String, mergeBase: String, branch: String, token: String?) : List<String>
}
