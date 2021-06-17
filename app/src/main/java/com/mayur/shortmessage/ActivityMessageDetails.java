package com.mayur.shortmessage;

//import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;

import com.mayur.R;
import com.mayur.shortmessage.adapter.MessageDetailsListAdapter;
import com.mayur.shortmessage.data.Constant;
import com.mayur.shortmessage.data.model.Message;
import com.mayur.shortmessage.data.model.MessageDetails;
import com.mayur.shortmessage.data.store.MessageDetailsStore;

import java.util.ArrayList;
import java.util.List;

public class ActivityMessageDetails extends AppCompatActivity {
	private static final String TAG = "ActivityMessageDetails";
	private Message conversation;
	private List<MessageDetails> message_details = new ArrayList<>();
	private Handler mHandler;

	private ImageButton btn_send;
	private EditText et_content;
	public static MessageDetailsListAdapter adapter;

    private ListView listview;
    private ActionBar actionBar;

	private MessageDetailsStore messageDetailStore;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();

		setContentView(R.layout.activity_message_details);
		// initialize conversation data
		Intent intent = getIntent();
		conversation = (Message) intent.getExtras().getSerializable("obj_conv");

		messageDetailStore = new MessageDetailsStore(this, conversation.threadId);

        initToolbar();

        iniComponen();
		initData();

		//messageChecker();
		// Set activity title
		if(conversation.contact.name!=null){
			actionBar.setTitle(conversation.contact.name);
			actionBar.setSubtitle(conversation.contact.number);
		}
		else{
			actionBar.setTitle(conversation.contact.number);
		}
        listview.requestFocus();
		registerForContextMenu(listview);

		// for system bar in lollipop
		Window window = this.getWindow();

		if (Constant.getAPIVerison() >= 5.0) {
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
		}
	}

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

	public void bindView(){
		try {
			adapter.notifyDataSetChanged();
			listview.setSelectionFromTop(adapter.getCount(), 0);
		}catch (Exception e){

		}
	}

    public void iniComponen(){
        listview    = (ListView) findViewById(R.id.listview);
        btn_send    = (ImageButton) findViewById(R.id.btn_send);
        et_content  = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String number = conversation.contact.number;
				String body = et_content.getText().toString();
				new MessageDetailsStore(getApplicationContext()).SendSms(number, body);
				hideKeyboard();
				et_content.setText("");
				bindView();
			}
		});
        et_content.addTextChangedListener(contentWatcher);
		if(et_content.length()==0){
			btn_send.setEnabled(false);
		}
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				dialogMessageOption(i);
			}
		});
    }

	private void initData(){
		message_details = messageDetailStore.getAllMessageDetail();
		adapter = new MessageDetailsListAdapter(this, message_details);
		listview.setAdapter(adapter);
		listview.setSelectionFromTop(adapter.getCount(), 0);

	}

	private void hideKeyboard(){
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private TextWatcher contentWatcher = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable etd) {
			if(etd.toString().trim().length()==0){
				btn_send.setEnabled(false);
			}else{
				btn_send.setEnabled(true);
			}
			//draft.setContent(etd.toString());
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thread_menu, menu);
		menu.getItem(1).setEnabled(conversation.contact.name == null);
		return true;
	}

	/**
	 *	Handle click on action bar
	 *
	 **/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
			case R.id.action_call:
				actionCallNumber(conversation.contact.number);
				return true;
			case R.id.action_add_contact:
				actionAddContact(conversation.contact.number);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public boolean sendSmsByManager() {
		String number = conversation.contact.number;
		String body = et_content.getText().toString();
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(number,
					null,
					body,
					null,
					null);
			MessageDetails msg = new MessageDetails();
			msg.address = number;
			msg.body = body;
			msg.date = System.currentTimeMillis();
			msg.type = MessageDetails.MESSAGE_TYPE_SENDING;
			adapter.add(msg);
			bindView();
			Toast.makeText(getApplicationContext(), "Sending message...", Toast.LENGTH_LONG).show();
			return true;
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(),"Your sms has failed...", Toast.LENGTH_LONG).show();
			ex.printStackTrace();
			return false;
		}
	}

	private void actionCallNumber(String number){

		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + number));
		startActivity(intent);
