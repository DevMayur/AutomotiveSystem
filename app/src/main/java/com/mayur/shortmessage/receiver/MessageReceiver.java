package com.mayur.shortmessage.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

import androidx.core.app.NotificationCompat;

import com.mayur.R;
import com.mayur.shortmessage.ActivityMain;
import com.mayur.shortmessage.data.Constant;
import com.mayur.shortmessage.data.DatabaseHandler;
import com.mayur.shortmessage.data.SharedPref;
import com.mayur.shortmessage.data.model.Contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageReceiver extends BroadcastReceiver {

	private static final String TAG = "MessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		if ( intent.getAction() == Constant.SMS_RECEIVED && prefs.getBoolean("notifications_new_message", true)) {
            abortBroadcast();
            // Parse new message
			Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    String body = messages[0].getMessageBody();
                    String address = extractNumber( messages[0].getOriginatingAddress() );
                    long timeStamp = messages[0].getTimestampMillis();
                    // set status bar notification
                    sendNotification(context, address, body);
					// play vibration
					if(prefs.getBoolean("notifications_new_message_vibrate", true)){
						((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(300);
					}
					String uri_ringtone = prefs.getString("notifications_new_message_ringtone", "content://settings/system/notification_sound");
					RingtoneManager.getRingtone(context, Uri.parse(uri_ringtone)).play();
                }
            }
        }
	}

	private void sendNotification(Context context, String address, String body){
        DatabaseHandler db = new DatabaseHandler(context);
		Contact c = db.findContactByNumber(address);
		String name = c.getNameOrNumber();

		// notify user if this app is NOT running foreground
		// TODO delete notification if this app is launched by the user.
		NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_logo_white)
				.setContentTitle(name)
				.setContentText(body);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, ActivityMain.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity( context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );
		mBuilder.setContentIntent(resultPendingIntent);

		// disappear after clicked
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = Constant.NEW_SMS_NOTIFICATION_ID;
		mNotificationManager.notify(mId, mBuilder.build());

		// set notification bagde
		int unread_count = SharedPref.getIntPref(Constant.I_KEY_UNREAD_COUNT, 0, context);
		unread_count++;
		BedgeNotifier.setBadge(context, unread_count);
		SharedPref.setIntPref(Constant.I_KEY_UNREAD_COUNT, unread_count, context);
	}

	private String extractNumber(String address){
		String number = "";
	    // get numbers
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(address);
		while(matcher.find()){
			number += matcher.group();
		}

		return number;
	}


}
