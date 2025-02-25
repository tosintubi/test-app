package com.example.location_app

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GeoLocationControllerTest {

    @LocalServerPort
    var port: Int = 0

    @BeforeAll
    fun setup(){
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    fun `should save rider location and return 200`() {
        val riderId = "123"
        val requestJson = """{
            "longitude": 80.0089,
            "latitude": 22.3832
        }""".trimIndent()

        Given {
            contentType(ContentType.JSON)
            body(requestJson)
        } When {
            post("/location/$riderId")
        } Then {
            statusCode(200)
            contentType(ContentType.JSON)
            body("location.longitude", equalTo(80.0089f))
            body("location.latitude", equalTo(22.3832f))
            body("timeStamp", notNullValue())
        }
    }

    @Test
    fun `should return 400 when request is invalid`() {
        val riderId = "123"
        val invalidRequestJson = """{
        "longitude": 200.0, 
        "latitude": 22.3832
        }""".trimIndent() // Invalid longitude

        Given {
            contentType(ContentType.JSON)
            body(invalidRequestJson)
        } When {
            post("/location/$riderId")
        } Then {
            statusCode(400)
            contentType(ContentType.JSON)
            body("errorCode", equalTo("INVALID_REQUEST"))
            body("timeStamp", notNullValue())
        }
    }

    @Test
    fun `should return 404 when rider location not found`() {
        val nonExistingRiderId = "999"

        Given {
            contentType(ContentType.JSON)
        } When {
            get("/location/$nonExistingRiderId")
        } Then {
            statusCode(404)
            contentType(ContentType.JSON)
            body("errorCode", equalTo("RIDER_LOCATION_NOT_FOUND"))
            body("timeStamp", notNullValue())
        }
    }
}
