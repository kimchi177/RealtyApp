package com.example.thu.realtysocial.postNews;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface RefreshImageListener {
    void onRemoveItemImage(ArrayList<String> Image);
    void onPostImage(String valuePush);
    void onImageBitmap(int i,ArrayList<Bitmap> bitmaps);
}
