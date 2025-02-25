package com.example.location_app.service

import com.example.location_app.api.request.GeoLocationRequest
import com.example.location_app.domain.GeoLocation
import com.example.location_app.repository.GeoLocationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Instant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@Service
class LocationService(
    private val locationRepository: GeoLocationRepository
) {

    fun saveRiderLocation(riderId: String, request: GeoLocationRequest): GeoLocation{
        val location = GeoLocation(
            longitude = request.longitude,
            latitude = request.latitude // ✅ Corrected this
        )
        return locationRepository.saveLocation(riderId, location)
    }

    fun getRiderLocation(riderId: String): GeoLocation {
        return locationRepository.getLocation(riderId)
            ?: throw RiderGeoLocationNotFoundException("Rider location not found for riderId=$riderId")
    }
}


class RiderGeoLocationNotFoundException(
    val riderId: String
): RuntimeException("Rider location not found for ID: $riderId")


// Logger: 	implementation("io.github.oshai:kotlin-logging-jvm")


@RestControllerAdvice
class ExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RiderGeoLocationNotFoundException::class)
    fun handleRiderNotFoundException(ex: RiderGeoLocationNotFoundException): ErrorResponse {
        logger.warn { "Rider location not found for rider:${ex.riderId}" }
        return ErrorResponse(
            errorCode = "RIDER_LOCATION_NOT_FOUND",
            message = "Location not found for rider:",
        )
    }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessage = ex.bindingResult
            .fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        logger.warn { "Validation failed: $errorMessage" }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse("INVALID_REQUEST", errorMessage, Instant.now().epochSecond))
    }
}


data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val timeStamp: Long = Instant.now().epochSecond
)
