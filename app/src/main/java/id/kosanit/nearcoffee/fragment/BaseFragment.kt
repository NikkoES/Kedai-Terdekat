package id.kosanit.nearcoffee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.adapter.BaseAdapter
import id.kosanit.nearcoffee.model.BaseModel
import kotlinx.android.synthetic.main.fragment_base.view.*

class BaseFragment : Fragment() {

    var list = ArrayList<BaseModel>()
    lateinit var adapter: BaseAdapter
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_base, container, false)

        initRecyclerView()

        return v
    }

    private fun initRecyclerView() {
        v.recyclerView.setHasFixedSize(true)
        v.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = BaseAdapter(activity!!)
        adapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onDelete(position: Int) {

            }

        })
        v.recyclerView.adapter = adapter
        loadItem()
    }

    private fun loadItem() {
//        list.add(
//            Pesan(
//                nama = "Iron Man",
//                message = "tes",
//                time = "10.20",
//                image = "https://i.annihil.us/u/prod/marvel/i/mg/5/c0/537ba730e05e0/standard_xlarge.jpg"
//            )
//        )
//        adapter.swap(list)
    }

}
