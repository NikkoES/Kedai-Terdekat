package id.kosanit.nearcoffee.activity.coffeeshop

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.model.rute.Distance
import id.kosanit.nearcoffee.navigation.FetchURL
import id.kosanit.nearcoffee.navigation.TaskLoadedCallback
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Double
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class NavigationActivity : AppCompatActivity(),
    ConnectionCallbacks,
    OnConnectionFailedListener,
    LocationListener,
    OnMapReadyCallback,
    TaskLoadedCallback {
    private var mMap: GoogleMap? = null
    private var place1: MarkerOptions? = null
    private var place2: MarkerOptions? = null
    internal var getDirection: Button? = null
    internal var txtDistance: TextView? = null
    private var currentPolyline: Polyline? = null
    private var mapFragment: MapFragment? = null
    private var isFirstTime = true
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private val listener: LocationListener? = null
    private val UPDATE_INTERVAL = 2 * 1000  /* 10 secs */.toLong()
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private var lat = 0.0
    private var lon = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(id.kosanit.nearcoffee.R.layout.activity_navigation)

        //code for getting current location


        requestMultiplePermissions()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear()
        Log.d("mylog", "Added Markers")
        mMap!!.addMarker(place1)
        mMap!!.addMarker(place2)
        mMap!!.setMyLocationEnabled(true)


        val intent: Intent = intent
        lat = intent.extras.getDouble("lat")
        lon = intent.extras.getDouble("lon")
        val googlePlex: CameraPosition? =
            CameraPosition.builder()
                .target(LatLng(lat, lon))
                .zoom(15f)
                .bearing(0f)
                .build()
        mMap!!.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                googlePlex
            ), 2000, null
        )
    }

    private fun getUrl(
        origin: LatLng,
        dest: LatLng,
        directionMode: String
    ): String {
        // Origin of route

        val str_origin =
            "origin=" + origin.latitude.toString() + "," + origin.longitude
        // Destination of route


        val str_dest =
            "destination=" + dest.latitude.toString() + "," + dest.longitude
        // Mode


        val mode = "mode=$directionMode"
        // Building the parameters to the web service


        val parameters = "$str_origin&$str_dest&$mode"
        // Output format


        val output = "json"
        // Building the url to the web service


        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=" + getString(
           id.kosanit.nearcoffee.R.string.google_maps_key
        )
    }

    override fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline!!.remove()
        currentPolyline =
            mMap!!.addPolyline(values[0] as PolylineOptions)
    }

    //runtime permission method


    private fun requestMultiplePermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
//                        }

                    // check for permanent denial of any permission

                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings

                        openSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(
                    applicationContext,
                    "Some Error! ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSameThread()
            .check()
    }

    private fun openSettingsDialog() {
        val builder =
            Builder(this@NavigationActivity)
        builder.setTitle("Required Permissions")
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.")
        builder.setPositiveButton(
            "Take Me To SETTINGS"
        ) { dialog, which ->
            dialog.cancel()
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri? =
                Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }


    //methods for getting current location


    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return
        }
        startLocationUpdates()
        mLocation =
            LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient
            )
        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));

        } else {
            Toast.makeText(
                this,
                "Location not Detected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection Suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(
            TAG,
            "Connection failed. Error: " + connectionResult.errorCode
        )
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    protected fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest, this
        )
        Log.d("reque", "--->>>>")
    }



    override fun onLocationChanged(location: Location) {
        if (isFirstTime) {
            //code to draw path on map
            var distance:kotlin.Double? = null
            getDirection =
                findViewById<Button?>(R.id.btnGetDirection)
            txtDistance =  findViewById<TextView?>(R.id.txt_distance)
            getDirection!!.setOnClickListener {
                FetchURL(this@NavigationActivity).execute(
                    getUrl(place1!!.position, place2!!.position, "driving"),
                    "driving"
                )
//                Log.e("DIST",distance.text)
            }

            val intent: Intent = intent
            lat = intent.extras.getDouble("lat")
            lon = intent.extras.getDouble("lon")
            val name: String? = intent.extras.getString("name")
            place1 = MarkerOptions().position(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            ).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            place2 = MarkerOptions()
                .position(LatLng(lat, lon)).title(name)

//            var results = FloatArray(1)
//            Location.distanceBetween(lat, lon,
//                         location.latitude, l.longitude,
//                         results);
//            location2 = Location.distanceBetween()
//            location.distanceTo(location2);
            distance = getDistance(LatLng(location.latitude,location.longitude), LatLng(lat,lon))
            txtDistance?.setText("Distance : " +String.format("%.2f", distance) + " Km")

            mapFragment =
                fragmentManager.findFragmentById(R.id.mapNearBy) as MapFragment
            mapFragment!!.getMapAsync(this)
            isFirstTime = false
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



