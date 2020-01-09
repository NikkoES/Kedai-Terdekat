package id.kosanit.nearcoffee.adapter.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.model.rute.RuteResponse
import id.kosanit.nearcoffee.model.rute.Step
import id.kosanit.nearcoffee.model.shop.CoffeeShop
import id.kosanit.nearcoffee.util.DialogUtil
import kotlinx.android.synthetic.main.item_coffee_shop.view.*
import java.text.DecimalFormat
import kotlin.math.ln

class CoffeeAdapter(private val context: Context) :
    RecyclerView.Adapter<CoffeeAdapter.MyViewHolder>() {

    private var listItem: ArrayList<CoffeeShop> = ArrayList()
    lateinit var mOnItemClickListener: OnItemClickListener


    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_coffee_shop, viewGroup, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: CoffeeShop = listItem[position]

        Glide.with(context).load(item.image).into(holder.view.image_thumbnail)
        holder.view.txt_name.text = item.name
        holder.view.txt_address.text = item.address
        holder.view.rating_course.rating = item.rating.toFloat()
        holder.view.txt_rating.text = (Math.round(item.rating * 10.0) / 10.0).toString()
        holder.view.txt_distance.text = String.format("%.2f", item.distance) + " Km"
        holder.itemView.setOnClickListener {
            mOnItemClickListener.onClick(position)
        }
    }

    override fun getItemCount(): Int = listItem.size

    fun add(item: CoffeeShop) {
        listItem.add(item)
        notifyItemInserted(listItem.size + 1)
    }

    fun addAll(list: List<CoffeeShop>) {
        for (listItem in list) {
            add(listItem)
        }
    }

    fun removeAll() {
        listItem.clear()
        notifyDataSetChanged()
    }

    fun remove(pos: Int) {
        listItem.removeAt(pos)
        notifyDataSetChanged()
    }

    fun swap(datas: List<CoffeeShop>) {
        if (listItem.size >= 0)
            listItem.clear()
        listItem.addAll(datas)
        notifyDataSetChanged()
    }

    fun getListItem(pos: Int): CoffeeShop {
        return listItem[pos]
    }

    fun setFilter(list: List<CoffeeShop>) {
        listItem = ArrayList<CoffeeShop>()
        listItem.addAll(list)
        notifyDataSetChanged()
    }

    fun getListItem(): List<CoffeeShop> {
        return listItem
    }
}