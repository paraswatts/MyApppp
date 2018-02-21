package com.nitesh.brill.saleslines._User_Classes.User_PojoClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Paras-Android on 19-01-2018.
 */

public class TodayFollowUp implements Serializable{
    String leadName;
    String leadTime;

    public TodayFollowUp(String leadName,String leadTime)
    {
        this.leadName = leadName;
        this.leadTime = leadTime;
    }
    public String getLeadName(){return leadName;}
    public void setLeadName(String leadName){this.leadName = leadName;}

    public String getLeadTime(){return leadTime;}
    public void setLeadTime(String leadTime){this.leadTime = leadTime;}



}
