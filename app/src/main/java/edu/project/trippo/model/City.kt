package edu.project.trippo.model

data class City(
    val englishRate: String,
    val timeDiff: String,
    val description: String,
    val image: String,
    val sights: List<Sight>
) {

    data class Sight(
        val name: String,
        val imageLink: String
    )
}