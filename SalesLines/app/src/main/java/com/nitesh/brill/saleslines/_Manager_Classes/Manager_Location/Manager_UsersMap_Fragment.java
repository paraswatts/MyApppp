package com.nitesh.brill.saleslines._Manager_Classes.Manager_Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brill.nitesh.punjabpool.Common.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.APIClient;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.RetrofitAPI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Manager_UsersMap_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Manager_UsersMap_Fragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    RetrofitAPI retrofitAPI;
    private String mParam2;
    private Button bt_mapDate;
    private EditText et_mapDate;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;


    MapView mMapView;
    private GoogleMap googleMap;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public Manager_UsersMap_Fragment() {
        // Required empty public constructor
    }


    public static Manager_UsersMap_Fragment newInstance(String param1, String param2) {
        Manager_UsersMap_Fragment fragment = new Manager_UsersMap_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        permissionStatus = getContext().getSharedPreferences("permissionStatus",MODE_PRIVATE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lead_map, container, false);
        et_mapDate = (EditText)view.findViewById(R.id.et_mapDate );
        bt_mapDate = (Button) view.findViewById(R.id.bt_mapDate );
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                et_mapDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        et_mapDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_mapDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_mapDate.getText().toString().trim()))
                {
                    getPermissions();
                }
                else{
                    Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_LONG).show();
                }
            }
        });




        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        getPermissions();
        return view;
    }
    public void getPermissions()
    {
        if(checkSelfPermission(getContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(getContext(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
               ){
            if(shouldShowRequestPermissionRationale(permissionsRequired[0])
                    || shouldShowRequestPermissionRationale(permissionsRequired[1])
                   ){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                requestPermissions(permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            //txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }    
    }


    void getUserCoordinates(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading map....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime = df.format(c.getTime());

        Log.e("Date time ",dateTime);

        String date = TextUtils.isEmpty(et_mapDate.getText().toString().trim())?dateTime:et_mapDate.getText().toString().trim();
        retrofitAPI = APIClient.getClient().create(RetrofitAPI.class);

        Call<JsonElement> call = retrofitAPI.getGPSLocationsManagerDay(objSaveData.getString("user_id"),objSaveData.getString("client_id"),date);

        call.enqueue(new Callback<JsonElement>() {

            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("URL", "=====" + response.raw().request().url());
                Log.e("=============", response.body().toString());
                try {
                    ArrayList<Double> lat = new ArrayList<>();
                    ArrayList<Double> lng = new ArrayList<>();
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> userids = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response.body().toString());
                    Log.e("json array",jsonArray+""+jsonArray.length());
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                       lat.add(Double.parseDouble(jsonObject.get("latitude").toString()));
                        lng.add(Double.parseDouble(jsonObject.get("longitude").toString()));
                        names.add(jsonObject.get("Saleman").toString());
                        userids.add(jsonObject.get("Userid").toString());
                    }

                    plotMap(lat,lng,names,userids);
                }catch (Exception e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }


            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void plotMap(final ArrayList<Double> lat, final ArrayList<Double> lng, final ArrayList<String> names, final ArrayList<String> userId)
    {
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                // For showing a move to my location button
                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
//                ArrayList<Double> lat = new ArrayList<>();
//                ArrayList<Double> lng = new ArrayList<>();
                ArrayList<String> markerText = new ArrayList<>();
                ArrayList<String> imageUrl = new ArrayList<>();

//                lat.add(30.7085777);
//                lng.add(76.68998731);
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//                lat.add(30.7072613);
//                lng.add(76.6892792);
//                markerText.add("Priya Negi");
//                imageUrl.add("http://www.seoghostwriter.com/wp-content/themes/thesis_151/rotator/sample-4.jpg");
//                lat.add(30.7091012);
//                lng.add(76.6910763);
//                markerText.add("Simranjit Singh");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJ1cpV5F1-YCYK4_G6M7EnnsrCSL_bmakM6wkUPQ8C9D9Cb5jqow");
//                lat.add(30.7098971);
//                lng.add(76.6896841);
//                markerText.add("Khushdeep Kaur");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_Z9pXgREvSOEhCApl7yKsaX7sv8d_RQ5lcplZWVQnRWDMmR1oEg");
//                lat.add(30.7072931);
//                lng.add(76.6919485);
//                markerText.add("Pawandeep Kaur");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8hePWwSnzLHOA9u_EuKS30LXTyhGurgyatv72OYxNRkRH9izp0A");
//                markerText.add("Paras Watts");

                for(int i = 0 ; i <
                        lat.size() ; i++ ) {
//                    Log.e("Lat Long",""+lat.get(i)+"===="+lng.get(i)+"======"+markerText.get(i));
                    try {

                        createMarker(lat.get(i), lng.get(i),names.get(i),userId.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(30.7085777, 76.68998731);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(shouldShowRequestPermissionRationale(permissionsRequired[0])
                    || shouldShowRequestPermissionRationale(permissionsRequired[1])
                    ){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    private void proceedAfterPermission() {
        getUserCoordinates();



    }

    
   
    protected void createMarker(final double latitude, final double longitude,final String markerText,final String userId) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        Log.e("Adding","Marker");
        LatLng marker = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        final InfoWindowData info = new InfoWindowData();
        info.setImage("http://console.salelinecrm.com/saleslineapi/GetprofileImage/"+userId);
        info.setHotel(addresses.get(0).getAddressLine(0));
        info.setFood(addresses.get(0).getSubLocality());
        info.setTransport(addresses.get(0).getLocality());
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getContext());
        googleMap.setInfoWindowAdapter(customInfoWindow);
//        final Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Log.e("Loading marker","Image loaded");
//
//
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                Log.e("Loading marker","Image failed");
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                Log.e("Loading marker","Image prepared");
//            }
//        };


        final View mCustomMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

//        Glide.with(getContext())
//                .asBitmap()
//                .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/"+userId)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        Marker m =googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(latitude, longitude)));
//                                //.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerText,resource,mCustomMarkerView))));
//                        m.setTag(info);
                        //m.showInfoWindow();

//                    }
//
//                    }
//                );
        try {

        Glide.with(getContext())
                .asBitmap()
                .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/"+userId)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.e("Load failed","marker image");
                        Marker m =googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(markerText)
                                .snippet("").icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerText,null,mCustomMarkerView))));
                        m.setTag(info);
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Marker m =googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(markerText)
                                .snippet("").icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerText,resource,mCustomMarkerView))));
                        m.setTag(info);
                        //m.showInfoWindow();

                    }

                    }
                );
        }catch (Exception e){
            Marker m = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(markerText)
                    .snippet("Salesman Info").icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerText, null, mCustomMarkerView))));
            m.setTag(info);
        }



    }

    private Bitmap getMarkerBitmapFromView(String markerText,Bitmap bitmap,View view) {

        if(bitmap!=null) {
            //View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
            final ImageView markerImageView = (ImageView) view.findViewById(R.id.profile_image);

            markerImageView.setImageBitmap(bitmap);
        }

        TextView markerTextView = (TextView) view.findViewById(R.id.marker_text);
        markerTextView.setText(markerText);

//        Glide.with(this)
//                .load(imageUrl)
//                .into(markerImageView);
//        Glide.with(getContext()).load(imageUrl)
//                .thumbnail(0.5f)
//                .into(markerImageView);


        // markerImageView.setImageResource(resId);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onResume() {
        
        super.onResume();
        if (sentToSettings) {
            if (checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        mMapView.onResume();


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




}
