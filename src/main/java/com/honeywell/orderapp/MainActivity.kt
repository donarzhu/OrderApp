package com.honeywell.orderapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.recyclerview.widget.GridLayoutManager
import com.honeywell.orderapp.Adapter.SearchResultAdapter
import com.honeywell.orderapp.Uitl.MyUitl
import com.honeywell.orderapp.db.OrderDao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    protected var totalPrice:Float = 0.0F
    private var mAdapterSearchResult: SearchResultAdapter? =SearchResultAdapter(this)
    private var mLayoutManager:GridLayoutManager= GridLayoutManager(this,2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyUitl.dbHelper = OrderDao(this)
        MyUitl.mainWin = this
        MyUitl.dbHelper.initDatas()
        recyclerview.layoutManager = mLayoutManager
        recyclerview.adapter = mAdapterSearchResult

    }
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when (msg!!.what)
                {
                    1->{
                        var price:Float = msg.obj as Float
                        totalPrice +=price
                        total_price.setText(totalPrice.toString())
                    }
                    2->
                    {
                        totalPrice = 0.0f
                        total_price.text = totalPrice.toString()
                        MyUitl.clearPrice()
                        recyclerview.adapter?.notifyDataSetChanged()
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