//		Intent callIntent = new Intent(Intent.ACTION_CALL);
//		callIntent.setData(Uri.parse("tel:" + number));
//		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(callIntent);
//		Toast.makeText(getApplicationContext(), "Call",Toast.LENGTH_LONG).show();
	}
	private void actionAddContact(String number) {
		// Ask stock contact manager to insert the new contact
		Intent in = new Intent(ContactsContract.Intents.Insert.ACTION);
		in.setType(ContactsContract.RawContacts.CONTENT_TYPE);
		in.putExtra(ContactsContract.Intents.Insert.PHONE, number);
		startActivity(in);
	}
	@Override
	public void onResume(){
		Uri uri = MessageDetailsStore.URI;
		changeObserver = new ChangeObserver();
		this.getContentResolver().registerContentObserver(uri, true, changeObserver);
		super.onResume();
	}

	@Override
	public void onPause(){
		this.getContentResolver().unregisterContentObserver(changeObserver);
		super.onPause();
	}

	private void dialogDetail(MessageDetails m){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		String str_detail = "";
		if( m.fromMe() ){
			str_detail += "To   	: " + m.address +"\n";
			str_detail += "Sent 	: " + Constant.formatTime(m.date);
		} else{
			str_detail += "From		: " + m.address +"\n";
			str_detail += "Received	: " + Constant.formatTime(m.date);
		}

		alert.setTitle("Message details");
		alert.setMessage(str_detail);
		alert.show();

	}
	private void dialogDelete(final MessageDetails m, final int pos){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Delete confirmation");
		alert.setMessage("Message will be deleted");
		alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// update in database
				new MessageDetailsStore(getApplicationContext()).deleteMessageDetails(m.id);
				// update in UI
				adapter.remove(pos);
				adapter.notifyDataSetChanged();
				if (adapter.getCount() == 0) {
					finish();
				} else {
					try {
						ActivityMain.f_message.mAdapter.notifyDataSetChanged();
					} catch (Exception e) {

					}
				}
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();
	}
	private void actionForward(MessageDetails m){
		Intent intent = new Intent(getApplicationContext(), ActivityNewMessage.class);
		intent.putExtra("body", m.body);
		startActivity(intent);
	}

	private void dialogMessageOption(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final MessageDetails m = message_details.get(position);
		builder.setTitle("Message Option");
		ListView listView = new ListView(this);
		listView.setPadding(25, 25, 25, 25);
		String[] stringArray = new String[] { "Detail", "Forward", "Delete" };
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray));
		builder.setView(listView);
		final AppCompatDialog dialog = builder.create();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				dialog.dismiss();
				switch (i){
					case 0:
						//detail
						dialogDetail(m);
					break;
					case 1:
						//forward
						actionForward(m);
					break;
					case 2:
						//delete
						dialogDelete(m, position);
					break;
				}
			}
		});

		dialog.show();
	}

	private ChangeObserver changeObserver;
	// wil update only when there a change
	private class ChangeObserver extends ContentObserver {
		public ChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			try{
				if(!loadRunning) {
					loadRunning = true;
					changeLoad = new ChangeLoad();
					changeLoad.execute("");
				}
			}catch (Exception e){

			}
		}
	}

	private ChangeLoad changeLoad;
	private boolean loadRunning = false;

	private class ChangeLoad extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... strings) {
			try {
				messageDetailStore.update();
			}catch (Exception e){

			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			loadRunning = false;
			message_details = messageDetailStore.getAllMessageDetail();
			bindView();
			super.onPostExecute(s);
		}
	}

}
