package com.honeywell.orderapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.honeywell.orderapp.Adapter.SearchResultAdapter
import com.honeywell.orderapp.Uitl.MyUitl
import com.honeywell.orderapp.db.OrderDao
import com.honeywell.orderapp.page.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Exception
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    protected var totalPrice:Float = 0.0F
    private lateinit var mAdapterSearchResult: SearchResultAdapter
    private lateinit var mLayoutManager:GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyUitl.dbHelper = OrderDao(this)
        MyUitl.mainWin = this
        MyUitl.dbHelper?.initDatas()
        mLayoutManager = GridLayoutManager(this,1)
        mAdapterSearchResult=SearchResultAdapter(this)
        recyclerview.layoutManager = mLayoutManager
        recyclerview.adapter = mAdapterSearchResult
        menu_manager.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        checkPermission()
    }
    var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            try {
                when (msg!!.what)
                {
                    1->{
                        var price:Float = msg.obj as Float
                        totalPrice = price
                        total_price.setText(totalPrice.toString())
                    }
                    2->
                    {
                        totalPrice = 0.0f
                        total_price.text = totalPrice.toString()
                        MyUitl.clearPrice()
                        recyclerview.adapter?.notifyDataSetChanged()
                    }
                    4->{
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
    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )
    val mPermissionList: ArrayList<String> = ArrayList()
    val mRequestCode = 100 //权限请求码

    fun checkPermission()
    {

        for (i in 0 until permissions.size) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionList.add(permissions[i]) //添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size > 0) { //有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode)
        }
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (permsRequestCode) {
            mRequestCode -> {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (!cameraAccepted) {
                    Toast.makeText(this, "没有权限无法继续", Toast.LENGTH_SHORT).show()
                    Thread.sleep(2000)
                    checkPermission()
                }
            }
        }
    }

}