package com.example.location_app.api.response

import com.example.location_app.domain.LocationDTO


data class GeoLocationResponse(
    val location: LocationDTO,
    val timeStamp: Long
)
