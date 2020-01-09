package id.kosanit.nearcoffee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.kosanit.nearcoffee.R

class BaseAdapter(private val context: Context) : RecyclerView.Adapter<BaseAdapter.MyViewHolder>() {

    //private var listItem: ArrayList<Order> = ArrayList()
    lateinit var mOnItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onDelete(position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.item_base, viewGroup, false)
        var viewHolder = MyViewHolder(view)
        return viewHolder
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val listItem: Order = listItem[position]
//        holder.view.txtNamaOrder.text = listItem.nama
//        holder.view.txtHarga.text = CommonUtil().formatPriceIndonesia(listItem.harga.toDouble())
//        holder.view.txtWaktuOrder.text = listItem.lokasi

        holder.itemView.setOnClickListener {
            mOnItemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = 5

    //override fun getItemCount(): Int = listitem.size

//    fun add(listItem: Order) {
//        listItem.add(listItem)
//        notifyItemInserted(listItem.size + 1)
//    }
//
//    fun addAll(listItem: List<Order>) {
//        for (listItem in listItem) {
//            add(listItem)
//        }
//    }
//
//    fun removeAll() {
//        listItem.clear()
//        notifyDataSetChanged()
//    }
//
//    fun swap(datas: List<Order>) {
//        if (listItem.size >= 0)
//            listItem.clear()
//        listItem.addAll(datas)
//        notifyDataSetChanged()
//    }
//
//    fun getListItem(pos: Int): Order {
//        return listItem[pos]
//    }
//
//    fun setFilter(list: List<Order>) {
//        listItem = ArrayList<Order>()
//        listItem.addAll(list)
//        notifyDataSetChanged()
//    }
//
//    fun getListItem(): List<Order> {
//        return listItem
//    }
}