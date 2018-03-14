package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

/**
 * Created by Paras Android on 04-09-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nitesh.brill.saleslines.Common_Files.ConstantValue;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;

import java.io.File;


public class CustomDialog extends AppCompatActivity implements ConstantValue {
    Button bt_yes, bt_no;
    CheckBox mRememberCheckBox;
    SaveData saveData;
    TextView tv_leadExist, tv_leadAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setFinishOnTouchOutside(false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_dialog);
            this.setFinishOnTouchOutside(false);
            saveData = new SaveData(this);
            tv_leadAdd = (TextView)findViewById(R.id.tv_leadAdd);
            tv_leadExist = (TextView)findViewById(R.id.tv_leadExist);
            initializeContent();

            if(getIntent().getStringExtra("leadExist").equals("yes"))
            {
                tv_leadAdd.setText("Do you want to update this Lead");
                tv_leadExist.setText("Lead already exists in our Database");
                bt_yes.setOnClickListener(new View.OnClickListener()
                {

                    public void onClick(View v)
                    {

                        Log.e("","Lead Does not Exist");
                        Intent addLeadIntent = new Intent(CustomDialog.this,User_Home_Activity.class);
                        addLeadIntent.putExtra("phoneNumber",getIntent().getStringExtra("phoneNumber"));
                        addLeadIntent.putExtra("addLeadFromCall","updateLeadFromCall");
                        addLeadIntent.putExtra("uploadRecordings","yes");
                        startActivity(addLeadIntent);
                        //finish();
                    }
                });
                bt_no.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

//                        File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//
//                        final File[] files = file.listFiles();
//                        for (File f : file.listFiles()) {
//                            if (f.getName().startsWith(saveData.getString("MobileNumber"))) {
//                                Log.e("File delete karan laga", f.getAbsolutePath() + "");
//                                f.delete();
//                            }
//                        }
//                        saveData.remove("MobileNumber");
//                        if(saveData.getString("MobileNumber")!=null) {
//                            Log.e("Key Removed", saveData.getString("MobileNumber"));
//                        }

                        finish();
                    }
                });

            }
            else if (getIntent().getStringExtra("leadExist").equals("no"))
            {
                tv_leadAdd.setText("Do you want to add as a Lead");
                tv_leadExist.setText("Lead does not exist in our Database");
                bt_yes.setOnClickListener(new View.OnClickListener()
                {

                    public void onClick(View v)
                    {

                        Log.e("","Lead Does not Exist");
                        Intent addLeadIntent = new Intent(CustomDialog.this,User_Home_Activity.class);
                        addLeadIntent.putExtra("phoneNumber",getIntent().getStringExtra("phoneNumber"));
                        addLeadIntent.putExtra("addLeadFromCall","addLeadFromCall");
                        addLeadIntent.putExtra("uploadRecordings","yes");
                        startActivity(addLeadIntent);
                    }
                });
                bt_no.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

//
                        finish();
                    }
                });

            }


        }
        catch (Exception e)
        {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
    }

    private void initializeContent()
    {
        bt_yes   = (Button) findViewById(R.id.bt_yes);
        bt_no   = (Button) findViewById(R.id.bt_no);
    }

//    @Override
//    protected void onPause() {
//        try {
//            File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//
//            final File[] files = file.listFiles();
//            for (File f : file.listFiles()) {
//                if (f.getName().startsWith(saveData.getString("MobileNumber"))) {
//                    Log.e("File delete karan laga", "on Pause"+f.getAbsolutePath() + "");
//                    f.delete();
//                }
//            }
//
//            saveData.save(saveData.getString("MobileNumber"), "yes");
//            saveData.remove("MobileNumber");
//            if (saveData.getString("MobileNumber") != null) {
//                Log.e("Key Removed", saveData.getString("MobileNumber"));
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onBackPressed() {
//        try {
//            File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//
//            final File[] files = file.listFiles();
//            for (File f : file.listFiles()) {
//                if (f.getName().startsWith(saveData.getString("MobileNumber"))) {
//                    Log.e("File delete karan laga", "on Back Pressed"+f.getAbsolutePath() + "");
//                    f.delete();
//                }
//            }
//
//            saveData.save(saveData.getString("MobileNumber"), "yes");
//            saveData.remove("MobileNumber");
//            if (saveData.getString("MobileNumber") != null) {
//                Log.e("Key Removed", saveData.getString("MobileNumber"));
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onDestroy() {
//        try {
//            File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//
//            final File[] files = file.listFiles();
//            for (File f : file.listFiles()) {
//                if (f.getName().startsWith(saveData.getString("MobileNumber"))) {
//                    Log.e("File delete karan laga", "on destroy"+f.getAbsolutePath() + "");
//                    f.delete();
//                }
//            }
//
//            saveData.save(saveData.getString("MobileNumber"), "yes");
//            saveData.remove("MobileNumber");
//            if (saveData.getString("MobileNumber") != null) {
//                Log.e("Key Removed", saveData.getString("MobileNumber"));
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//
//        }
//        super.onDestroy();
//    }
}
