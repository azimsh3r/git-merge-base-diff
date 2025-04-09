package com.github.azimsh3r.core.exceptions

import io.ktor.http.*

sealed class GitRemoteException(message: String) : Exception(message)

class UnauthorizedException : GitRemoteException("Access token is invalid or missing")
class RepositoryNotFoundException : GitRemoteException("The repository could not be found")
class BadRequestException : GitRemoteException("The request was malformed")
class UnexpectedResponseException(status: HttpStatusCode) :
    GitRemoteException("Unexpected HTTP status: $status")

class GitHubApiException(cause: Throwable?) : GitRemoteException("GitHub API error: ${cause?.message}")
