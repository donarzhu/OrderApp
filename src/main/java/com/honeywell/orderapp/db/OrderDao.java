package com.honeywell.orderapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class OrderDao {
    Context context;
    OrderDBHelper helper;
    ArrayList<Food> datas = new ArrayList<>();
    public  OrderDao(Context context)
    {
        this.context = context;
        helper = new OrderDBHelper(context);
    }

    public int Size()
    {
        return datas.size();
    }

    public Food GetFood(int index)
    {
        if(index<datas.size())
            return datas.get(index);
        return null;
    }

    public void clearPrice()
    {
        for(Food food : datas)
        {
            food.count = 0;
            food.price = 0;
        }
    }
    public void initDatas()
    {
        datas.clear();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(OrderDBHelper.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext()){
                Food food = new Food();
                food.id = cursor.getString(0);
                food.foodname = cursor.getString(1);
                food.price = cursor.getFloat(2);
                food.type = cursor.getInt(3);
                food.image = cursor.getString(4);
                datas.add(food);
            }
        }
        if(datas.size() == 0)
        {
            try
            {
                add("芒果果冻", (float) 15.00,0,"food0");
                add("曲奇饼干", (float) 10.00,0,"food1");
                add("提拉米苏", (float) 20.00,0,"food2");
                add("水果派", (float) 12.00,0,"food3");
                add("蛋糕", (float) 18.00,0,"food4");
                add("奶油卷", (float) 8.00,0,"food5");
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void add(String foodName,float price,int type,String image)
    {
        String id = UUID.randomUUID().toString();
        Food food = new Food();
        food.id = id;
        food.foodname = foodName;
        food.image = image;
        food.price = price;
        food.type = type;
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.beginTransaction();
        String sql = "insert into " + OrderDBHelper.TABLE_NAME + " (Id, foodName, foodPrice,imageType,imageFile) values ('"+id+"','"+ foodName+"',"+ price+","+type+",'"+image+"')";
        db.execSQL(sql);
        //db.setTransactionSuccessful();
        datas.add(food);
    }

    public void delete(String id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(OrderDBHelper.TABLE_NAME,"Id = ?",new String[]{id});
        for (Food food:datas) {
            if(food.id.equals(id))
            {
                datas.remove(food);
                break;
            }
        }
    }

    public void update(Food food)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("foodName",food.foodname);
        values.put("foodPrice",food.price);
        values.put("imageType",food.type);
        values.put("imageFile",food.image);
        db.update(OrderDBHelper.TABLE_NAME,values,"Id = ?",new String[]{food.id});
    }
}
