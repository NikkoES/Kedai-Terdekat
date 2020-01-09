package id.kosanit.nearcoffee.model.login

data class LoginResponse(
    val `data`: List<Login>,
    val status: String
)