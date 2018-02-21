package com.nitesh.brill.saleslines._GM_Classes.GM_Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brill.nitesh.punjabpool.Common.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Authenticate.Login_Activity
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.CustomDialogImageView
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.Common_Fragment.*
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._GM_Classes.GM_Fragment.*
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.ReminderAsyncTask
import com.nitesh.brill.saleslines.Common_Files.UpdateProfilePicture
import com.nitesh.brill.saleslines.Common_Files.updateNotIcon
import com.nitesh.brill.saleslines.FirebaseService.MyFirebaseMessagingService
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.alert
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.ByteArrayOutputStream


class GM_Home_Activity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener , updateNotIcon, UpdateProfilePicture {

    var dbHelper: AudioDbHelper? = null

    lateinit var tv_Name: TextView
    lateinit var tv_Email: TextView
    lateinit var imageView: ImageView
    var navigationView: NavigationView? = null


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        ReminderAsyncTask(baseContext).execute();

        //com.nitesh.brill.saleslines.Camera.NotificationAsyncTask(baseContext).execute();

        //==================\\
        if (objSaveData.getString(ConstantValue.GmAppTheme).equals("GmAppTheme")) {
            // do notihing

        } else {
            when (objSaveData.getString(ConstantValue.GmAppTheme).toInt()) {
                0 -> setTheme(R.style.AppTheme)
                1 -> setTheme(R.style.Fuchsia)
                2 -> setTheme(R.style.Red)
            }
        }

        //==================\\

        setContentView(R.layout.activity_home)

