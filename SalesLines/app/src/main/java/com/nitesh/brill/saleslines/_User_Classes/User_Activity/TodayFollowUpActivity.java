package com.nitesh.brill.saleslines._User_Classes.User_Activity;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Adapter.TodayFollowupAdapter;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.APIClient;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.AlarmGIFActivity;
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.TodayFollowUp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TodayFollowUpActivity extends AppCompatActivity {
    private List<TodayFollowUp> dataList = new ArrayList<>();
    RecyclerView recyclerView;
    TodayFollowUp todayFollowUp;
    SaveData objSaveData;
    TodayFollowupAdapter todayFollowupAdapter;
    ImageView iv_close;
    TextView tv_current_time_today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        setContentView(R.layout.targetreminder);

        objSaveData = new SaveData(this);
        ArrayList<TodayFollowUp> myList = (ArrayList<TodayFollowUp>) getIntent().getSerializableExtra("arrayList");

        todayFollowupAdapter = new TodayFollowupAdapter(myList, null);
        recyclerView = (RecyclerView) findViewById(R.id.rc_todayFollowups);
        iv_close = (ImageView)findViewById(R.id.iv_close);
        tv_current_time_today = (TextView) findViewById(R.id.tv_current_time_today);
        String currentDateTimeString = new SimpleDateFormat("dd-MMM-yyyy hh:mm:a").format(new Date());
        tv_current_time_today.setText(currentDateTimeString);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TodayFollowUpActivity.this.finish();
                //finish();
            }
        });


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
            //recyclerView.addItemDecoration(itemDecor);
            recyclerView.setAdapter(todayFollowupAdapter);
            //getData();

        todayFollowupAdapter.notifyDataSetChanged();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    private void getData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....");
        progressDialog.setCancelable(false);
        progressDialog.show();





        ApiEndpointInterface apiEndpointInterface = APIClient.getClient().create(ApiEndpointInterface.class);

        Call<JsonElement> call = apiEndpointInterface.getFollowupsToday(objSaveData.getString("user_id"), Integer.parseInt(objSaveData.getString("client_id")));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
               Log.d("URL", "=====" + response.raw().request().url());
                Log.e("getting our vicar", ""+response.body().toString());

                if(response.isSuccessful() && response.body()!=null) {
                    try {
                        //dbHelper.deleteTable(DbContract.DbEntry.TABLE_OUR_BISHOPS);

                        JSONArray jsonArray =  new JSONArray(response.body().toString());
                        Log.e("Array Length",""+jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                //     Log.e("Name ",""+jsonObject1.getString("name"));
                                todayFollowUp = new TodayFollowUp(jsonObject1.getString("LeadName")
                                        , jsonObject1.getString("NxtInteractDate")

                                );
                                dataList.add(todayFollowUp);
                                //dbHelper.addOurBishops(bishopModel);


                            }

                            todayFollowupAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        e.printStackTrace();
                       // Log.e("Exception bishop",e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }


}
