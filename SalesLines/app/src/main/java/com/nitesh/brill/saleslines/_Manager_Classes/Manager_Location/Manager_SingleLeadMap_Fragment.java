package com.nitesh.brill.saleslines._Manager_Classes.Manager_Location;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brill.nitesh.punjabpool.Common.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
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
import java.util.Arrays;
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
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    RetrofitAPI retrofitAPI;
    TextView title;
    MapView mMapView;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button bt_mapDate;
    private EditText et_mapDate;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private GoogleMap googleMap;
    RelativeLayout map_root;

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
        permissionStatus = getContext().getSharedPreferences("permissionStatus", MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_lead_map, container, false);
        et_mapDate = (EditText) view.findViewById(R.id.et_mapDate);
        bt_mapDate = (Button) view.findViewById(R.id.bt_mapDate);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                et_mapDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        map_root = (RelativeLayout)view.findViewById(R.id.map_root);
        et_mapDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        title = (TextView) view.findViewById(R.id.textView2);
        title.setText(getArguments().getString("username") + " Map");
        bt_mapDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_mapDate.getText().toString().trim())) {
                    getPermissions();
                } else {
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

    void getUserCoordinates() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading map....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime = df.format(c.getTime());
        String userid = getArguments().getString("user_id");
        String date = TextUtils.isEmpty(et_mapDate.getText().toString().trim()) ? dateTime : et_mapDate.getText().toString().trim();
        retrofitAPI = APIClient.getClient().create(RetrofitAPI.class);
        Call<JsonElement> call = retrofitAPI.getGPSLocationsUserDay(userid, objSaveData.getString("client_id"), date);
        call.enqueue(new Callback<JsonElement>() {
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("URL", "=====" + response.raw().request().url());
                Log.e("=============", response.body().toString());
                try {
                    ArrayList<Double> lat = new ArrayList<>();
                    ArrayList<Double> lng = new ArrayList<>();
                    ArrayList<String> time = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response.body().toString());
                    if (jsonArray.length() > 0) {
                        Log.e("json array length", "" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            lat.add(Double.parseDouble(jsonObject.get("latitude").toString()));
                            lng.add(Double.parseDouble(jsonObject.get("longitude").toString()));
                            time.add(jsonObject.get("locationdatetime").toString());
                        }
                        plotMapTest(lat, lng, time);
                    } else {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }


   public void plotMapTest(final ArrayList<Double> lat, final ArrayList<Double> lng, final ArrayList<String> time) {
        //public void plotMapTest() {

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startRevealAnimation();
                }

                googleMap = mMap;
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                googleMap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                getContext(), R.raw.style_json));
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
//                //1
//                lat.add(30.749483);
//                lng.add(76.674680);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //2
//                lat.add(30.740428);
//                lng.add(76.674980);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //3
//                lat.add(30.738224);
//                lng.add(76.681731);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //4
//                lat.add(30.733093);
//                lng.add(76.696437);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //5
//                lat.add(30.731725);
//                lng.add(76.703490);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //6
//                lat.add(30.735176);
//                lng.add(76.708901);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //7
//                lat.add(30.738541);
//                lng.add(76.714588);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //8
//                lat.add(30.743395);
//                lng.add(76.721848);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //9
//                lat.add(30.737324);
//                lng.add(76.726827);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //10
//                lat.add(30.731336);
//                lng.add(76.731667);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //11
//                lat.add(30.727649);
//                lng.add(76.734264);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //12
//                lat.add(30.730085);
//                lng.add(76.739498);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //13
//                lat.add(30.731759);
//                lng.add(76.747428);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //14
//                lat.add(30.726265);
//                lng.add(76.751523);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //15
//                lat.add(30.719591);
//                lng.add(76.757200);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //16
//                lat.add(30.722976);
//                lng.add(76.762529);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //17
//                lat.add(30.726852);
//                lng.add(76.768748);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //18
//                lat.add(30.720847);
//                lng.add(76.773618);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //19
//                lat.add(30.715010);
//                lng.add(76.778863);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");
//
//                //20
//                lat.add(30.709316);
//                lng.add(76.784317);
//                markerText.add("Paras Watts");
//                imageUrl.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA");

                if (lat.size() < 11) {
                    String origin = lat.get(0) + "," + lng.get(0);
                    String destination = lat.get(lat.size() - 1) + "," + lng.get(lng.size() - 1);
                    String waypoints = "optimize:true|";
                    String path = "";
                    for (int i = 1; i <lat.size() - 1; i++) {
                        path = path + lat.get(i) + "," + lng.get(i) + "|";
                    }
                    waypoints = waypoints + path;
                    getRoutes(origin, waypoints.replace("|", "%7C"), destination, "false");
                } else {
                    double number = (float) lat.size() / 9;
                    int size = lat.size();
                    double i = Math.ceil(number);
                    int num = (int) size;
                    int remainder = num % 9;
                    int num1;
                    if (remainder == 0) {
                        num1 = (int) i - 1;
                    } else {
                        num1 = (int) i - 1;
                    }
                    for (int j = 0; j < num1; j++) {
                        int finalSize = 9;
                        String origin = lat.get(finalSize * j) + "," + lng.get(finalSize * j);
                        String destination = lat.get(finalSize * (j + 1)) + "," + lng.get(finalSize * (j + 1));
                        String waypoints = "optimize:true|";
                        String path = "";
                        int loop = (finalSize * (j + 1));
                        for (int k = (j * 9) + 1; k < loop; k++) {
                            path = path + lat.get(k) + "," + lng.get(k) + "|";
                        }
                        waypoints = waypoints + path;
                        getRoutes(origin, waypoints.replace("|", "%7C"), destination, "false");
                    }

                    if (remainder != 0) {
                        String origin = lat.get(size - remainder) + "," + lng.get(size - remainder);
                        String destination = lat.get(lat.size() - 1) + "," + lng.get(lat.size() - 1);
                        String waypoints = "optimize:true|";
                        String path = "";
                        for (int k = size + 1 - remainder; k < size; k++) {
                            path = path + lat.get(k - 1) + "," + lng.get(k - 1) + "|";
                        }
                        waypoints = waypoints + path;
                        getRoutes(origin, waypoints.replace("|", "%7C"), destination, "false");
                    } else {
                        String origin = lat.get(size - 9) + "," + lng.get(size - 9);
                        String destination = lat.get(lat.size() - 1) + "," + lng.get(lat.size() - 1);
                        String waypoints = "optimize:true|";
                        String path = "";
                        for (int k = size - 9 + 1; k < size - 1; k++) {
                            path = path + lat.get(k) + "," + lng.get(k) + "|";
                        }
                        waypoints = waypoints + path;
                        getRoutes(origin, waypoints.replace("|", "%7C"), destination, "false");
                    }
                }

                for (int i = 0; i <
                        lat.size(); i++) {
                    try {
                        createMarker(i, lat.get(i), lng.get(i), "Salesman Name", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA", time.get(i), lat.size());
                        //createMarker(i, lat.get(i), lng.get(i), "Salesman Name", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbeNZvEaAoQWVCPolqCs7UR7d6JBfZeX1qmV01XSn4C65k4-pCVA", "", lat.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getPermissions() {
        if (checkSelfPermission(getContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(getContext(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ) {
            if (shouldShowRequestPermissionRationale(permissionsRequired[0])
                    || shouldShowRequestPermissionRationale(permissionsRequired[1])
                    ) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
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
            } else {
                requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            proceedAfterPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
                proceedAfterPermission();
            } else if (shouldShowRequestPermissionRationale(permissionsRequired[0])
                    || shouldShowRequestPermissionRationale(permissionsRequired[1])
                    ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
                Toast.makeText(getContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            }
        }
    }


    private void proceedAfterPermission() {
        getUserCoordinates();
        //plotMapTest();

    }


    public void getRoutes(String origin, String waypoints, String destination, String sensor) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching salesman route");
        progressDialog.show();
        RetrofitAPI retrofitAPI = APIClient.getMapsClient().create(RetrofitAPI.class);
        Call<JsonElement> call = retrofitAPI.getMapPath(origin, waypoints, destination, sensor);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("url is ", "" + response.raw().request().url());
                if(response!=null & response.isSuccessful()){
                    try {
                        progressDialog.dismiss();
                        drawPath(response.body().toString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void drawPath(String result) {

        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            List<PatternItem> pattern = Arrays.<PatternItem>asList(
                    new Dot(), new Gap(20), new Dash(30), new Gap(20));
            googleMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(6)
                    .jointType(JointType.ROUND)
                    .color(Color.parseColor("#4dc412"))//Google maps blue color05b1fb
                    .geodesic(true)
            );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("I am here in", "Exception");
        }
    }


    void startRevealAnimation() {

        int cx = map_root.getMeasuredWidth() / 2;
        int cy = map_root.getMeasuredHeight() / 2;

        Animator anim =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(map_root, cx, cy, 50, map_root.getWidth());
        }

        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator(2));
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });

        anim.start();
    }



    private List<LatLng> decodePoly(String encoded) {
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


    protected void createMarker(final int index, final double latitude, final double longitude, final String markerText, final String imageUrl, final String time, int size) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        LatLng marker = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        String userid = getArguments().getString("user_id");
        final InfoWindowData info = new InfoWindowData();
        info.setImage("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + userid);
        info.setHotel(addresses.get(0).getAddressLine(0));
        info.setFood(addresses.get(0).getSubLocality());
        info.setTransport(addresses.get(0).getLocality());
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getContext());
        googleMap.setInfoWindowAdapter(customInfoWindow);
        final View mCustomMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker1, null);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = sdf.parse(time);
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:a");
            String format = sdf1.format(d);
            //String format = "";

            if (index == 0) {
                                      Marker m =googleMap.addMarker(new MarkerOptions()
                                              .position(new LatLng(latitude, longitude))
                                              .title(markerText)
                                              .snippet("was here at "+format).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_start,mCustomMarkerView))));
                                      m.setTag(info);
                }
            else if(index == size -1)
            {
                Marker m =googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(markerText)
                        .snippet("was here at "+format).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_finish,mCustomMarkerView))));
                m.setTag(info);
                   }
            else {
                Marker m =googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(markerText)
                        .snippet("was here at "+format).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_placeholder,mCustomMarkerView))));
                m.setTag(info);
                m.setTag(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap getMarkerBitmapFromView(int id, View view) {
        final ImageView markerImageView = (ImageView) view.findViewById(R.id.profile_image);
        markerImageView.setImageDrawable(getResources().getDrawable(id));
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
