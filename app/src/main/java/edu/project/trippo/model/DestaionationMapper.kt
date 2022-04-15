package edu.project.trippo.model

import edu.project.trippo.extensions.getConformity

fun DestinationDTO.map(preferences: Preferences): DestinationVO =
    DestinationVO(
        id = "$city$country".hashCode().toLong(),
        city = city,
        country = country,
        cardImageLink = cardImageLink,
        averageTemp = averageTemp[preferences.month],
        description = description,
        latitude = latitude,
        longitude = longitude,
        isStared = false,
        isPopular = isPopular,
        seaExist = seaExist,
        bestLocations = bestLocations,
        hotels = hotels,
        conformity = getConformity(preferences),
        distance = distance,
        timeDiff = timeDiff,
        engLevel = engLevel,
        corona = corona
    )