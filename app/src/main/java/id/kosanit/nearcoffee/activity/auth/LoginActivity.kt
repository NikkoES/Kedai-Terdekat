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
import id.kosanit.nearcoffee.data.Constant.Companion.LOGIN
import id.kosanit.nearcoffee.data.Session
import id.kosanit.nearcoffee.model.BaseModel
import id.kosanit.nearcoffee.model.login.LoginResponse
import id.kosanit.nearcoffee.util.CommonUtil
import id.kosanit.nearcoffee.util.DialogUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.Response

class LoginActivity : AppCompatActivity() {

    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = Session(this)

        initActionButton()
    }

    private fun initActionButton() {
        btn_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        btn_login.setOnClickListener {
            val list = ArrayList<View>()
            list.add(et_input_email)
            list.add(et_input_password)
            if (CommonUtil.validateEmptyEntries(list)) {
                login()
            }
        }
    }

    private fun login() {
        DialogUtil.openDialog(this)
        AndroidNetworking.post(LOGIN)
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter("email", et_input_email.text.toString())
            .addBodyParameter("password", et_input_password.text.toString())
            .build()
            .getAsOkHttpResponseAndObject(
                LoginResponse::class.java,
                object : OkHttpResponseAndParsedRequestListener<LoginResponse> {
                    override fun onResponse(okHttpResponse: Response?, response: LoginResponse?) {
                        if (okHttpResponse != null) {
                            if (okHttpResponse.code() == 200) {
                                if (response != null) {
                                    session.createLoginSession(response.data[0])

                                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    finish()
                                    startActivity(i)
                                }
                            } else {
                                DialogUtil.closeDialog()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Failed to login !",
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
                            this@LoginActivity,
                            "Sorry, server is busy, please try again later !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
    }
}
