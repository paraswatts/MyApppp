package com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.AudioData


/**
 * Created by Web Designing Brill on 04-07-2017.
 */

class Manager_Adapter_Lead(mContext: FragmentActivity, private val albumList: MutableList<AudioData>) : RecyclerView.Adapter<Manager_Adapter_Lead.ViewHolder>()  {

    private val mContext: Context

      var objUsefullData: UsefullData
    lateinit var seekBarProgress: SeekBar
    lateinit var buttonPlayPause: ImageView
    private var mediaFileLengthInMilliseconds: Int = 0
    val mediaPlayer = MediaPlayer()
    private val handler = Handler()

    init {

        this.mContext = mContext
        objUsefullData = UsefullData(mContext)
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_audio_list, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list = albumList[position]


        holder.tv_AudioName.setText(list.AudioName)



    }


    override fun getItemCount(): Int {

        return albumList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tv_AudioName: TextView
        var imageView3: ImageView

        // var tv_AudioData: TextView
        init {
            tv_AudioName = itemView.findViewById(R.id.tv_AudioName) as TextView
            imageView3 = itemView.findViewById(R.id.imageView3) as ImageView


        }
    }
}