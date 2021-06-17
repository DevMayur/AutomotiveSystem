package com.mayur.shortmessage.adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mayur.R;
import com.mayur.shortmessage.ActivityMain;
import com.mayur.shortmessage.ActivityMessageDetails;
import com.mayur.shortmessage.data.Constant;
import com.mayur.shortmessage.data.model.Message;
import com.mayur.shortmessage.data.store.MessageStore;
import com.mayur.shortmessage.widget.CircleTransform;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> implements Filterable {

    private final int mBackground;

    private List<Message> original_items = new ArrayList<>();
    private List<Message> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    private final TypedValue mTypedValue = new TypedValue();

    private Context ctx;
    private ActivityMain act;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView content;
        public TextView time;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            time = (TextView) v.findViewById(R.id.time);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }

    }

    public Filter getFilter() {
        return mFilter;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageListAdapter(ActivityMain act, Context ctx, List<Message> items) {
        this.ctx = ctx;
        this.act = act;
        original_items = items;
        filtered_items = items;
        ctx.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message, parent, false);
        v.setBackgroundResource(mBackground);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final Message c = filtered_items.get(position);
            holder.title.setText(c.contact.getNameOrNumber());

            holder.time.setText(Constant.formatTime(c.date));
            holder.content.setText(c.snippet);
            if (c.contact.photoUri.equals("")) {
                holder.image.setImageResource(R.drawable.unknown_avatar);
            } else {
                Picasso.get().load(c.contact.photoUri)
                        .resize(100, 100)
                        .transform(new CircleTransform())
                        .into(holder.image);
            }
            if (!c.read) {
                holder.title.setTypeface(null, Typeface.BOLD);
                holder.time.setTypeface(null, Typeface.BOLD);
                holder.content.setTypeface(null, Typeface.BOLD);
            } else {
                holder.title.setTypeface(null, Typeface.NORMAL);
                holder.time.setTypeface(null, Typeface.NORMAL);
                holder.content.setTypeface(null, Typeface.NORMAL);
            }

            // view detail message conversation
            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.messageStore.markAsRead(c.threadId);
                    try {
                        act.f_message.mAdapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                    Intent intent = new Intent(ctx, ActivityMessageDetails.class);
                    intent.putExtra("obj_conv", c);
                    ctx.startActivity(intent);
                }
            });

            holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dialogDeleteMessageConfirm(view, c);
                    return false;
                }
            });
        }catch (Exception e){
            Toast.makeText(ctx, "Ups.., Something error", Toast.LENGTH_SHORT).show();
        }
    }


    private void dialogDeleteMessageConfirm(final View view, final Message c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("All message from : " + c.contact.getNameOrNumber() + " will be deleted?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = MessageStore.deleteMessage(ctx, c.threadId + "");
                if (result) {
                    try {
                        Snackbar.make(view, "Delete success", Snackbar.LENGTH_SHORT).show();
                        ActivityMain.messageList.remove(c.threadId);
                        ActivityMain.f_message.mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public long getItemId(int position) {
        return filtered_items.get(position).threadId;
    }

    public int getItemPosition(long threadId){
        int pos = -1;
        for (int i = 0; i < filtered_items.size(); i++) {
            if(filtered_items.get(i).threadId == threadId){
                return i;
            }
        }
        return pos;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Message> list = original_items;
            final List<Message> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).contact.getNameOrNumber();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<Message>) results.values;
            notifyDataSetChanged();
        }

    }
}