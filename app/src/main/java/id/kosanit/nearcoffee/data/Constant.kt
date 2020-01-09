package id.kosanit.nearcoffee.data

class Constant {

    companion object {
        val WEB_URL = "http://kosanit.id"

        val WEB_URL_API = "$WEB_URL/nearcoffee/api"

        //auth
        val LOGIN = "$WEB_URL_API/auth/login"
        val REGISTER = "$WEB_URL_API/auth/login"
        val PROFILE = "$WEB_URL_API/auth/me"

        //form
        val COFFEE_SHOP = "$WEB_URL_API/shop"
        val REVIEW = "$WEB_URL_API/shop/review"
    }
}