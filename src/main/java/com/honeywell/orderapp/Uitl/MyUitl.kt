package com.honeywell.orderapp.Uitl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Message
import android.widget.ImageView
import com.honeywell.orderapp.MainActivity
import com.honeywell.orderapp.R
import com.honeywell.orderapp.db.Food
import com.honeywell.orderapp.db.OrderDao
import java.io.FileNotFoundException

object MyUitl {
    var dbHelper: OrderDao? = null
    var mainWin: MainActivity? = null
    private var price = 0.0.toFloat()
    fun addPrice(new_price: Float) {
        price += new_price
        val msg = Message()
        msg.what = 1
        msg.obj = price
        mainWin?.handler?.sendMessage(msg)
    }

    fun clearPrice() {
        dbHelper?.clearPrice()
    }

    fun SetImage(food: Food,image:ImageView)
    {
        when(food.type)
        {
            0->{
                when(food.image)
                {
                    "food1"->image.setImageResource(R.drawable.food1)
                    "food2"->image.setImageResource(R.drawable.food2)
                    "food3"->image.setImageResource(R.drawable.food3)
                    "food4"->image.setImageResource(R.drawable.food4)
                    "food5"->image.setImageResource(R.drawable.food5)
                    "food6"->image.setImageResource(R.drawable.food6)
                    "food7"->image.setImageResource(R.drawable.food7)
                }
             }
            1->{
                val uri = Uri.parse(food.image)
                var bitmap:Bitmap? = null;
                try {
                    bitmap = BitmapFactory.decodeStream(mainWin!!.getContentResolver().openInputStream(uri));
                    image.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace();
                }
            }
        }

    }

    fun MainRefresh() {
        mainWin?.handler?.sendEmptyMessage(4)
    }
}