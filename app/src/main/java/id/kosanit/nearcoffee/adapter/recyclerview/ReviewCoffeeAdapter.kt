package id.kosanit.nearcoffee.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.kosanit.nearcoffee.R
import id.kosanit.nearcoffee.model.shop.Reviewer
import kotlinx.android.synthetic.main.item_review_coffeeshop.view.*
import java.util.*

class ReviewCoffeeAdapter(private val context: Context) :

    RecyclerView.Adapter<ReviewCoffeeAdapter.Holder>() {

    private var listItem: ArrayList<Reviewer> = ArrayList()
    lateinit var mOnItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        return Holder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.item_review_coffeeshop, viewGroup, false
            )
        )
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val item = listItem[position]

        holder.view.txt_name.text = item.reviewer
        holder.view.txt_review.text = item.review
        //holder.view.txt_time.text = item.posted
        holder.view.image_rate.rating = item.rating.toFloat()

        holder.itemView.setOnClickListener {
            mOnItemClickListener.onClick(position)
        }
    }

    override fun getItemCount(): Int = listItem.size

    fun add(item: Reviewer) {
        listItem.add(item)
        notifyItemInserted(listItem.size + 1)
    }

    fun addAll(listItem: List<Reviewer>) {
        for (item in listItem) {
            add(item)
        }
    }

    fun remove(pos: Int) {
        listItem.removeAt(pos)
        notifyDataSetChanged()
    }

    fun removeAll() {
        listItem.clear()
        notifyDataSetChanged()
    }

    fun swap(datas: List<Reviewer>) {
        if (listItem.size >= 0)
            listItem.clear()
        listItem.addAll(datas)
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): Reviewer {
        return listItem[pos]
    }

    fun setFilter(list: List<Reviewer>) {
        listItem = ArrayList()
        listItem.addAll(list)
        notifyDataSetChanged()
    }

    fun getListItem(): List<Reviewer> {
        return listItem
    }

}