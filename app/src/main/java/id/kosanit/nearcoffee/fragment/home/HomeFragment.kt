package id.kosanit.nearcoffee.fragment.home

import android.app.Dialog
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener
import com.google.android.gms.maps.model.LatLng
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.activity.coffeeshop.DetailCoffeeShopActivity
import id.kosanit.nearcoffee.adapter.recyclerview.CoffeeAdapter
import id.kosanit.nearcoffee.data.Constant.Companion.COFFEE_SHOP
import id.kosanit.nearcoffee.model.shop.CoffeeShop
import id.kosanit.nearcoffee.model.shop.CoffeeShopResponse
import id.kosanit.nearcoffee.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.Response
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private var locationManager: LocationManager? = null
    var list = ArrayList<CoffeeShop>()
    lateinit var adapter: CoffeeAdapter
    lateinit var v: View

    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        initView()
        initActionButton()

        return v
    }

    private fun initActionButton() {
        v.et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun search(query: String) {
        adapter.removeAll()
        for (i in 0 until list.size) {
            if (list[i].name.toLowerCase().contains(query.toLowerCase())
                || list[i].address.toLowerCase().contains(query.toLowerCase())
            ) {
                adapter.add(list[i])
            }
        }
    }


    private fun initView() {
        DialogUtil.openDialog(activity!!)
        locationManager =
            context?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            );
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available");
        }
        v.recycler_view.setHasFixedSize(true)
        v.recycler_view.layoutManager = GridLayoutManager(activity, 2)
        adapter = CoffeeAdapter(activity!!)
        adapter.setOnItemClickListener(object : CoffeeAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                val i = Intent(activity, DetailCoffeeShopActivity::class.java)
                i.putExtra("item", adapter.getListItem(position))
                startActivity(i)
            }

        })
        v.recycler_view.adapter = adapter
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {


        }

        override fun onProviderEnabled(p0: String?) {


        }

        override fun onProviderDisabled(p0: String?) {

        }

        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude

            loadItem()
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

    private fun loadItem() {
        val fixedList: ArrayList<CoffeeShop> = ArrayList()
        AndroidNetworking.get(COFFEE_SHOP)
            .build()
            .getAsOkHttpResponseAndObject(
                CoffeeShopResponse::class.java,
                object : OkHttpResponseAndParsedRequestListener<CoffeeShopResponse> {
                    override fun onResponse(
                        okHttpResponse: Response?,
                        response: CoffeeShopResponse?
                    ) {
                        if (okHttpResponse != null) {
                            if (okHttpResponse.code() == 200) {
                                if (response != null) {
                                    list = response.data as ArrayList<CoffeeShop>

                                    for (item in list) {
                                        item.distance = getDistance(
                                            LatLng(item.latitude, item.longitude),
                                            LatLng(latitude, longitude)
                                        )
                                        fixedList.add(item)
                                    }

                                    adapter.swap(fixedList.sortedWith(compareBy {
                                        it.distance
                                    }))

                                    DialogUtil.closeDialog()
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Kedai tidak ditemukan !",
                                    Toast.LENGTH_SHORT
                                ).show()

                                DialogUtil.closeDialog()
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        if (anError != null) {
                            Log.e(
                                "ERROR",
                                anError.cause.toString() + "\n" + anError.errorCode + "\n" + anError.errorBody + "\n" + anError.errorDetail
                            )
                        }
                        Toast.makeText(
                            activity,
                            "Mohon maaf ada kesalahan teknis, silahkan coba beberapa saat lagi !",
                            Toast.LENGTH_SHORT
                        ).show()

                        DialogUtil.closeDialog()
                    }

                })
    }

}
