package com.honeywell.orderapp.page

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.honeywell.orderapp.R
import com.honeywell.orderapp.Uitl.MyUitl
import kotlinx.android.synthetic.main.activity_foods_manage.*
import kotlinx.android.synthetic.main.item_edit.view.*
import java.lang.Exception

class FoodsManageActivity : AppCompatActivity() {
    private lateinit var baseAdapter: BaseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foods_manage)
        val my= this
        baseAdapter = object: BaseAdapter() {
            override fun getCount(): Int {
                var count =  0
                try {
                    count = MyUitl.dbHelper!!.Size();
                }
                catch (e:Exception)
                {
                    println(e.message)
                }
                return count
            }

            override fun getItem(position: Int): Any {
                return MyUitl.dbHelper!!.GetFood(position)
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            @SuppressLint("ViewHolder", "InflateParams")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = LayoutInflater.from(my).inflate(R.layout.item_edit,null)
                var item = MyUitl.dbHelper?.GetFood(position)
                MyUitl!!.SetImage(item!!,view.eidt_item_image)
                view.edit_item_name.text = item.foodname
                view.edit_item_price.text = "¥"+item.price+"元"
                view.eidt_button.setOnClickListener {
                    val intent = Intent(my,EditFoodActivity::class.java)
                    intent.putExtra("index",position)
                    my.startActivityForResult(intent,100)
                }
                view.delete_button.setOnClickListener {
                    MyUitl.dbHelper?.delete(item.id)
                    handler.sendEmptyMessage(3)
                }
                return view
            }

        }
        food_list.adapter = baseAdapter
        ManageBackButton.setOnClickListener {
            MyUitl.MainRefresh()
            finish()
        }
        addFood_button.setOnClickListener {
            MyUitl.dbHelper!!.add("新品",10.0f,0,"food0")
            baseAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
            baseAdapter.notifyDataSetChanged()
    }

    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when (msg!!.what)
                {
                    3->{
                        baseAdapter.notifyDataSetChanged()
                    }
                }
            }
            catch ( e:Exception)
            {
                println(e.message)
            }
        }
    }
}