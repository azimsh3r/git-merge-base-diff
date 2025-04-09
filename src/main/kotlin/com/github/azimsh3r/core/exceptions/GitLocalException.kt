package com.github.azimsh3r.core.exceptions

sealed class GitLocalException(message: String?) : Exception(message)

class LocalGitRepoNotAccessible(path: String) : GitLocalException("Git repository at $path not accessible")