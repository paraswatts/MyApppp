package com.nitesh.brill.saleslines._User_Classes.User_Activity


import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.Settings
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.firebase.jobdispatcher.*
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Authenticate.Login_Activity
import com.nitesh.brill.saleslines.Common_Files.*
import com.nitesh.brill.saleslines.Common_Fragment.*
import com.nitesh.brill.saleslines.FirebaseService.Config
import com.nitesh.brill.saleslines.FirebaseService.MyFirebaseMessagingService
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.*
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper
import com.nitesh.brill.saleslines._User_Classes.User_Fragment.*
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.SaveRegId
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.alert
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class User_Home_Activity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, updateNotIcon, UpdateProfilePicture, LocationListener {
    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    internal var main_view: CoordinatorLayout? = null
    internal var isGPSEnabled = false
    internal var canGetLocation = false
    internal var location: Location? = null // location
    internal var latitude: Double = 0.toDouble() // latitude
    internal var longitude: Double = 0.toDouble() // longitude
    private var provider: String? = null
    protected var locationManager: LocationManager? = null

    var locationSwitch: SwitchCompat? = null

    internal var permissionsRequired = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS)

    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    internal lateinit var packageName: String

    internal var __permissionsRequired = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false
    var dbHelper:AudioDbHelper? = null

    lateinit var tv_Name: TextView
    lateinit var tv_Email: TextView
    var mDemoId: String? = null
    lateinit var imageView: ImageView
    var navigationView: NavigationView? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        NotificationAsyncTask(baseContext).execute();

        ReminderAsyncTask(baseContext).execute();


        val dispatcher = FirebaseJobDispatcher( GooglePlayDriver(this));

        val myJob = dispatcher.newJobBuilder()
                .setService(ScheduledJobService::class.java) // the JobService that will be called
                .setTag("demo")
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(0, 300))
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                        // only run when the device is charging
                )
                .build()
        dispatcher.mustSchedule(myJob);
        val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)
        val settings = getSharedPreferences(
                Constants.LISTEN_ENABLED, 0)
        val editor = settings.edit()
        editor.putBoolean("silentMode", false)
        editor.commit()
        setSharedPreferences(true)
        dbHelper =  AudioDbHelper(this);
        val token = pref.getString("regId", "");
        UsefullData.Log(token)
        saveRegId();
        //==================\\
        if (objSaveData.getString(ConstantValue.UserAppTheme).equals("UserAppTheme")) {
            // do notihing

        } else {
            when (objSaveData.getString(ConstantValue.UserAppTheme).toInt()) {
                0 -> setTheme(R.style.AppTheme)
                1 -> setTheme(R.style.Fuchsia)
                2 -> setTheme(R.style.Red)
            }
        }
//        val gpsTracker =GPSTracker(this)
//        gpsTracker.location

        setContentView(R.layout.activity_home)
        setAlarm()
        val fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
        // callFragment(fragment)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        val mHomeImage = toolbar.findViewById(R.id.toolbarLogo) as ImageView
        val iv_Notifibell = toolbar.findViewById(R.id.iv_Notifibell) as ImageView
        val tv_not_count  = toolbar.findViewById(R.id.tv_not_count) as TextView
        //=======================================\\
        iv_Notifibell.setOnClickListener {

            val fragment =User_NotificationView_Fragment.newInstance("", "")
            //==== Call Fragment  ====\\
            callFragment(fragment)

        }

        getNotCount()



//        Picasso.with(this)
//                .load("http://console.salelinecrm.com/saleslineapi/GetImage/" + objSaveData.getString(ConstantValue.CLIENT_ID))
//                .into(mHomeImage)
        Glide.with(baseContext).asBitmap()
                .load("http://console.salelinecrm.com/saleslineapi/GetImage/" + objSaveData.getString(ConstantValue.CLIENT_ID))
                .into(object: SimpleTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        Log.e("Resource","is ready")

                        mHomeImage.setImageBitmap(resource)
                    }
                })

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setTitle(null);


