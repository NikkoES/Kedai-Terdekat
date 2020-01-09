package id.kosanit.nearcoffee.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.activity.MainActivity
import id.kosanit.nearcoffee.data.Constant
import id.kosanit.nearcoffee.model.login.LoginResponse
import id.kosanit.nearcoffee.util.CommonUtil
import id.kosanit.nearcoffee.util.DialogUtil
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initActionButton()
    }

    private fun initActionButton() {
        btn_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        btn_register.setOnClickListener {
            val list = ArrayList<View>()
            list.add(et_name)
            list.add(et_email)
            list.add(et_phone)
            list.add(et_password)
            if (CommonUtil.validateEmptyEntries(list)) {
                register()
            }
        }
    }

    private fun register() {
        DialogUtil.openDialog(this)
        AndroidNetworking.post(Constant.LOGIN)
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter("name", et_name.text.toString())
            .addBodyParameter("password", et_email.text.toString())
            .addBodyParameter("phone", et_phone.text.toString())
            .addBodyParameter("password", et_password.text.toString())
            .build()
            .getAsOkHttpResponseAndObject(
                LoginResponse::class.java,
                object : OkHttpResponseAndParsedRequestListener<LoginResponse> {
                    override fun onResponse(okHttpResponse: Response?, response: LoginResponse?) {
                        if (okHttpResponse != null) {
                            if (okHttpResponse.code() == 200) {
                                if (response != null) {
                                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                    finish()
                                }
                            } else {
                                DialogUtil.closeDialog()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Failed to register !",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        DialogUtil.closeDialog()
                        if (anError != null) {
                            Log.e(
                                "ERROR",
                                anError.cause.toString() + "" + anError.errorCode + "\n" + anError.errorBody + "\n" + anError.errorDetail
                            )
                        }
                        Toast.makeText(
                            this@RegisterActivity,
                            "Sorry, server is busy, please try again later !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
    }
}
