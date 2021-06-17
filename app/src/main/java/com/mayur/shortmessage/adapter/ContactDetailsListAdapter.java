package com.mayur.shortmessage.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mayur.R;
import com.mayur.shortmessage.ActivityNewMessage;

import java.util.List;

public class ContactDetailsListAdapter extends BaseAdapter {
	
	Context contex;
	private static List<String> items;

	private LayoutInflater l_Inflater;
	private String name;

	public ContactDetailsListAdapter(Context context, List<String> items, String name) {
		this.contex=context;
		this.items = items;
		l_Inflater = LayoutInflater.from(context);
		this.name  = name;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.row_contact_number, null);
			holder = new ViewHolder();
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.call = (ImageButton) convertView.findViewById(R.id.btn_call);
			holder.create = (ImageButton) convertView.findViewById(R.id.btn_create);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.number.setText(items.get(position));
		holder.create.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(contex, ActivityNewMessage.class);
				i.putExtra("name", name);
				i.putExtra("number", items.get(position));
				contex.startActivity(i);
			}
		});

		holder.call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + items.get(position)));
				callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				contex.startActivity(callIntent);
				Toast.makeText(contex, "Call", Toast.LENGTH_LONG).show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView number;
		ImageButton call;
		ImageButton create;
	}
}
