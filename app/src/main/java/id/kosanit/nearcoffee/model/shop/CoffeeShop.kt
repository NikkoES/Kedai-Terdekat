package id.kosanit.nearcoffee.model.shop

import java.io.Serializable

data class CoffeeShop(
    val address: String,
    val description: String,
    val id: Int,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val menu: String,
    val name: String,
    val phone: String,
    val rating: Double,
    val reviewer: List<Reviewer>,
    var distance: Double
) : Serializable