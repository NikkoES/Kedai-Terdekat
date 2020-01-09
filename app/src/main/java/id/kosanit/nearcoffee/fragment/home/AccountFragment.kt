package id.kosanit.nearcoffee.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.activity.auth.LoginActivity
import id.kosanit.nearcoffee.data.Session
import kotlinx.android.synthetic.main.fragment_account.view.*

class AccountFragment : Fragment() {

    lateinit var v: View

    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_account, container, false)

        session = Session(activity!!)

        initActionButton()

        return v
    }

    override fun onResume() {
        initView()
        super.onResume()
    }

    private fun initActionButton() {
        v.btn_login_logout.setOnClickListener {
            if (session.isLoggedIn()) {
                session.logoutUser()
            } else {
                startActivity(Intent(activity!!, LoginActivity::class.java))
            }
        }
    }

    private fun initView() {
        if (session.isLoggedIn()) {
            v.layout_name.visibility = View.VISIBLE
            v.layout_profile.visibility = View.VISIBLE
            v.btn_login_logout.text = "LOGOUT"
            v.btn_login_logout.setTextColor(resources.getColorStateList(R.color.lightred))

            v.txt_name.text = "Hi, " + session.getUser().name
            v.txt_email.text = session.getUser().email
            v.txt_phone_number.text = session.getUser().phone
        } else {
            v.layout_name.visibility = View.GONE
            v.layout_profile.visibility = View.GONE
            v.btn_login_logout.text = "LOGIN"
            v.btn_login_logout.setTextColor(resources.getColorStateList(R.color.green))
        }
        v.txt_about.text = "Near Coffee is the apps for information about coffee shop near us"
    }

}
