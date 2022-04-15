package edu.project.trippo.model

import java.io.Serializable

data class Quiz(
    val title: String,
    val resId: Int,
    val quizNum: Int,
    val variantA: String,
    val variantB: String,
    val variantC: String? = null,
    val variantD: String? = null
) : Serializable