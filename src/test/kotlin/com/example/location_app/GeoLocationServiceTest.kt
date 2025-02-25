package com.example.location_app

import com.example.location_app.api.request.GeoLocationRequest
import com.example.location_app.domain.GeoLocation
import com.example.location_app.repository.GeoLocationRepository
import com.example.location_app.service.LocationService
import com.example.location_app.service.RiderGeoLocationNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GeoLocationServiceTest {

    private lateinit var locationService: LocationService
    private val locationRepository = mockk<GeoLocationRepository>(relaxed = true)

    @BeforeEach
    fun setup() {
        locationService = LocationService(locationRepository)
    }

    @Test
    fun `should save rider location successfully`() {
        val riderId = "123"
        val request = GeoLocationRequest(40.7128, -74.0060)
        val expectedLocation = GeoLocation(40.7128, -74.0060)

        every { locationRepository.saveLocation(riderId, any()) } returns expectedLocation

        val result = locationService.saveRiderLocation(riderId, request)

        assertEquals(expectedLocation, result)
        verify { locationRepository.saveLocation(riderId, any()) }
    }

    @Test
    fun `should return saved rider location`() {
        val riderId = "123"
        val expectedLocation = GeoLocation(40.7128, -74.0060)

        every { locationRepository.getLocation(riderId) } returns expectedLocation

        val result = locationService.getRiderLocation(riderId)

        assertEquals(expectedLocation, result)
        verify { locationRepository.getLocation(riderId) }
    }

    @Test
    fun `should throw RiderGeoLocationNotFoundException if rider location is not found`() {
        val riderId = "999"

        every { locationRepository.getLocation(riderId) } returns null

        assertThrows<RiderGeoLocationNotFoundException> {
            locationService.getRiderLocation(riderId)
        }
    }
}
