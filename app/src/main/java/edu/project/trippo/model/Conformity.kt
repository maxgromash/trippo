package edu.project.trippo.model

data class Conformity(
    var weather: Status,
    var budget: Status,
    var sea: Status,
    var popularity: Status,
    var conformityCount: Int
)

enum class Status {
    YES, PARTIALLY, NO
}