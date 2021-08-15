package com.mayur.shortmessage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mayur.R;
import com.mayur.shortmessage.data.Constant;
import com.mayur.shortmessage.data.model.MessageDetails;
import com.mayur.shortmessage.data.store.MessageDetailsStore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityNewMessage extends AppCompatActivity {
	private final static String TAG = "ActivityNewMessage";
	private Button btn_send;
	private ImageButton btn_add_rec;
	private EditText et_to;
	private EditText et_content;
	private MessageDetails draft;
	private long cid;

    private ActionBar actionBar;

	private MessageDetailsStore messageDetailStore;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);

        initToolbar();
		// initialize a empty draft
		cid = -1;
		//draft = new MessageDetails(-1, cid, "", MessageDetails.MESSAGE_TYPE_DRAFT, false, 0, null, null);

		btn_send = (Button) findViewById(R.id.btn_send);
		btn_add_rec = (ImageButton) findViewById(R.id.btn_add);
		et_to = (EditText) findViewById(R.id.text_to);
		et_content = (EditText) findViewById(R.id.text_content);

		messageDetailStore = new MessageDetailsStore(getApplicationContext());

		Intent in = getIntent();
		String name = in.getStringExtra("name");
		String number = in.getStringExtra("number");
		if(name!=null && number!=null){
			et_to.setText(String.format("%s <%s>", name, number));
		}

		btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            Log.i(TAG, "Send message");
			messageDetailStore.SendSms(parseRecipient(), et_content.getText().toString());
			finish();
//            if(sendSmsByManager()){
//            	// clean content text
//               	et_content.setText("");
//               	finish();
//            }
            }
        });
		btn_add_rec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Add recipients");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });
		et_content.addTextChangedListener(contentWatcher);

		Intent intent = getIntent();
		et_content.setText(intent.getStringExtra("body"));
		if(et_content.length()==0){
			btn_send.setEnabled(false);
		}

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

	public boolean sendSmsByManager() {
		String number = parseRecipient();
		if(number.trim()==""){
			Toast.makeText(this, "Invalid recipient:"+et_to.getText().toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(number,
					null,
					et_content.getText().toString(),
					null,
					null);
			Toast.makeText(getApplicationContext(), "Sending message...", Toast.LENGTH_LONG).show();
			return true;
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(),"Your sms has failed...", Toast.LENGTH_LONG).show();
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public void onDestroy(){
//		String number = parseRecipient();
//		if(number!="" && draft.getContent().length()!=0){
//			Contact receiver = ContactManager.getContactByNumber(this, number);
//			cid = MessageManager.getOrCreateConversationId(this, number);
//			draft.setConversationId(cid);
//			draft.setReceiver(receiver);
//			draft.setTimeStamp(System.currentTimeMillis());
//			draft.insert(this);
//		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	// finish this activity when up button is clicked.
            	// User will back to where he is from.
                finish();
                return true;
        }
        return false;
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				Cursor c = null;
				try {
					c = getContentResolver().query(uri, new String[]{
									ContactsContract.CommonDataKinds.Phone.NUMBER,
									ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_PRIMARY},
							null, null, null);

					if (c != null && c.moveToFirst()) {
						String number = c.getString(0);
						String name = c.getString(1);
						String recipent = String.format("%s <%s>", name, number);
						et_to.setText(recipent);
					}
				} finally {
					if (c != null) {
						c.close();
					}
				}
			}
		}
	}

	public void showSelectedNumber(String type, String number) {
	    Toast.makeText(this, type + ": " + number, Toast.LENGTH_LONG).show();
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
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub

		}
	};

	private String parseRecipient(){
		String to = et_to.getText().toString();
		String number = "";
		if(to.length()!=0){
			if(to.contains("<")){
				// get <content> inside <>
				Pattern pattern = Pattern.compile("<(.*?)>");
				Matcher matcher = pattern.matcher(to);
				if (matcher.find()){
				    to = matcher.group(1);
				    // get numbers
					pattern = Pattern.compile("\\d+");
					matcher = pattern.matcher(to);
					while(matcher.find()){
						number += matcher.group();
					}
				}
			}else{
				// input must be all numberic characters.
				if(to.matches("^[0-9]+$")){
					number = to;
				}
			}
		}
		return number;
	}


}
