package com.mayur.shortmessage.data.store;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.mayur.shortmessage.ActivityMain;
import com.mayur.shortmessage.ActivityMessageDetails;
import com.mayur.shortmessage.data.DatabaseHandler;
import com.mayur.shortmessage.data.model.Message;
import com.mayur.shortmessage.data.model.MessageDetails;

import java.util.ArrayList;
import java.util.List;

public class MessageDetailsStore {
	public static final Uri URI = Uri.parse("content://sms/");
	public static final Uri URI_SMS_SENT = Uri.parse("content://sms/sent");
	public static final Uri URI_SMS_FAILED = Uri.parse("content://sms/failed");
	private Context ctx;
	private ContentResolver mResolver;
	//private String mName;
	private long mThreadId;
	//private Adapter mAdapter;
	//private ListView mListView;
	//private ActivityMessageDetails act;

	private List<MessageDetails> mItems = new ArrayList<>();
	private Cursor mCursor;

	private static String[] smsProjection = new String[] {"_id", "thread_id", "address", "body", "date", "type", "read"};
	
	public MessageDetailsStore(Context context, long threadId) {
		ctx = context;
		mResolver = ctx.getContentResolver();
		// mName = name;
		mThreadId = threadId;
		mItems = new ArrayList<>(50);
		// mAdapter = new Adapter();
		// TODO: Make this query faster.
		mCursor = mResolver.query(
				URI,
				smsProjection,
				"thread_id=" + mThreadId,
				null,
				"date ASC"
		);
		update();
	}

	public MessageDetailsStore(Context context) {
		ctx = context;
		mResolver = ctx.getContentResolver();
	}

	public void deleteMessageDetails(long id){
		Uri uriMsg = Uri.parse("content://sms/"+id);
		mResolver.delete(uriMsg, null, null);
	}


	public List<MessageDetails> getAllMessageDetail(){
		return mItems;
	}
	public void update() {
		mCursor.requery();
		mItems.clear();
		
		mCursor.moveToFirst();
		do {
			MessageDetails m = new MessageDetails();
			m.id = mCursor.getLong(0);
			m.threadId = mCursor.getLong(1);
			m.address = mCursor.getString(2);
			m.body = mCursor.getString(3);
			m.date = mCursor.getLong(4);
			m.type = mCursor.getInt(5);
			m.read = mCursor.getInt(6) == 1;
			mItems.add(m);
		} while(mCursor.moveToNext());

		markThreadRead();
		//mAdapter.notifyDataSetChanged();
		//mListView.setSelection(mItems.size() - 1);
	}
	
	private void markThreadRead() {
		ContentValues cv = new ContentValues(1);
		cv.put("read", 1);
		mResolver.update(URI,
				cv,
				"read=0 AND thread_id=" + mThreadId,
				null
				);
	}

	public void SendSms(String address, String  body) {

		if (address.length() < 1) {
			Toast.makeText(ctx, "Enter Phone Number", Toast.LENGTH_LONG).show();
			return;
		}

		ContentValues values = new ContentValues();

		values.put("address", address );
		values.put("body", body );
		values.put("date", System.currentTimeMillis());

		if (sendSMS(address, body)) {
			try {
				MessageDetails m = new MessageDetails();
				m.address = address;
				m.body = body;
				m.date = System.currentTimeMillis();
				m.type = MessageDetails.MESSAGE_TYPE_SENDING;
				ActivityMessageDetails.adapter.add(m);
				ActivityMessageDetails.adapter.notifyDataSetChanged();
				updateSmsInfo(ctx, m);
				addToInboxDataList(ctx, m);
				Toast.makeText(ctx, "Sending message...", Toast.LENGTH_LONG).show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			mResolver.insert(URI_SMS_FAILED, values);
			Toast.makeText(ctx, "Message not send", Toast.LENGTH_LONG).show();
		}
	}

	private static boolean sendSMS(String number, String message) {
		SmsManager smManager = SmsManager.getDefault();
		ArrayList<String> parts = smManager.divideMessage(message);
		try {
			smManager.sendMultipartTextMessage(number, null, parts, null, null);
		} catch (Exception e) {
			Log.d("myCustomTag", "sendSMS Exception: " + e.getMessage());
			return false;
		}

		return true;
	}

	public static void addToInboxDataList(Context ctx, MessageDetails sms) {
		try {
			boolean isNotAvaiable = true;

			for (int i = 0; i < ActivityMain.messageList.size(); i++) {

				if (ActivityMain.messageList.get(i).threadId == sms.threadId) {
					isNotAvaiable = false;

					Message smsObj = ActivityMain.messageList.remove(i);
					smsObj.snippet = sms.body;
					smsObj.date = sms.date;
					smsObj.read = sms.read ;
					ActivityMain.messageList.add(0, smsObj);

					ActivityMain.f_message.mAdapter.notifyDataSetChanged();
					break;
					//return smsObj;
				}

			}

			if (isNotAvaiable) {
				Message smsClone = sms.copyProperty();
				smsClone.contact = new DatabaseHandler(ctx).findContactByNumber(sms.address);

				ActivityMain.messageList.add(0, smsClone);
				ActivityMain.f_message.mAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {

		}
	}

	private void updateSmsInfo(Context ctx, MessageDetails sms) {

		String where = "address=? AND body=?";
		String[] whereArgs = new String[] { "" + sms.address, sms.body };

		Cursor cursor = mResolver.query(URI, smsProjection, where, whereArgs, null); //

		if (cursor.moveToFirst()) {
			try {
				sms.id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
				sms.threadId = cursor.getLong(cursor.getColumnIndexOrThrow("thread_id"));
				sms.address = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
				sms.date = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
				sms.read = cursor.getInt(cursor.getColumnIndexOrThrow("read")) == 1;
				sms.type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
				sms.body = cursor.getString( cursor.getColumnIndexOrThrow("body")).toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
			cursor.moveToNext();
		}
		cursor.close();
	}
}
