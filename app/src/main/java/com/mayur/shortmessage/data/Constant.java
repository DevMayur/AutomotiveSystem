package com.mayur.shortmessage.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Constant {

    public static final String B_KEY_FIRST_LAUNCH = "b_key_first_launch";

    public static final String I_KEY_UNREAD_COUNT = "b_key_unread_count";
    /**
     *  Id of new message notification in status bar
     **/
    public static final int NEW_SMS_NOTIFICATION_ID = 7758258;
    /**
     *  String identifier of SMS received action in Android
     **/
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";


    public static Resources getStrRes(Context context){
        return context.getResources();
    }

    public static String formatTime(long time){
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if(date.get(Calendar.YEAR)==curDate.get(Calendar.YEAR)){
            if(date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR) ){
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            }
            else{
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        }
        else{
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }

    public static boolean isFirstLaunch(Context ctx){
        return SharedPref.getBooleanPref(Constant.B_KEY_FIRST_LAUNCH, true, ctx);
    }
    public static void setFirstLaunch(Context ctx, boolean b){
        SharedPref.setBooleanPref(Constant.B_KEY_FIRST_LAUNCH, b, ctx);
    }

    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "erro ao recuperar a versão da API" + e.getMessage());
        }

        return f.floatValue();
    }

}
