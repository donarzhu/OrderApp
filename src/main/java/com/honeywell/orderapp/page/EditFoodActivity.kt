package com.honeywell.orderapp.page

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.honeywell.orderapp.R
import com.honeywell.orderapp.Uitl.MyUitl
import com.honeywell.orderapp.Uitl.SelectPicUtil
import com.honeywell.orderapp.db.Food
import kotlinx.android.synthetic.main.activity_edit_food.*
import kotlinx.android.synthetic.main.item_edit.*
import java.io.File
import java.io.IOException


class EditFoodActivity : AppCompatActivity() {
    val RESULT_IMAGE = 200
    val CROP_PICTURE = 300
    var index:Int = -1
    var foodUri: Uri? =null
    lateinit var  my:EditFoodActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)
        my = this
        index = this.intent.getIntExtra("index", -1)
        if(index==-1)
            return
        try {
            val food = MyUitl.dbHelper!!.GetFood(index) ?: return
            MyUitl.SetImage(food, edit_imageView)
            edit_food_name.setText(food.foodname)
            edit_food_price.setText(food.price.toString())
            edit_image_button.setOnClickListener {
                SelectPicUtil.setFileName(food.id)
                SelectPicUtil.getByAlbum(my)
            }
            backEditPageButton.setOnClickListener {
                finish()
            }
            edit_finish_button.setOnClickListener {
                if(foodUri!=null)
                {
                    food.type = 1
                    food.image = foodUri.toString()
                }
                food.foodname = edit_food_name.text.toString()
                food.price = edit_food_price.text.toString().toFloat()
                MyUitl.dbHelper!!.update(food)
                my.setResult(RESULT_OK)
                finish()
            }
        }
        catch (e: Exception)
        {
            println(e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK)
        {
            var uri = SelectPicUtil.onActivityResult(my,requestCode,resultCode,data,300,200,3,2)
            if(uri !=null)
            {
                foodUri = uri
                val food = MyUitl.dbHelper!!.GetFood(index) ?: return
                food.type = 1
                food.image = uri.toString()
                MyUitl.SetImage(food,edit_imageView)
            }
        }
    }


}