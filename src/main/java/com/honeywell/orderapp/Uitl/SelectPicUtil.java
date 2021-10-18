package com.honeywell.orderapp.Uitl;

import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

public class SelectPicUtil {

    @SuppressLint("SdCardPath")
    public static void setFileName(String fileName)
    {
        lsimg = "file:///sdcard/"+fileName+".jpg";
    }
    /**临时存放图片的地址，如需修改，请记得创建该路径下的文件夹*/
    private static String lsimg = Environment.getDataDirectory()+"/temp.jpg";

    public static final int GET_BY_ALBUM = 801;//如果有冲突，记得修改
    public static final int GET_BY_CAMERA = 802;//如果有冲突，记得修改
    public static final int CROP = 803;//如果有冲突，记得修改

    /**从相册获取图片*/
    public static void getByAlbum(Activity act){
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        act.startActivityForResult(getAlbum, GET_BY_ALBUM);
    }

    /**通过拍照获取图片*/
    public static void getByCamera(Activity act){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(lsimg));
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            act.startActivityForResult(getImageByCamera, GET_BY_CAMERA);
        } else {
            //Log.e("请确认已经插入SD卡");
        }
    }
    /**
     * 处理获取的图片，注意判断空指针，默认大小480*480，比例1:1
     */
    public static Uri onActivityResult(Activity act, int requestCode, int resultCode, Intent data){
        return onActivityResult(act, requestCode, resultCode, data, 0, 0, 0, 0);
    }
    /**
     * 处理获取的图片，注意判断空指针
     */
    public static Uri onActivityResult(Activity act, int requestCode, int resultCode, Intent data,
                                          int w, int h,int aspectX,int aspectY){
        //Bitmap bm = null;
        Uri uri = null;
        if (resultCode == Activity.RESULT_OK) {
            //Uri uri = null;
            switch (requestCode) {
                case GET_BY_ALBUM:
                    uri = data.getData();
                    //act.startActivityForResult(crop(uri,w,h,aspectX,aspectY),CROP);
                    Intent intent = crop(uri,w,h,aspectX,aspectY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    act.startActivityForResult(intent,CROP);
                    break;
                case GET_BY_CAMERA:
                    uri = Uri.parse(lsimg);
                    act.startActivityForResult(crop(uri,w,h,aspectX,aspectY),CROP);
                    break;
                case CROP:
                    uri = dealCrop(act);
                    break;
            }
        }
        return uri;
    }

    /**默认裁剪输出480*480，比例1:1*/
    public static Intent crop(Uri uri){
        return crop(uri,480,480,1,1);
    }
    /**
     * 裁剪，例如：输出100*100大小的图片，宽高比例是1:1
     * @param w 输出宽
     * @param h 输出高
     * @param aspectX 宽比例
     * @param aspectY 高比例
     */
    public static Intent crop(Uri uri,int w,int h,int aspectX,int aspectY){
        if (w==0&&h==0) {
            w=h=480;
        }
        if (aspectX==0&&aspectY==0) {
            aspectX=aspectY=1;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 照片URL地址
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", w);
        intent.putExtra("outputY", h);
        // 输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(lsimg));
        // 输出格式
        intent.putExtra("outputFormat", "JPEG");
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        return intent;
    }

    /**处理裁剪，获取裁剪后的图片*/
    public static Uri dealCrop(Context context){
        // 裁剪返回
        Uri uri = Uri.parse(lsimg);
        return uri;
        /*
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;

         */
    }

}