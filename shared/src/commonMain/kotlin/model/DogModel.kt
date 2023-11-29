package model

import kotlinx.serialization.Serializable

@Serializable
data class DogModel(
    val message: List<String>,
    val status: String
)