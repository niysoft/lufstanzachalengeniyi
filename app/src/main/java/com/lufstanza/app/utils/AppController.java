package com.lufstanza.app.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.io.IOException;
import java.io.InputStream;

import eu.giovannidefrancesco.easysharedprefslib.IStorage;
import eu.giovannidefrancesco.easysharedprefslib.SharedPreferenceStorage;

/*import eu.giovannidefrancesco.easysharedprefslib.IStorage;
import eu.giovannidefrancesco.easysharedprefslib.SharedPreferenceStorage;*/
//import com.google.android.material.snackbar.Snackbar;

public class AppController extends Application /*SugarApp*/ {
    private static Context context;
    public static boolean activityVisible;
    private static AppController mInstance;
    public static String PACKAGE_NAME;//new int[]
    private static String SIMPLE_LOGGER = "SIMPLE_LOGGER_LUFSTANZA";
    public  static String DEPARTURE = "";//LOS/BUD
    public static  String ARRIVAL = "";
    public static String DATE = "2019-08-16";
    public static String AUTORIZATION = "Bearer ckfngm37qfrgczm8vpxmez5u";
    public static String [] arrayLoaners;
    public static String JSON_ERROR_STRING = "JSON_ERROR_STRING";//new int[]
    public static String NETWORK_ERROR_STRING = "NETWORK_ERROR_STRING";//new int[]
    public static IStorage storage;
   // public static IStorage storage;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppController.context = getApplicationContext();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        storage = new SharedPreferenceStorage(this, "havypagePref",MODE_PRIVATE);
// save a key value pair

        //storage = new SharedPreferenceStorage(this, "havypagePref", MODE_PRIVATE);
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static void doRoate(View view, int degree, int time, int rotationCount, Boolean start) {
        RotateAnimation rotate = new RotateAnimation(
                0, degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        if (start) {
            rotate.setDuration(time);
            rotate.setRepeatCount(rotationCount);
            view.startAnimation(rotate);
        } else {
            rotate.setRepeatCount(0);
        }
    }

    public static String loadJSONFromAsset(String json_file_name) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(json_file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
           AppController.doToast(ex.toString());
        }
        return json;
    }

    public static int dpToPx(int dp) {
        float density = AppController.getAppContext().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static String makeScheduleQuery(String origin, String destination, String date){
        return origin+"/"+destination+"/"+date;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public static Context getAppContext() {
        return AppController.context;
    }

    public static void doToast(String str) {
        try{
            Log.i(AppController.SIMPLE_LOGGER, str);
        }catch (Exception e){

        }
    }

    public static void setStatusBarTranslucent(boolean makeTranslucent, Activity activity) {
        if (makeTranslucent) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void doSnackBar(String str, Context context, View parent, int color, boolean indefinite) {
        if (context != null) {
          /*  Snackbar snackbar;
            snackbar = Snackbar.make(parent, str, 10000);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor(context.getString(color)));
            ((TextView) (sbView.findViewById(com.google.android.material.R.id.snackbar_text))).setTextColor(Color.WHITE);
            snackbar.show();*/
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public class Demacator{

        public Demacator(int number) {
            this.number = number;
        }

        private int number;
        public int getNumber(){
            return number;
        }

        public void setNumber(int number){
            this.number = number;
        }
    }
}

