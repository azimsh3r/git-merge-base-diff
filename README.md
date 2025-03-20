# git-merge-base-diff

## Overview
`git-merge-base-diff` is a Kotlin library that identifies files that have been independently modified in two different branches since their last common ancestor (merge base). It helps detect conflicting changes between a **local** and **remote** branch before merging.

## Features
- Finds the **merge base** commit between two branches.
- Lists files changed in **branchA** (remote) and **branchB** (local).
- Returns the **intersection** of files modified in both branches.
- Uses the GitHub API to fetch remote changes.
- Supports local Git operations using shell commands.

## Installation
Add the library to your Kotlin project:

```kotlin
// Gradle (Kotlin DSL)
dependencies {
    implementation("com.yourname:git-merge-base-diff:1.0.0")
}
```

## Usage
```kotlin
val diffFinder = MergeBaseDiff()
val changedFiles = diffFinder.findChangedFiles(
    owner = "your-github-username",
    repo = "your-repo-name",
    accessToken = "your-github-access-token",
    localRepoPath = "/path/to/local/repo",
    branchA = "main",    // Remote branch
    branchB = "feature"  // Local branch
)

println("Files changed in both branches: $changedFiles")
```

## Requirements
- Kotlin 1.8+
- Git installed on the system
- GitHub access token for API requests

## License
[MIT License](LICENSE)
