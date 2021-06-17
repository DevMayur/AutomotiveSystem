package com.mayur.shortmessage.data.store;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mayur.shortmessage.ActivityMain;
import com.mayur.shortmessage.data.Constant;
import com.mayur.shortmessage.data.SharedPref;
import com.mayur.shortmessage.data.model.Contact;
import com.mayur.shortmessage.data.model.Message;
import com.mayur.shortmessage.receiver.BedgeNotifier;

import java.util.ArrayList;
import java.util.List;

public class MessageStore {

	public static final Uri URI = Uri.parse("content://mms-sms/conversations?simple=true");

	public static final Uri URI_SMS = Uri.parse("content://sms");

	private ContentResolver mResolver;
	public static List<Message> allconv = new ArrayList<>();
	static Cursor cursor = null;
	private ContactStore contactStore;
	private ActivityMain act;
	private Context ctx;

	private static final String TAG = "/ConvStore";

	public static final String[] PROJECTION = new String[] {
			"_id",
			"date",
			"message_count",
			"recipient_ids",
			"snippet",
			"read",
			"type"
	};

	public MessageStore(ActivityMain act) {
		this.ctx = act.getApplicationContext();
		this.act = act;
		contactStore = new ContactStore(ctx);
		mResolver = ctx.getContentResolver();
		allconv = new ArrayList<>(20);
		cursor = mResolver.query(URI,
				PROJECTION,
				null,
				null,
				"date DESC"
		);
//		cursor.registerContentObserver(new ChangeObserver());
		rettriveAllConversation();
	}


	private void rettriveAllConversation(){
		update();
	}
	
	public void update() {
		int unread_counter = 0;
		allconv.clear();
		cursor.requery();
		
		if(cursor == null || cursor.getCount() == 0) {
			return;
		}

		cursor.moveToFirst();
		do {

			Message conv = new Message();
			conv.threadId = cursor.getLong(0);
			conv.date = cursor.getLong(1);
			conv.msgCount = cursor.getInt(2);
			conv.snippet = cursor.getString(4);
			conv.read = cursor.getInt(5) == 1;
			if(!conv.read){
				unread_counter++;
			}

			if(!allconv.contains(conv)) {
				allconv.add(conv);
			}

			int recipient_id = cursor.getInt(3);

			Contact recipient = contactStore.getByRecipientId(recipient_id);
			conv.contact = recipient;
		} while(cursor.moveToNext());
		SharedPref.setIntPref(Constant.I_KEY_UNREAD_COUNT, unread_counter, ctx);
		BedgeNotifier.setBadge(ctx, unread_counter);
	}

	public List<Message> getAllconversation() {
		return allconv;
	}

	public void markAsRead(long threadId) {
		ContentValues cv = new ContentValues(1);
		cv.put("read", 1);
		mResolver.update(URI_SMS,
				cv,
				"read=0 AND thread_id=" + threadId,
				null
		);
	}

	/**
	 * Find conversation by phone number
	 *
	 * @return conversation id, new id will be assigned if not exist
	 **/
	public static long getOrCreateConversationId(Context context, String number){
		long cid = -1;
		Uri uri = Uri.parse("content://sms");
		Cursor cur = context.getContentResolver().query(
				uri,
				new String[] {"thread_id"},
				"address =?",
				new String[] { number},
				null);
		if(cur!=null){
			if(cur.moveToFirst()){
				cid = cur.getLong(0);
			}
			cur.close();
		}

		// Allocate new conversation_id if no match
		if(cid==-1){
			cur = context.getContentResolver().query(
					uri,
					new String[] {"thread_id"},
					null,
					null,
					"thread_id DESC ");
			if(cur!=null){
				if(cur.moveToFirst()){
					// new conversation_id = Max(conversation_id)+1
					cid = cur.getLong(0)+1;
				}
				cur.close();
			}
			// no entry in the database, start the id from 10 in case smaller numbers are reserved
			if(cid==-1){
				cid=10;
			}
		}

		return cid;
	}

	public static boolean deleteMessage(Context context, String threadId) {
		int result;
		result = context.getContentResolver().delete(URI_SMS, "thread_id = ?", new String[] { threadId });
		if (result > 0) {
			return true;
		}
		return false;
	}

}