//        if(!TextUtils.isEmpty(objSaveData.getString("notificationSound")))
//        {
//            var alert: Uri? = RingtoneManager
//                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            if (alert == null) {
//                alert = RingtoneManager
//                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//                if (alert == null) {
//                    alert = RingtoneManager
//                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE)
//                }
//            }
//            objSaveData.save("notificationSound",alert.toString())
//        }

        main_view = findViewById(R.id.main_view) as CoordinatorLayout

        navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView!!.setNavigationItemSelectedListener(this)
        val header = navigationView!!.getHeaderView(0)
        tv_Name = header.findViewById(R.id.tv_Name) as TextView
        tv_Email = header.findViewById(R.id.tv_Email) as TextView
        imageView = header.findViewById(R.id.imageView) as ImageView
        //=====================================\\
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_menu_button_of_three_horizontal_lines, baseContext.theme)
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val menu = navigationView!!.getMenu()
        val menuItem = menu.findItem(R.id.nav_location)
        val actionView = MenuItemCompat.getActionView(menuItem) as View


        var toggle = object:ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerClosed(drawerView: View?) {
                               supportInvalidateOptionsMenu();
            }

            override fun onDrawerOpened(drawerView: View?) {
                supportInvalidateOptionsMenu();
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                main_view!!.setTranslationX(slideOffset * drawerView!!.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        }




        drawer.setDrawerListener(toggle)

        if(drawer.isDrawerOpen(GravityCompat.START)) {
            //drawer is open


        }


        mHomeImage.setOnClickListener {


                for (i in 0 until menu.size()) {
                    val item = menu.getItem(i)
                    if(item.isChecked)
                    {
                        item.setChecked(false)
                    }

                }

            val fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            //==== Call Fragment  ====\\
            callFragment(fragment)

        }
        locationSwitch = actionView.findViewById(R.id.drawer_switch) as SwitchCompat

        if(objSaveData.getString("locationSharing"+objSaveData.getString(ConstantValue.USER_ID)).equals("yes"))
        {
            locationSwitch!!.isChecked = true
        }
        else{
            locationSwitch!!.isChecked = false
        }

        locationSwitch?.setOnCheckedChangeListener({ _, isChecked ->

            if(isChecked){
                getPermissions()

            }
            else{
                objSaveData.save("locationSharing"+objSaveData.getString(ConstantValue.USER_ID), "no")
                cancelAlarm()
            }

            val msg = if (isChecked) "Attendence Started" else "Attendence Stopped"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

        })





        //disableNavigationViewScrollbars(navigationView!!);
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(drawable)

        toggle.toolbarNavigationClickListener = View.OnClickListener {

            UsefullData.Log("=====check image====="+objSaveData.getString("checkProfileImage"))


//            if(objSaveData.getString("checkProfileImage").equals("yes")) {

//            Picasso.with(baseContext)
//                    .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID))
//                    .placeholder(R.drawable.profile_pic)
//                    .into(object: com.squareup.picasso.Target {
//                        override fun onBitmapFailed(errorDrawable: Drawable?) {
//                        }
//
//                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                        }
//
//                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                            Log.e("I am ","here")
//                            imageView.setImageBitmap(bitmap);
//
//                        }
//                    })
//                Picasso.with(this)
//                        .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID)).resize(400, 400)
//                        .placeholder(R.drawable.profile_pic).into(imageView)

            Glide.with(baseContext).asBitmap()
                    .load("http://console.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID))
                    .into(object: SimpleTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                            Log.e("Resource","is ready")

                            imageView.setImageBitmap(resource)
                        }
                    })
//                objSaveData.save("checkProfileImage","no")
//
//
//
//            }

            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)

            } else {
                drawer.openDrawer(GravityCompat.START)

            }
        }
        registerReceiver(myReceiver, IntentFilter(MyFirebaseMessagingService.INTENT_FILTER));
       // val fr = fragmentManager.findFragmentById(R.id.p) as ProfilePic_Upload_Fragment

       // registerReceiver(myReceiver1, IntentFilter(ConstantValue.INTENT_FILTER));

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)

        packageName = "com.nitesh.brill.saleslines";

        //=====================================\\

        tv_Name.setText(objSaveData.getString(ConstantValue.NAME))
        tv_Email.setText(objSaveData.getString(ConstantValue.EMAIL))


