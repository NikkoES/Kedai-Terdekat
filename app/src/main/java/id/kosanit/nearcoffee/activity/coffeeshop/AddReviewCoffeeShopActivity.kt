package id.kosanit.nearcoffee.activity.coffeeshop

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.data.Constant.Companion.REVIEW
import id.kosanit.nearcoffee.data.Session
import id.kosanit.nearcoffee.model.BaseModel
import id.kosanit.nearcoffee.model.review.ReviewResponse
import id.kosanit.nearcoffee.util.CommonUtil
import id.kosanit.nearcoffee.util.DialogUtil
import kotlinx.android.synthetic.main.activity_add_review_coffeeshop.*
import okhttp3.Response

class AddReviewCoffeeShopActivity : AppCompatActivity() {

    var id : Int = 0
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review_coffeeshop)

        session = Session(this)
        id = intent.getIntExtra("id", 0)

        initActionButton()
    }

    private fun initActionButton() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_submit.setOnClickListener {
            if (!CommonUtil.isEmptyEt(et_review)) {
                if (rate_bar.rating == 0f) {
                    rate_bar.requestFocus()
                } else {
                    postReview()
                }
            }
        }
    }

    private fun postReview(){
        DialogUtil.openDialog(this)
        AndroidNetworking.post(REVIEW)
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter("user_id", session.getUser().id)
            .addBodyParameter("coffee_shop_id", id.toString())
            .addBodyParameter("review", et_review.text.toString())
            .addBodyParameter("rating", rate_bar.rating.toString())
            .build()
            .getAsOkHttpResponseAndObject(
                ReviewResponse::class.java,
                object : OkHttpResponseAndParsedRequestListener<ReviewResponse> {
                    override fun onResponse(okHttpResponse: Response?, response: ReviewResponse?) {
                        if (okHttpResponse != null) {
                            if (okHttpResponse.code() == 200) {
                                if (response != null) {
                                    finish()
                                }
                            } else {
                                DialogUtil.closeDialog()
                                Toast.makeText(
                                    this@AddReviewCoffeeShopActivity,
                                    "Failed to review !",
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
                            this@AddReviewCoffeeShopActivity,
                            "Sorry, server is busy, please try again later !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
    }
}
