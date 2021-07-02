package com.mayur.document_vault;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mayur.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class  DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {
    private Context context;
    private List<DocumentsModel> dList;

    public DocumentsAdapter(Context context, List<DocumentsModel> dList) {
        this.context = context;
        this.dList = dList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_document_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.button.setText(dList.get(position).getTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocumentViewer.class);
                intent.putExtra("img_url", dList.get(position).getImg_url());
                intent.putExtra("title", dList.get(position).getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.single_button);
        }
    }
}
