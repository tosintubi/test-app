package com.example.location_app.domain

import com.example.location_app.api.response.GeoLocationResponse
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

data class GeoLocation(
    val longitude: Double,
    val latitude: Double,
    @JsonIgnore
    val timeStamp: Instant = Instant.now()
)

fun GeoLocation.toLocationResponse(): GeoLocationResponse =
    GeoLocationResponse(
        location = LocationDTO(
            longitude = this.longitude,
            latitude = this.latitude
        ),
        timeStamp = this.timeStamp.epochSecond
    )

data class LocationDTO(
    val longitude: Double,
    val latitude: Double
)
