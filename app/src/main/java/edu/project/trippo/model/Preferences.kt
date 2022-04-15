package edu.project.trippo.model

data class Preferences(
    var temperature: Pair<Double, Double>? = null,
    var hotelBudget: Int? = null,
    var isSeaExist: Boolean? = null,
    var isPopular: Boolean? = null,
    var month: Int = 6
)