//        Picasso.with(this)
//                .load("http://conso
//
// le.salelinecrm.com/saleslineapi/GetprofileImage/" + objSaveData.getString(ConstantValue.USER_ID)).resize(400, 400)
//                .placeholder(R.drawable.profile_pic).into(imageView)

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
                objUsefullData.showMsgOnUI("No Image From Server")
            }
        }


        //=======================================\\
        val notificationFragment = intent.getStringExtra("notificationFragment")
        val addLeadFragment = intent.getStringExtra("addLeadFromCall")
        Log.e("Coming from update","update")
        if (addLeadFragment != null) {
            if (addLeadFragment.equals("addLeadFromCall")) {
                val fragment = User_Add_Enquiry_Fragment.newInstance("", "")
                val bundle = Bundle()
                bundle.putString("phoneNumber", intent.getStringExtra("phoneNumber"));
                bundle.putString("uploadRecordings", "yes");
                fragment.arguments = bundle
                callFragment(fragment)
            }
            if (addLeadFragment.equals("updateLead")) {

                var mFragment = User_Update_Enquiry_Fragment.newInstance(
                        intent.getStringExtra("eid"),
                        intent.getStringExtra("leadId"),
                        intent.getStringExtra("did"),
                        intent.getStringExtra("rating"))
                Log.e("Update fragment","after call"+"Demo id"+intent.getStringExtra("did"))
//                val fragment = User_Update_Enquiry_Fragment.newInstance("" + mEnqId, mLeadId!!, "" + mDemoId, "" + mRating)
//                val fragmentManager = fragmentManager
//                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
//                fragmentTransaction.replace(R.id.content_frame, fragment)
//                fragmentTransaction.commit()

                callFragment(mFragment)
            }
        } else {

            if (notificationFragment != null) {
                Log.e("Fragment paras", "" + notificationFragment);
                if (notificationFragment.equals("missedFollowups")) {
                    val fragment = User_MissedFollowupReminder_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                    val bundle = Bundle()
                    bundle.putString("image", intent.getStringExtra("image"))
                    bundle.putString("reminders", intent.getStringExtra("reminders"))
                    fragment.arguments = bundle

                    //==== Call Fragment  ====\\

                    callFragment(fragment)
                }
                if (notificationFragment.equals("targetReminder")) {

                    val fragment = User_TargetReminder_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                    val bundle = Bundle()
                    bundle.putString("totalTarget", intent.getStringExtra("totalTarget"))
                    bundle.putString("targetAchieved", intent.getStringExtra("targetAchieved"))
                    fragment.arguments = bundle

                    //==== Call Fragment  ====\\

                    callFragment(fragment)
                }
                if (notificationFragment.equals("followUpReminder")) {
                    val fragment = User_FollowUpReminder_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                    val bundle = Bundle()
                    bundle.putString("extraMessage", intent.getStringExtra("extraMessage"))
                    bundle.putString("message", intent.getStringExtra("message"))
                    bundle.putString("lastCalled", intent.getStringExtra("lastCalled"))
                    bundle.putString("image", intent.getStringExtra("image"))
                    bundle.putString("leadMobile", intent.getStringExtra("leadMobile"))
                    bundle.putString("leadSMS", intent.getStringExtra("leadSMS"))
                    fragment.arguments = bundle

                    //==== Call Fragment  ====\\

                    callFragment(fragment)
                }
                if (notificationFragment.equals("saleClosureReminder")) {
                    val fragment = User_SaleCloserReminder_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                    val bundle = Bundle()
                    bundle.putString("image", intent.getStringExtra("image"))
                    bundle.putString("message", intent.getStringExtra("message"))
                    bundle.putString("title", intent.getStringExtra("title"))
                    bundle.putString("extra", intent.getStringExtra("extra"))
                    fragment.arguments = bundle

                    //==== Call Fragment  ====\\

                    callFragment(fragment)
                }
            } else {
                val fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                //==== Call Fragment  ====\\
                callFragment(fragment)
            }
        }
        supportActionBar!!.title = ""
        //setAlarm("PM");
    }
    private fun getPermissions() {


        if (ActivityCompat.checkSelfPermission(this, __permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, __permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale( this,__permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale( this,__permissionsRequired[1])
                    ) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs GPS permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions( this,__permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel()
                    locationSwitch!!.isChecked = false

                }
                builder.show()
            } else if (permissionStatus!!.getBoolean(__permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs GPS permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->

                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)

                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                    locationSwitch!!.isChecked = false

                    builder.show()
                }
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this,__permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }


            val editor = permissionStatus!!
                    .edit()
            editor.putBoolean(__permissionsRequired[0], true)
            editor.commit()


        } else {

            proceedAfterPermission();


        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {

                proceedAfterPermission()


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,__permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this,__permissionsRequired[1])
                           ) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs GPS permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->

                    dialog.cancel()
                    ActivityCompat.requestPermissions( this,__permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    locationSwitch!!.isChecked = false

                    dialog.cancel()
                }
                builder.show()
            } else {

                locationSwitch!!.isChecked = false;
                Toast.makeText(this, "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ContextCompat.checkSelfPermission(this, __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission()
            }
            else
            {

            }
        }

    }


    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission()
            }
            else
            {

            }
        }
    }
    //https://maps.googleapis.com/maps/api/directions/json?origin=17.44638818,78.51188972&waypoints=optimize:true|17.44638818,78.51188972|17.44996022,78.5059135|17.444069,78.49879287|17.44231017,78.49761323|17.44231017,78.49761323|17.44231017,78.49761323|17.43616439,78.50028259|17.43582405,78.49980843|&destination=17.43550938,78.4998931&sensor=false

    fun proceedAfterPermission()
    {

        getLocation();



        Log.e("proceeding","After permission")
        //bt_location.text = "Stop Location Sharing"
        objSaveData.save("locationSharing"+objSaveData.getString(ConstantValue.USER_ID), "yes")
        val dispatcher = FirebaseJobDispatcher( GooglePlayDriver(baseContext));

        val myJob = dispatcher.newJobBuilder()
                .setService(LocationJobService::class.java) // the JobService that will be called
                .setTag(objSaveData.getString(ConstantValue.USER_ID))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(0, 300))