        registerReceiver(myReceiver, IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_GM));
        registerReceiver(myReceiver1, IntentFilter(ConstantValue.INTENT_FILTER));

        dbHelper =  AudioDbHelper(this);

        getNotCount()

        val toolbar = findViewById(R.id.toolbar) as Toolbar

        val mHomeImage = toolbar.findViewById(R.id.toolbarLogo) as ImageView

        val iv_Notifibell = toolbar.findViewById(R.id.iv_Notifibell) as ImageView




        Picasso.with(this)
                .load("http://console.salelinecrm.com/saleslineapi/GetImage/" + objSaveData.getString(ConstantValue.CLIENT_ID))
                .into(mHomeImage)
        setSupportActionBar(toolbar)

        mHomeImage.setOnClickListener {

            val fragment = GM_HomeFragment.newInstance("", "")
            //==== Call Fragment  ====\\

            callFragment(fragment)

        }

        //=======================================\\
        iv_Notifibell.setOnClickListener {
            Log.e("====","====iv_Notifibell ==")
            val fragment = GM_NotificationView_Fragment.newInstance("", "")
            //==== Call Fragment  ====\\

            callFragment(fragment)

        }


        navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView!!.setNavigationItemSelectedListener(this)
        val header = navigationView!!.getHeaderView(0)
        tv_Name = header.findViewById(R.id.tv_Name) as TextView
        tv_Email = header.findViewById(R.id.tv_Email) as TextView
        imageView = header.findViewById(R.id.imageView) as ImageView


        //=====================================\\
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_menu_button_of_three_horizontal_lines, baseContext.theme)
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(drawable)


        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }

        //=====================================\\

        getImage()

        if(!TextUtils.isEmpty(objSaveData.getString("notificationSound")))
        {
            var alert: Uri? = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                if (alert == null) {
                    alert = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                }
            }
            objSaveData.save("notificationSound",alert.toString())
        }



        //=====================================\\
        disableNavigationViewScrollbars(navigationView!!)

        tv_Name.text = objSaveData.getString(ConstantValue.NAME)
        tv_Email.text = objSaveData.getString(ConstantValue.EMAIL)

        //imageView.background = resources.getDrawable(R.drawable.ic_user)


        Picasso.with(this)
                .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID)).resize(400, 400)
                .placeholder(R.drawable.profile_pic).into(imageView)
        //=======================================\\

        imageView.setOnClickListener {


            val hasDrawable = imageView.getDrawable() != null
            if (hasDrawable) {
                // imageView has image in it
                val bitmap = (imageView.getDrawable() as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                val intent = Intent(this, CustomDialogImageView::class.java)
                intent.putExtra("byteArray", byteArray)
                startActivity(intent)
            } else {
                objUsefullData.showMsgOnUI("No image from Server")
            }
        }


        //==========================================\\




        //=======================================\\
        val notificationFragment = intent.getStringExtra("gmNotificationFragment")

        val addLeadFragment = intent.getStringExtra("addLeadFromCall")

        if(addLeadFragment!=null)
        {

            if(addLeadFragment.equals("updateLead"))
            {
                var mFragment = Update_Lead_Details.newInstance(
                        intent.getStringExtra("eid"),
                        intent.getStringExtra("leadId"),
                        "",
                        intent.getStringExtra("rating"),
                        intent.getStringExtra("managerId"),
                        intent.getStringExtra("userId"))

                callFragment(mFragment)
            }
        }

        if (notificationFragment != null) {
            Log.e("Notification", "for GM Sale Closure Home")

            if (notificationFragment.equals("gmSaleClosureReminder")) {
                Log.e("Notification", "for GM Sale Closure Home")

                val fragment = GM_SaleClosureReminder_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                val bundle = Bundle()
                bundle.putString("userName", intent.getStringExtra("userName"))
                bundle.putString("image", intent.getStringExtra("image"))
                fragment.arguments = bundle
                //==== Call Fragment  ====\\

                callFragment(fragment)
            }
            if (notificationFragment.equals("gmTargetReminder")) {

                val fragment = GM_TargetReminder_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                val bundle = Bundle()
                bundle.putString("totalTarget", intent.getStringExtra("totalTarget"))
                bundle.putString("targetAchieved", intent.getStringExtra("targetAchieved"))
                fragment.arguments = bundle
                //==== Call Fragment  ====\\

                callFragment(fragment)
            }
        } else {
            val fragment = GM_HomeFragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            //==== Call Fragment  ====\\

            callFragment(fragment)
        }
        supportActionBar!!.title = ""


    }


    private fun disableNavigationViewScrollbars(navigationView: NavigationView){
        val navigationMenuView = navigationView.getChildAt(0) as NavigationMenuView
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
    }

    val myReceiver1 = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent:Intent) {
            navigationView = findViewById(R.id.nav_view) as NavigationView
            val header = navigationView!!.getHeaderView(0)
            imageView = header.findViewById(R.id.imageView) as ImageView
            Log.e("in", "receiverUpdaing dp");
//            navigationView = findViewById(R.id.nav_view) as NavigationView
//            val header = navigationView!!.getHeaderView(0)
//            imageView = header.findViewById(R.id.imageView) as ImageView
            Log.e("Changing profile ","Profile picture change user home activity " +"http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID));
//            val imgFile= File(intent.getStringExtra("imagePath"))
//            if(imgFile.exists()) {
//                Log.e("File ","image exist"+imgFile.absolutePath)
//                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                imageView.setImageBitmap(myBitmap);
//
//            }
//            val target = object : Target {
//                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
//                    imageView.setImageBitmap(bitmap)
//
//                }
//
//                override fun onBitmapFailed(errorDrawable: Drawable) {
//
//                }
//
//                override fun onPrepareLoad(placeHolderDrawable: Drawable) {
//
//                }
//            }
//            Picasso.with(baseContext)
//                    .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString("user_id"))
//                    .into(target)
//
//            imageView.setTag(target)

            Glide.with(context).asBitmap()
                    .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID))
                    .into(object: SimpleTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                            imageView.setImageBitmap(resource)
                        }


                    })

        }
    }

    override fun updateDp(imagePath: String?) {
        Glide.with(baseContext).asBitmap()
                .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID))
                .into(object: SimpleTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        Log.e("Resource","is ready")
                        imageView.setImageBitmap(resource)
                    }
                })
    }

    override  fun updateNotIcon()
    {
        Log.e("Calling interface", "Method in activity")

        rl_not_count.visibility = View.GONE

    }
    private fun getNotCount() {
        if(dbHelper!!.getNotCount(objSaveData.getString("LoginId"),"no")>0) {
            rl_not_count.visibility = View.VISIBLE
            tv_not_count.text = dbHelper!!.getNotCount(objSaveData.getString("LoginId"), "no").toString();
        }
        else

        {
            rl_not_count.visibility = View.GONE

        }
    }

    val myReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent:Intent) {
            getNotCount()   }
    };


    private fun getImage() {


    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {


            val fragments = supportFragmentManager.backStackEntryCount
            if (fragments == 1) {


                //finish()
                alert(title = "Exit App", message = "Do you really want to exit from app.") {
                    positiveButton("Yes") {
                        finish()

                    }
                    negativeButton("No") { }
                }.show()


            } else {
                if (fragmentManager.backStackEntryCount > 1) {
                    fragmentManager.popBackStack()
                } else {
                    super.onBackPressed()
                }
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        var fragment: Fragment? = null
        var title: String? = null
        when (id) {
            R.id.nav_home -> fragment = GM_HomeFragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            R.id.nav_profile -> fragment = Update_profile_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            R.id.nav_picture_upload -> fragment = ProfilePic_Upload_Fragment.newInstance("", "")
            R.id.nav_recomend -> fragment = Recommend_Fragment.newInstance("", "")
            R.id.nav_setting -> {
                fragment = GM_Setting_Fragment.newInstance("", "")
                title = "Settings"
            }
            R.id.nav_video -> fragment = Video_Fragment.newInstance("", "")
            R.id.nav_help -> fragment = Help_Fragment.newInstance("", "")
            R.id.nav_rateapp -> fragment = Rate_Fragment.newInstance("", "")
            R.id.nav_feedback -> fragment = Feedback_Fragment.newInstance("", "")
            R.id.nav_logout -> {
                if (isNetworkConnected) {
                    objUsefullData.showProgress("Logging Out...", "")




                    Handler().postDelayed({
                        try {
                            callLogout()
                        } catch (e: Exception) {
                        }
                    }, 3000)
                }
            }
        }
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, fragment)
            fragmentTransaction.commit()
            supportActionBar!!.title = title
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }



    companion object {

        //================ Set Theme ==============\\

        var THEME = 0
    }


    //=============logout==================\\
    private fun callLogout() {

        val mCall = apiEndpointInterface!!.mLogout(objSaveData.getString(ConstantValue.USER_ID))

        mCall.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Failed to fetch Data")
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                    if (response.isSuccessful) {
                        UsefullData.Log("User_Response    " + response.body().toString())
                        var array = JSONArray(response!!.body().toString())

                        var success = ""
                        var msg = "Logout Successfully"

                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                        }

                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI(msg)


                        } else {
                            objUsefullData.showMsgOnUI("Logout failed")
                        }
                        if (success.equals("1")) {
                            objSaveData.saveBoolean("loginState", false);

                            objUsefullData.showMsgOnUI(msg)
                            objSaveData.save(ConstantValue.USER_LOGIN, "0")
                            val intent = Intent(this@GM_Home_Activity, Login_Activity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                    } else {

                        UsefullData.Log("========" + response.code())
                        objUsefullData.showMsgOnUI(resources.getString(R.string.server_error))

                    }
                }catch (e:Exception){

                }

            }

        })

    }


    override fun getNotCount1() {
        if(dbHelper!!.getNotCount(objSaveData.getString("LoginId"),"no")>0) {
            rl_not_count.visibility = View.VISIBLE
            tv_not_count.text = dbHelper!!.getNotCount(objSaveData.getString("LoginId"), "no").toString();
        }
        else

        {
            rl_not_count.visibility = View.GONE

        }    }
    //==== Call Fragment ===\\

    private fun callFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.commit()
    }
}