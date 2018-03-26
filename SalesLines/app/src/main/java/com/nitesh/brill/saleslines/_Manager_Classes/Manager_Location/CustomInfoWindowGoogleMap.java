package com.nitesh.brill.saleslines._Manager_Classes.Manager_Location;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nitesh.brill.saleslines.Common_Files.ConstantValue;
import com.nitesh.brill.saleslines.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Paras-Android on 07-02-2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    boolean not_first_time_showing_info_window;

    private Context context;


    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_marker_info, null);

        TextView name_tv = (TextView)view.findViewById(R.id.tv_name);
        TextView details_tv = (TextView)view.findViewById(R.id.details);
        final ImageView img = (ImageView) view.findViewById(R.id.iv_smpic);

        TextView hotel_tv = (TextView)view.findViewById(R.id.tv_one);
        TextView food_tv = (TextView)view.findViewById(R.id.tv_two);
        TextView transport_tv = (TextView)view.findViewById(R.id.tv_three);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

//        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
//                "drawable", context.getPackageName());
//        img.setImageResource(imageId);
//        Picasso.with(context).load(infoWindowData.getImage()).into(img);
//        if (marker != null && marker.isInfoWindowShown()){
//            marker.showInfoWindow();
//        }
        if (not_first_time_showing_info_window) {
            not_first_time_showing_info_window= false;
            Picasso.with(context).load(infoWindowData.getImage()).into(img);

        } else {
            not_first_time_showing_info_window = true;
//            Glide.with(context).asBitmap()
//                    .load(infoWindowData.getImage())
//                    .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
//
//                    .into(new SimpleTarget<Bitmap>(){
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            img.setImageBitmap(resource);
//                            marker.showInfoWindow();
//                        }
//
//
//                    });
            Picasso.with(context).load(infoWindowData.getImage()).into(img, new InfoWindowRefresher(marker));
        }
//        Picasso.with(context).load(infoWindowData.getImage()).into(img, new com.squareup.picasso.Callback() {
//            @Override
//            public void onSuccess() {
////                if (marker != null && marker.isInfoWindowShown()){
////                    marker.showInfoWindow();
////                }
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//        Glide.with(context)
//                .asBitmap()
//                .load(infoWindowData.getImage())
//                .into(new SimpleTarget<Bitmap>() {
//                          @Override
//                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                              if (marker != null && marker.isInfoWindowShown()){
//                                marker.showInfoWindow();
//                              }
//                              img.setImageBitmap(resource);
//
//                          }
//
//                      }
//                );

        hotel_tv.setText(infoWindowData.getHotel());
        food_tv.setText(infoWindowData.getFood());
        transport_tv.setText(infoWindowData.getTransport());

        return view;
    }
}