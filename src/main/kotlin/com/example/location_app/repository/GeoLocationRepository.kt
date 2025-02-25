package com.example.location_app.repository

import com.example.location_app.domain.GeoLocation
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Repository

@Repository
class GeoLocationRepository {

    private val riderToLocationMap = ConcurrentHashMap<String, GeoLocation>()

    fun saveLocation(riderId: String, location: GeoLocation): GeoLocation {
        riderToLocationMap[riderId] = location
        return location
    }

    fun getLocation(riderId: String): GeoLocation? {
        return riderToLocationMap[riderId]
    }
}
