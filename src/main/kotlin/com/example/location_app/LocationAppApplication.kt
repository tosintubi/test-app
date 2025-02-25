package com.example.location_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LocationAppApplication

fun main(args: Array<String>) {
	runApplication<LocationAppApplication>(*args)
}
