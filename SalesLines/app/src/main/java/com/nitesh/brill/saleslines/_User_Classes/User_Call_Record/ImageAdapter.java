package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	
	// Keep all Images in array
	public String[] mThumbIds = {
			"1","2","3","4","5","6","7","8","9","*","0","#"
	};
	
	// Constructor
	public ImageAdapter(Context c){
		mContext = c;
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {			
		TextView imageView = new TextView(mContext);
        imageView.setText(mThumbIds[position]);
        imageView.setTextSize(30);

		imageView.setGravity(Gravity.CENTER_HORIZONTAL);


        return imageView;
	}

}
