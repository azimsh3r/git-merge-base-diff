package com.github.azimsh3r.utils

import java.io.File

object CommandRunner {
    fun runCommand(command: List<String>, path: String): String {
        val process = ProcessBuilder(command)
            .directory(File(path))
            .redirectErrorStream(true)
            .start()

        return process.inputStream.bufferedReader().use { it.readText() }
    }
}
