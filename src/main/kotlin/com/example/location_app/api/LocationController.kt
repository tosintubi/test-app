package com.example.location_app.api

import com.example.location_app.api.request.GeoLocationRequest
import com.example.location_app.api.response.GeoLocationResponse
import com.example.location_app.domain.toLocationResponse
import com.example.location_app.service.LocationService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/location")
@Validated
class LocationController(
    private val locationService: LocationService
) {
    companion object{
        private val logger = KotlinLogging.logger {}
    }
    @PostMapping("/{riderId}")
    fun saveLocationForRider(
        @PathVariable riderId: String,
        @Valid @RequestBody geoLocationRequest: GeoLocationRequest
    ): ResponseEntity<GeoLocationResponse> {
        logger.info { "Received request to save rider location riderid=$riderId, location=$geoLocationRequest" }
        val response = locationService.saveRiderLocation(riderId, geoLocationRequest).toLocationResponse()
        return  ResponseEntity.ok(response)
    }


    @GetMapping("/{riderId}")
    fun getRiderLocation(
        @PathVariable riderId: String
    ): ResponseEntity<GeoLocationResponse> {
        logger.info { "Fetching location for riderId: $riderId" }
        val location = locationService.getRiderLocation(riderId)
        return ResponseEntity.ok(location.toLocationResponse())
    }

}
