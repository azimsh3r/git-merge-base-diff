package com.github.azimsh3r.core.exceptions

import io.ktor.http.*

class UnauthorizedException : RuntimeException("Access token is invalid or missing")
class RepositoryNotFoundException : RuntimeException("The repository could not be found")
class BadRequestException : RuntimeException("The request was malformed")
class UnexpectedResponseException(status: HttpStatusCode) :
    RuntimeException("Unexpected HTTP status: $status")
