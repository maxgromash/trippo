package edu.project.trippo.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class DestinationVO(
    val id: Long,
    val city: String,
    val country: String,
    val cardImageLink: String,
    val averageTemp: Double,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timeDiff: String,
    var isStared: Boolean,
    var seaExist: Boolean,
    var isPopular: Boolean,
    val hotels: Hotels,
    val bestLocations: List<BestLocationDTO>,
    val conformity: Conformity? = null,
    val distance: Int,
    val engLevel: Int,
    val corona: String
)

