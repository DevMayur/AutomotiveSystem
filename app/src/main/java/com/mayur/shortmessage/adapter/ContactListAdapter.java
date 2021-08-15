package com.mayur.shortmessage.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mayur.R;
import com.mayur.shortmessage.data.model.Contact;
import com.mayur.shortmessage.data.store.ContactStore;
import com.mayur.shortmessage.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> implements Filterable {

    private final int mBackground;
    private ContactStore contactStore;

    private List<Contact> original_items = new ArrayList<>();
    private List<Contact> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    private final TypedValue mTypedValue = new TypedValue();
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            name   = (TextView) v.findViewById(R.id.name);
            image   = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactListAdapter(Context context, List<Contact> items) {
        original_items = items;
        filtered_items = items;
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        ctx = context;
        contactStore = new ContactStore(ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
        v.setBackgroundResource(mBackground);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact c = filtered_items.get(position);
        holder.name.setText(c.name);
        if(c.photoUri.equals("")){
            holder.image.setImageResource(R.drawable.unknown_avatar);
        }else{
            Picasso.get().load(c.photoUri)
                    .resize(100, 100)
                    .transform(new CircleTransform())
                    .into(holder.image);
        }
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAbout(c);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_items.size();
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Contact> list = original_items;
            final List<Contact> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).name;
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
            filtered_items = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    }

    protected void dialogAbout(Contact c) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_details_contact);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ListView list = (ListView) dialog.findViewById(R.id.listView);
        ImageView img = (ImageView) dialog.findViewById(R.id.image);
        TextView name = (TextView) dialog.findViewById(R.id.name);

        Contact contact = contactStore.getDetailsContact(c);
        name.setText(contact.name);
        if(c.photoUri.equals("")){
            img.setImageResource(R.drawable.unknown_avatar);

        }else{
            Picasso.get().load(c.photoUri)
                    .resize(300, 300)
                    .transform(new CircleTransform())
                    .into(img);
        }
        final List<String> phoneArr = contact.allPhoneNumber;

        list.setAdapter(new ContactDetailsListAdapter(ctx, phoneArr, contact.name));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}