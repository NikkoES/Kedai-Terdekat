package id.kosanit.nearcoffee.activity.coffeeshop

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.adapter.recyclerview.ReviewCoffeeAdapter
import id.kosanit.nearcoffee.data.Session
import id.kosanit.nearcoffee.model.shop.CoffeeShop
import kotlinx.android.synthetic.main.activity_detail_coffeeshop.*
import java.text.DecimalFormat


class DetailCoffeeShopActivity : AppCompatActivity() {

    private var locationManager : LocationManager? = null
  //  private val locationListener: LocationListener? = null
    lateinit var adapter: ReviewCoffeeAdapter
    var item: CoffeeShop? = null
    internal var txtDistance: TextView? = null

    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_coffeeshop)

        session = Session(this)

        initView()
        initActionButton()
    }

    private fun initView() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
        } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available");
        }
        adapter = ReviewCoffeeAdapter(this)
        txtDistance =  findViewById<TextView?>(R.id.txt_distance)

        rv_review.layoutManager = LinearLayoutManager(this)
        rv_review.itemAnimator = DefaultItemAnimator()
        rv_review.adapter = adapter
        rv_review.isNestedScrollingEnabled = false
        rv_review.hasFixedSize()

        adapter.setOnItemClickListener(object : ReviewCoffeeAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
            }
        })

        item = intent.getSerializableExtra("item") as CoffeeShop

        Glide.with(this).load(item?.image).into(image_backdrop)
        txt_title.text = item?.name
        txt_name.text = item?.name
      //  txt_distance.text = "0.45 km"
        txt_rating.text = (Math.round((item!!.rating) * 10.0) / 10.0).toString()
        txt_description.text = item?.description
        txt_menu.text = item?.menu
        txt_address.text = item?.address
        txt_rating_2.text = (Math.round((item!!.rating) * 10.0) / 10.0).toString()


        if (session.isLoggedIn()) {
            btn_review.visibility = View.VISIBLE
            for (i in item!!.reviewer.indices) {
                if (session.getUser().id.toInt() == item!!.reviewer[i].user_id) {
                    btn_review.visibility = View.GONE
                    break
                }
            }
        } else {
            btn_review.visibility = View.GONE
        }
        adapter.swap(item!!.reviewer)

    }

    private fun initActionButton() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_review.setOnClickListener {
            val i = Intent(this, AddReviewCoffeeShopActivity::class.java)
            i.putExtra("id", item?.id)
            startActivity(i)
        }
        btn_navigation.setOnClickListener {
            val i = Intent(this, NavigationActivity::class.java)
            i.putExtra("lat", item?.latitude)
            i.putExtra("lon", item?.longitude)
            i.putExtra("name", item?.name)
            startActivity(i)
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {


        }

        override fun onProviderEnabled(p0: String?) {


        }

        override fun onProviderDisabled(p0: String?) {

        }

        override fun onLocationChanged(location: Location) {
            var distance:kotlin.Double? = null
            distance = getDistance(LatLng(location.latitude,location.longitude), LatLng(
                item!!.latitude,
                item!!.longitude))
            txtDistance?.setText(String.format("%.2f", distance) + " Km")
        }
    }
    fun getDistance(
        StartP: LatLng,
        EndP: LatLng
    ): kotlin.Double {
        val Radius = 6371// radius of earth in Km

        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "$valueResult   KM  " + kmInDec
                .toString() + " Meter   " + meterInDec
        )
        return Radius * c
    }

    companion object {
        private const val TAG = "NavigationActivity"
    }


}
