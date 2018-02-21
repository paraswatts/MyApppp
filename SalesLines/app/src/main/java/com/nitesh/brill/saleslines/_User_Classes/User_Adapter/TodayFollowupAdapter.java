package com.nitesh.brill.saleslines._User_Classes.User_Adapter;

/**
 * Created by Paras-Android on 22-12-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.TodayFollowUp;

import java.util.List;



/**
 * Created by Belal on 10/18/2017.
 */

public class TodayFollowupAdapter extends RecyclerView.Adapter<TodayFollowupAdapter.MyViewHolder> {

    private List<TodayFollowUp> todayFollowUpList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_today_followup;


        public MyViewHolder(View view) {
            super(view);
            tv_today_followup = (TextView) view.findViewById(R.id.tv_today_followup);

        }
    }


    public TodayFollowupAdapter(List<TodayFollowUp> moviesList, Context context) {
        this.context = context;
        this.todayFollowUpList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_followup_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TodayFollowUp todayFollowUp = todayFollowUpList.get(position);
       // Log.e("name in adapter",""+bishop.getName());
        holder.tv_today_followup.setText("Call "+todayFollowUp.getLeadName()+" @ "+todayFollowUp.getLeadTime());


    }

    @Override
    public int getItemCount() {
        return todayFollowUpList.size();
    }
}