//                .setConstraints(
//                        // only run on an unmetered network
//                        Constraint.ON_ANY_NETWORK
//                        // only run when the device is charging
//                )
                .build()
        dispatcher.mustSchedule(myJob);


        //setAlarm()

        //val locationIntent=Intent(context,GPSTracker_DUP::class.java)
        //context.startService(locationIntent)

    }

    fun getLocation(){
        try {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.powerRequirement = Criteria.POWER_HIGH
            provider = locationManager!!.getBestProvider(criteria, false)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            // getting GPS status
            isGPSEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isGPSEnabled) {
                showSettingsAlert()

            } else {

                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, this)
                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location != null) {
                    Log.e("Provider ", provider + " has been selected." + location!!.getLatitude() + "===" + location!!.getLongitude())
                    saveLocation(location!!.getLatitude(), location!!.getLongitude())

                    onLocationChanged(location)
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveLocation(latitude: Double?, longitude: Double?) {
        objSaveData = SaveData(this)

        Log.e("Saving Coordinates", latitude.toString() + " " + longitude)
        val audioDbHelper = AudioDbHelper(this)
        val userCoordinates = UserCoordinates()
        userCoordinates.latitude = latitude.toString()
        userCoordinates.longitude = longitude.toString()
        userCoordinates.uploaded = "no"
        val objSaveData = SaveData(this)
        userCoordinates.userEmail = objSaveData.getString("LoginId")
        userCoordinates.type = "insert"
        val time = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().time)
        userCoordinates.locationTime = time
        audioDbHelper.addCoordinates(userCoordinates)
    }

    fun showSettingsAlert() {
        val alertDialog = android.app.AlertDialog.Builder(this)

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }


        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }
    fun cancelAlarm() {

//        val locationIntent=Intent(context,GPSTracker_DUP::class.java)
//        context.stopService(locationIntent)
//        Log.e("Cancelling ","alarm")
//        val myIntent = Intent(context,
//                LocationSharingReceiver::class.java)
//        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//        val alarmManager: AlarmManager
//        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//
//        val pendingIntent: PendingIntent
//
//        // A PendingIntent specifies an action to take in the
//        // future
//
//            pendingIntent = PendingIntent.getBroadcast(
//                    context, 300, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//            alarmManager.cancel(pendingIntent)

        getLocation();
        val dispatcher = FirebaseJobDispatcher( GooglePlayDriver(baseContext));
//
////        val myJob = dispatcher.newJobBuilder()
////                .setService(LocationJobService::class.java) // the JobService that will be called
////                .setTag("location")
////                .setReplaceCurrent(false)
////                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
////                .setLifetime(Lifetime.FOREVER)
////                .setRecurring(false)
////                .setTrigger(Trigger.executionWindow(0, 300))
//////                .setConstraints(
//////                        // only run on an unmetered network
//////                        Constraint.ON_ANY_NETWORK
//////                        // only run when the device is charging
//////                )
////                .build()
        dispatcher.cancel(objSaveData.getString(ConstantValue.USER_ID))
    }

    private fun disableNavigationViewScrollbars(navigationView: NavigationView){
        val navigationMenuView = navigationView.getChildAt(0) as NavigationMenuView
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
    }

     val myReceiver = object: BroadcastReceiver() {
    override fun onReceive(context: Context, intent:Intent) {
        Log.e("in","receiver");
       getNotCount()   }
};

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

    override fun getNotCount1() {
        if(dbHelper!!.getNotCount(objSaveData.getString("LoginId"),"no")>0) {
            rl_not_count.visibility = View.VISIBLE
            tv_not_count.text = dbHelper!!.getNotCount(objSaveData.getString("LoginId"), "no").toString();
        }
        else

        {
            rl_not_count.visibility = View.GONE

        }    }
    private fun getNotCount() {
        Log.e("Getting","count")
        if(dbHelper!!.getNotCount(objSaveData.getString("LoginId"),"no")>0) {
            rl_not_count.visibility = View.VISIBLE
            Log.e("Getting","count1")

            tv_not_count.text = dbHelper!!.getNotCount(objSaveData.getString("LoginId"), "no").toString();
        }
        else
        {
            Log.e("Getting","count2")
            rl_not_count.visibility = View.GONE

        }
    }

