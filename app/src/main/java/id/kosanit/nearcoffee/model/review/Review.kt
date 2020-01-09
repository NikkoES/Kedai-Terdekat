package id.kosanit.nearcoffee.model.review

data class Review(
    val coffee_shop_id: String,
    val rating: String,
    val review: String,
    val user_id: String
)