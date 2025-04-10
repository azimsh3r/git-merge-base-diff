# Git Merge Base Differ

**Git Merge Base Differ** is a Kotlin library and CLI tool that detects files changed independently in both a local and remote Git branch since their common ancestor (merge base). It avoids fetching remote branches and uses only command-line Git and HTTP requests.

---

## 🔧 What It Does

Given a local Git repository and two branches:
- `branchA` (exists both remotely and locally, but may have diverged)
- `branchB` (created locally from `branchA`)

The tool finds files that were modified in **both** branches since their common base commit, based on:
- Local changes via Git CLI
- Remote changes via GitHub API

---

## 🚀 CLI Usage

You can run the CLI using Gradle:

```bash
./gradlew run --args="--owner azimsh3r --repo testRepo --path C:\\Users\\Azim\\Desktop\\testRepoHeree --branchA master --branchB azim"
```

View available options:

```bash
./gradlew run --args="--help"
```

---

## 🧩 API Usage

This library can be used directly in your Kotlin/Java project:

```kotlin
val differ = GitMergeBaseDiffer()
val changedFiles = differ.findCommonChangedFiles(
    owner = "azimsh3r",
    repo = "repo",
    accessToken = "your-token",
    localRepoPath = "path",
    branchA = "master",
    branchB = "azim"
)
```

---

## ✅ Tests

The project includes unit tests for core functionality using **JUnit 5** and **MockK**.

Run tests with:

```bash
./gradlew test
```

---

## 🤝 Contribution

Contributions are welcome. Please keep the code clean, tested, and modular.

---
