package com.honeywell.orderapp.Uitl;

import android.os.Message;

import com.honeywell.orderapp.MainActivity;
import com.honeywell.orderapp.db.OrderDao;

public class MyUitl {
    static public OrderDao dbHelper = null;
    static public MainActivity mainWin = null;
    private static float price = (float) 0.0;
    static public void addPrice(float new_price)
    {
        price += new_price;
        Message msg = new Message();
        msg.what = 1;
        msg.obj = price;
    }

    public static void clearPrice() {
        dbHelper.clearPrice();
    }
}
