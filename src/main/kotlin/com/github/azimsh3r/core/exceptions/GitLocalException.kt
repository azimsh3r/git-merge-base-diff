package com.github.azimsh3r.core.exceptions

class LocalGitRepoNotAccessible(path: String) : RuntimeException("Git repository at $path not accessible")