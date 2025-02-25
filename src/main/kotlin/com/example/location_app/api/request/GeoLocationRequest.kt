package com.example.location_app.api.request

import com.example.location_app.domain.GeoLocation
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class GeoLocationRequest(
    @field:Min(value = -90, message = "value should not be less than -90")
    @field:Max(value = 90, message = "value should not be greater than 90")
//    @NotBlank
    val longitude: Double,

    @field:Min(value = -90, message = "value should not be less than -90")
    @field:Max(value = 90, message = "value should not be greater than 90")
//    @field:NotBlank
    val latitude: Double
)

fun GeoLocationRequest.toGeoLocationModel(): GeoLocation =
    GeoLocation(this.longitude, this.latitude)
