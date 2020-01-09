package id.kosanit.nearcoffee.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import id.kosanit.nearcoffee.activity.MainActivity
import id.kosanit.nearcoffee.model.login.Login

class Session(context: Context) {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    private var _context: Context = context

    var PRIVATE_MODE = 0

    companion object {
        private val PREF_NAME = "my-pref"

        private val IS_LOGIN = "IsLoged"
        private val IS_FIRST = "IsFisrt"
        private val IS_NOT_ALARMT = "IsAlarm"

        val KEY_USER = "user"
        val KEY_ALARM = "alarm"
        val KEY_IP = "IP"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun createLoginSession(user: Login) {
        val gson = Gson()
        val json = gson.toJson(user)

        editor.putString(KEY_USER, json)

        editor.putBoolean(IS_LOGIN, true)

        editor.commit()
    }

    fun getUser(): Login {
        val gson = Gson()
        val json = pref.getString(KEY_USER, "")
        return gson.fromJson<Login>(json, Login::class.java!!)
    }

    fun checkLogin() {
        if (isLoggedIn()) {
            var intent = Intent(_context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            _context.startActivity(intent)
        }
    }

    fun clearAlarm() {
        pref.edit().remove(KEY_ALARM).commit()
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        editor.putBoolean(IS_LOGIN, false)


        //login activity
        val i = Intent(_context, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        _context.startActivity(i)
    }


    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }


    fun setIsLogin(v: Boolean?) {
        editor.putBoolean(IS_LOGIN, v!!)
        editor.commit()
    }

    fun isFirst(): Boolean {
        return pref.getBoolean(IS_FIRST, false)
    }

    fun isAlarm(): Boolean {
        return pref.getBoolean(IS_NOT_ALARMT, false)
    }

    fun getIp(): String {
        return pref.getString(KEY_IP, "192.168.1.1")
    }

    fun setIsFisrt(v: Boolean?) {
        editor.putBoolean(IS_FIRST, v!!)
        editor.commit()
    }

    fun setAlarm(v: Boolean?) {
        editor.putBoolean(IS_NOT_ALARMT, v!!)
        editor.commit()
    }

    fun setIp(ip: String) {
        editor.putString(KEY_IP, ip)
        editor.commit()
    }


}