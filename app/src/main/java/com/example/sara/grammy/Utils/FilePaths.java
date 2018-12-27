package com.example.sara.grammy.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class FilePaths {

    //"/storage/emulated/0"

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
   // public String PICTURES = ROOT_DIR + "/Pictures";

    public String PICTURES = ROOT_DIR + "/DCIM";

    public String CAMERA = ROOT_DIR + "/DCIM/Camera";

    public static ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;

        int column_index_data, column_index_folder_name;

        ArrayList<String> listOfAllImages = new ArrayList<String>();

        String absolutePathOfImage = null;

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        String last ="";
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            if(!absolutePathOfImage.equals(last)){
                listOfAllImages.add(absolutePathOfImage);
                last=absolutePathOfImage;
            }

        }
        return listOfAllImages;
    }
}
