package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

/**
 * Created by Sonu Android on 30-05-2017.
 */

public class Audio {

    String mAudioRecording;

    String audioMobile;

    public String getAudioMobile(){return audioMobile;}

    public void setAudioMobile(String audioMobile){this.audioMobile = audioMobile;}

    public String getAudioRecording()
    {
        return mAudioRecording;
    }

    public void setAudioRecording(String audioRecording)
    {
        this.mAudioRecording = audioRecording;
    }
}
