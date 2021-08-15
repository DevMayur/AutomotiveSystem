package com.mayur.shortmessage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayur.R;
import com.mayur.shortmessage.ActivityMain;
import com.mayur.shortmessage.adapter.ContactListAdapter;
import com.mayur.shortmessage.widget.DividerItemDecoration;
import com.google.android.material.snackbar.Snackbar;

public class ContactFragment extends Fragment {

    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    public ContactListAdapter mAdapter;

    private ProgressBar progressBar;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new ContactListAdapter( getActivity(), ((ActivityMain) getActivity()).contacsStore.getAllContacts());
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onRefreshLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void onStopRefreshLoading(){
        try {
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }catch (Exception e){

        }
        recyclerView.setVisibility(View.VISIBLE);
        Snackbar.make(view, "Refresh contact finished.", Snackbar.LENGTH_SHORT).show();
    }

}
