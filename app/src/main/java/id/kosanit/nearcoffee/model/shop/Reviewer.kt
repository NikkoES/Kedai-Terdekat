package id.kosanit.nearcoffee.model.shop

import java.io.Serializable

data class Reviewer(
    val id: Int,
    val user_id: Int,
    val rating: Double,
    val review: String,
    val reviewer: String
) : Serializable