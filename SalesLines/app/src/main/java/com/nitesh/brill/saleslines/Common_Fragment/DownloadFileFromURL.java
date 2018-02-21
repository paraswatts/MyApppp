package com.nitesh.brill.saleslines.Common_Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.nitesh.brill.saleslines.Common_Files.UsefullData;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by nitesh on 17/11/17.
 */

class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private Context mContext;

    private UsefullData usefullData;

    public DownloadFileFromURL(@Nullable FragmentActivity activity) {
        mContext = activity;
        usefullData = new UsefullData(activity);
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        usefullData.showProgress("Opening file...", "");
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            OutputStream output = new FileOutputStream("/sdcard/SaleLine.amr");


            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage

    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {

        // Reading image path from sdcard
        String filePath = Environment.getExternalStorageDirectory().toString() + "/SaleLine.amr";

        usefullData.dismissProgress();
        //=================== Play media file=================\\
        Uri myUri = Uri.fromFile(new File(filePath.toString()));
        Intent mIntent = new Intent();
        mIntent.setAction(Intent.ACTION_VIEW);
        mIntent.setDataAndType(myUri, "audio/amr");
        mContext.startActivity(mIntent);

    }


}