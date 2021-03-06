package com.honeywell.orderapp.Adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.honeywell.orderapp.R
import com.honeywell.orderapp.Uitl.MyUitl

class SearchResultAdapter(private val mActivity: Activity) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolderItem(mActivity.layoutInflater.inflate(R.layout.item_shop, parent, false))
    }
    protected var items:ArrayList<ViewHolderItem> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val item = holder as ViewHolderItem
        val food = MyUitl.dbHelper?.GetFood(position)
        //holderItemDouble.tv_position.setText("position=" + position);
        item.foodNameView.text = food?.foodname
        item.priceView.text = food?.price.toString()
        item.countView.text = food?.count.toString()
        item.addButton.setOnClickListener {
            food!!.count++
            MyUitl.addPrice(food?.price)
            item.countView.text = food!!.count.toString()
        }
        item.subButton.setOnClickListener {
            if(food!!.count>0) {
                food!!.count--
                MyUitl.addPrice(-food.price)
                item.countView.text = food!!.count.toString()
            }
        }
        MyUitl!!.SetImage(food!!, item.imageView)
        items.add(item)
    }

    override fun getItemCount(): Int {
        val count = MyUitl.dbHelper!!.Size()
        return count
    }

    class ViewHolderItem(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView
        var foodNameView: TextView
        var priceView: TextView
        var countView: TextView
        var addButton: ImageView
        var subButton: ImageView

        init {
            //tv_position = itemView.findViewById(R.id.tv_position);
            imageView = itemView.findViewById(R.id.food_image)
            foodNameView = itemView.findViewById(R.id.food_name)
            priceView = itemView.findViewById(R.id.food_price)
            countView = itemView.findViewById(R.id.food_count)
            addButton = itemView.findViewById(R.id.add_button)
            subButton = itemView.findViewById(R.id.sub_button)
        }
    }
}