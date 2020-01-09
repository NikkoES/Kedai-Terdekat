package id.kosanit.nearcoffee.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class VpAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()

    private val fragmentTitle = ArrayList<String>()

    override fun getCount(): Int {
        return fragmentTitle.size
    }


    init {
        fm.executePendingTransactions()
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitle[position]
    }

    fun addFragment(fragment: Fragment, Title: String) {
        fragmentList.add(fragment)
        fragmentTitle.add(Title)
    }
}
