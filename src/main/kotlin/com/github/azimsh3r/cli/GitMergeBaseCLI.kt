package com.github.azimsh3r.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main

class GitMergeBaseCLI: CliktCommand() {
    override fun run() {
        //TODO: run the feature as a command
    }
}

fun main(args: Array<String>) = GitMergeBaseCLI().main(args)
