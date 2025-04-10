package com.github.azimsh3r.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.azimsh3r.api.GitMergeBaseDiffer

class DiffCommand : CliktCommand() {
    private val owner : String by option("--owner", help = "Repo owner").required()
    private val repo : String by option("--repo", help = "Repo name").required()
    private val token : String? by option("--token", help = "Access token")
    private val path : String by option("--path", help = "Path to local repo").required()
    private val branchA : String by option("--branchA", help = "branchA").required()
    private val branchB : String by option("--branchB", help = "branchB").required()

    private val gitMergeDiffer  = GitMergeBaseDiffer()

    override fun run() {
        val files = gitMergeDiffer.findCommonChangedFiles(owner, repo, token, path, branchA, branchB)
        files.forEach { println(it) }
    }
}

fun main(args: Array<String>) = DiffCommand().main(args)