//    fun getBlogsCount(): Int {
//        val db = this.writableDatabase
//        val countQuery = "SELECT  * FROM " + TABLE_BLOGS
//        val cursor = db.rawQuery(countQuery, null)
//        val count = cursor.count
//        cursor.close()
//        db.close()
//// return count
//        return count
//    }

    fun setAlarm() {

        val rand = Random()
        val n = rand.nextInt(10000) + 1

        val calendar = Calendar.getInstance()
        // set selected time from timepicker to calendar
        calendar.setTimeInMillis(System.currentTimeMillis());

        // if it's after or equal 9 am schedule for next day
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 9) {
            Log.e("", "Alarm will schedule for next day!");
            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }
        else{
            Log.e("", "Alarm will schedule for today!");
        }
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);



//        if (AMPM == "PM") {
//            Log.e("PM", "")
//            calendar.set(Calendar.AM_PM, Calendar.PM)
//        } else {
            //calendar.set(Calendar.AM_PM, Calendar.AM)
       // }
        val myIntent = Intent(this,
                MyReceiver::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        myIntent.putExtra("fetchApiData", "yes")
        val alarmManager: AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val pendingIntent: PendingIntent

        // A PendingIntent specifies an action to take in the
        // future
        val alarmRunning = PendingIntent.getBroadcast(this, 280, myIntent, PendingIntent.FLAG_NO_CREATE) != null
        if (alarmRunning == false) {

            pendingIntent = PendingIntent.getBroadcast(
                    this, 280, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            Log.e("Check Alarm", "Alarm Not running")

            // set alarm time
//        if (Build.VERSION.SDK_INT < 19) {

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent)
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
//                    1000*60*2, pendingIntent)
       // }
//        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 22) {
//            alarmManager.setExact(AlarmManager.RTC,
//                    calendar.timeInMillis, pendingIntent)
//        } else if (Build.VERSION.SDK_INT >= 23) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,
//                    calendar.timeInMillis, pendingIntent)
//        }

        } else {
            Log.e("Check Alarm", "Alarm Already running")
        }
    }


    private fun callFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.commit()
    }


    private fun setSharedPreferences(silentMode: Boolean) {

        objSaveData.saveBoolean("silentMode", silentMode)

        val myIntent = Intent(this, RecordService::class.java)
        myIntent.putExtra("commandType",
                if (silentMode)
                    Constants.RECORDING_DISABLED
                else
                    Constants.RECORDING_ENABLED)
        myIntent.putExtra("silentMode", silentMode)
        startService(myIntent)
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

            R.id.nav_home -> {

                fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                title = " "
            }
            R.id.nav_profile ->

                fragment = Update_profile_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            R.id.nav_picture_upload -> fragment = ProfilePic_Upload_Fragment.newInstance("", "")
            R.id.nav_recomend -> fragment = Recommend_Fragment.newInstance("", "")
            R.id.nav_setting -> {
                fragment = User_Setting_Fragment.newInstance("", "")
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
            supportActionBar!!.setTitle(title)
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun saveRegId() {

        val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)
        val token = pref.getString("regId", "");
            Log.e("Token saving",""+token);
        val mCall = apiEndpointInterface1!!.saveRegId(objSaveData.getString(ConstantValue.FIREBASEID),objSaveData.getString("LoginId"),objSaveData.getString(ConstantValue.NAME))

        mCall.enqueue(object : retrofit2.Callback<SaveRegId> {
            override fun onFailure(call: Call<SaveRegId>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<SaveRegId>?, response: Response<SaveRegId>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "Reg id saving in db===" + response!!.raw().request().url())
                try {
                    if (response.isSuccessful) {


                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })


    }

    //=============logout==================\\
    private fun callLogout() {

        val mCall = apiEndpointInterface!!.mLogout(objSaveData.getString(ConstantValue.USER_ID))

        mCall.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
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
        //objSaveData.remove("locationSharing")
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
            val intent = Intent(this@User_Home_Activity, Login_Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

    } else {

        UsefullData.Log("========" + response.code())
        objUsefullData.showMsgOnUI(resources.getString(R.string.server_error))

    }
}catch (e:Exception){
    e.printStackTrace()
}
            }

        })


    }

    companion object {

        //================ Set Theme ==============\\

        var THEME = 0
    }



}



