package edu.project.trippo.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class DestinationDTO(
    val city: String,
    val country: String,
    val cardImageLink: String,
    val averageTemp: List<Double>,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timeDiff: String,
    var seaExist: Boolean,
    val bestLocations: List<BestLocationDTO>,
    var isPopular: Boolean,
    val hotels: Hotels,
    val distance: Int,
    val engLevel: Int,
    val corona: String
)

data class BestLocationDTO(
    val imageLink: String,
    val name: String
)

data class Hotels(
    val threeStar: Int,
    val FourStar: Int,
    val FiveStar: Int
)