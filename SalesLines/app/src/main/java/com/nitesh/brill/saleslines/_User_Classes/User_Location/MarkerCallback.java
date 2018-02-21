package com.nitesh.brill.saleslines._User_Classes.User_Location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Paras-Android on 07-02-2018.
 */

public class MarkerCallback implements Target {

    ImageView userPhoto;
    String URL;
    Context context;

    public MarkerCallback( String URL, ImageView userPhoto,Context context) {
        this.URL = URL;
        this.userPhoto = userPhoto;
        this.context = context;
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        userPhoto.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
