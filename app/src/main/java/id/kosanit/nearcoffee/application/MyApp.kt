package id.kosanit.nearcoffee.application

import android.annotation.SuppressLint
import android.app.Application
import android.os.StrictMode
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor


class MyApp : Application() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {
        super.onCreate()

        AndroidNetworking.initialize(this)
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        //AndroidNetworking.enableLogging()

        if (android.os.Build.VERSION.SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
    }

}