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
import android.os.AsyncTask;
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
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.APIClient;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.RetrofitAPI;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Manager_SingleLeadMap_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Manager_SingleLeadMap_Fragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button bt_mapDate;
    private EditText et_mapDate;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    RetrofitAPI retrofitAPI;
    TextView title;

    MapView mMapView;
    private GoogleMap googleMap;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public Manager_SingleLeadMap_Fragment() {
        // Required empty public constructor
    }


    public static Manager_SingleLeadMap_Fragment newInstance(String param1, String param2) {
        Manager_SingleLeadMap_Fragment fragment = new Manager_SingleLeadMap_Fragment();
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
        Log.e("I am in Notification ", "Fragment");

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

        title = (TextView)view.findViewById(R.id.textView2);
        title.setText(getArguments().getString("username")+ " Map");
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

    void getUserCoordinates(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading map....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime = df.format(c.getTime());

        Log.e("Date time ",dateTime);
        String userid  = getArguments().getString("user_id");
        Log.e("user id",userid);
        String date = TextUtils.isEmpty(et_mapDate.getText().toString().trim())?dateTime:et_mapDate.getText().toString().trim();
        retrofitAPI = APIClient.getClient().create(RetrofitAPI.class);

        Call<JsonElement> call = retrofitAPI.getGPSLocationsUserDay(userid,objSaveData.getString("client_id"),date);

        call.enqueue(new Callback<JsonElement>() {

            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("URL", "=====" + response.raw().request().url());
                Log.e("=============", response.body().toString());
                try {
                    ArrayList<Double> lat = new ArrayList<>();
                    ArrayList<Double> lng = new ArrayList<>();
                    ArrayList<String> time = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response.body().toString());
                    if(jsonArray.length()>0) {
                        Log.e("json array length", "" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            lat.add(Double.parseDouble(jsonObject.get("latitude").toString()));
                            lng.add(Double.parseDouble(jsonObject.get("longitude").toString()));
                            time.add(jsonObject.get("locationdatetime").toString());
                        }

                        plotMap(lat, lng,time);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("No map data found")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }


            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
    public void plotMap(final ArrayList<Double> lat,final ArrayList<Double> lng,final ArrayList<String> time) {

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
//                ArrayList<String> markerText = new ArrayList<>();
//                ArrayList<String> imageUrl = new ArrayList<>();
//
//                lat.add(30.7085777);
//                lng.add(76.68998731);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//                lat.add(30.7072613);
//                lng.add(76.6892792);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//                lat.add(30.7091012);
//                lng.add(76.6910763);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//                lat.add(30.7098971);
//                lng.add(76.6896841);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//                lat.add(30.7072931);
//                lng.add(76.6919485);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//


                if(lat.size()>8)
                {
                    String origin = "origin=" + lat.get(0) + "," + lng.get(0);
                    String destination = "destination=" + lat.get(7) + "," + lng.get(7);
                    String waypoints = "waypoints=optimize:true|";
                    String path ="";
                    for(int i = 1 ; i <
                            7 ; i++ ) {
                        path = path + lat.get(i) + "," + lng.get(i) + "|";


                    }

                    waypoints = waypoints + path;
                    String sensor = "sensor=false";
                    String params = origin + "&" + waypoints + "&"  + destination + "&" + sensor;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/"
                            + output + "?" + params;
                    String fixedUrlStr = url.replace("|", "%7C");

                    Log.e("I am here in","urlk"+fixedUrlStr);


                    new FetchRouteAsyncTask().execute(fixedUrlStr);
                }
                else{
                    String origin = "origin=" + lat.get(0) + "," + lng.get(0);
                    String destination = "destination=" + lat.get(lat.size()-1) + "," + lng.get(lng.size()-1);
                    String waypoints = "waypoints=optimize:true|";
                    String path ="";
                    for(int i = 1 ; i <
                            lat.size()-1 ; i++ ) {
                        path = path + lat.get(i) + "," + lng.get(i) + "|";


                    }

                    waypoints = waypoints + path;
                    String sensor = "sensor=false";
                    String params = origin + "&" + waypoints + "&"  + destination + "&" + sensor;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/"
                            + output + "?" + params;
                    String fixedUrlStr = url.replace("|", "%7C");

                    Log.e("I am here in","urlk"+fixedUrlStr);


                    new FetchRouteAsyncTask().execute(fixedUrlStr);
                }


                for(int i = 0 ; i <
                        lat.size() ; i++ ) {
                    try {
                        createMarker(lat.get(i), lng.get(i),"Salesman Name","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA",time.get(i));
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


    public void drawPath(String result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Log.e("I am here in","Drawpath");
            googleMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#4dc412"))//Google maps blue color05b1fb
                    .geodesic(true)
            );

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("I am here in","Exception");
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        Log.e("I am here in","Decode poly");

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    protected void createMarker(final double latitude, final double longitude,final String markerText,final String imageUrl,final String time) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        Log.e("Adding","Marker");
        LatLng marker = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        String userid  = getArguments().getString("user_id");

        final InfoWindowData info = new InfoWindowData();
        info.setImage("http://console.salelinecrm.com/saleslineapi/GetprofileImage/"+userid);

        info.setHotel(addresses.get(0).getAddressLine(0));
        info.setFood(addresses.get(0).getSubLocality());
        info.setTransport(addresses.get(0).getLocality());
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getContext());
        googleMap.setInfoWindowAdapter(customInfoWindow);

        final View mCustomMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

//        Glide.with(getContext())
//                .asBitmap()
//                .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/"+userid)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        Marker m =googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(latitude, longitude))
//                                .title(markerText)
//                                .snippet("Salesman Info").icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerText,resource,mCustomMarkerView))));
//                        m.setTag(info);
//                        //m.showInfoWindow();
//
//                    }
//
//                    }
//                );

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            Date d = sdf.parse(time);
            String formattedTime = d.toString();


            Marker m = googleMap.addMarker(new MarkerOptions()
                    .title(getArguments().getString("username"))
                    .snippet("was here at " + formattedTime)
                    .position(new LatLng(latitude, longitude)));
            //.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerText,resource,mCustomMarkerView))));
            m.setTag(info);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }




    private Bitmap getMarkerBitmapFromView(String markerText,Bitmap bitmap,View view) {

        //View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        final ImageView markerImageView = (ImageView) view.findViewById(R.id.profile_image);
        TextView markerTextView = (TextView)view.findViewById(R.id.marker_text);
        markerTextView.setText(markerText);
        markerImageView.setImageBitmap(bitmap);



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

    public class FetchRouteAsyncTask extends AsyncTask<String, String, String> {

        final ProgressDialog progressDialog =  new ProgressDialog(getContext());


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("In","post");
            if(s!=null){
                Log.e("In","post");
                //drawPath(s);
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Fetching salesman route....");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.e("In","Doinbackground" +strings[0]);
                // defaultHttpClient
                JSONParcer jParser = new JSONParcer();
                String json = jParser.getJSONFromUrl(strings[0]);
                return json;


        }
    }


}
