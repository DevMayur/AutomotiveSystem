package com.mayur.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mayur.model.ReferencesModel;

import java.util.List;

public class ReferencesAdapter extends RecyclerView.Adapter<ReferencesAdapter.ViewHolder> {
    Context context;
    List<ReferencesModel> rList;

    public ReferencesAdapter(Context context, List<ReferencesModel> rList) {
        this.context = context;
        this.rList = rList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
