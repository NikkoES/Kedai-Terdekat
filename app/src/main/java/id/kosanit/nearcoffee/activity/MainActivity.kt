package id.kosanit.nearcoffee.activity

import android.Manifest
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.fragment.home.AccountFragment
import id.kosanit.nearcoffee.fragment.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val fragments = mutableListOf(HomeFragment(), AccountFragment())
    private var selectedFragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initActionButton()

        if (savedInstanceState == null) {
            bnv_main.selectedItemId = R.id.nav_home
        }

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {/* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {/* ... */
                }
            }).check()
    }


    private fun initActionButton() {
        bnv_main.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_home -> selectedFragment = 0
            R.id.nav_profile -> selectedFragment = 1
        }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_main,
                fragments[selectedFragment],
                fragments[selectedFragment].javaClass.simpleName
            )
            .commit()
        return true
    }

}
