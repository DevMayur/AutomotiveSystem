package com.mayur.document_vault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mayur.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DocumentVaultActivity extends AppCompatActivity {

    FloatingActionButton fab_add;
    RecyclerView recyclerView;
    DocumentsAdapter adapter;
    List<DocumentsModel> dList;
    String mobile_number = "9975888110";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_vault);

        fab_add = findViewById(R.id.fab_add_document);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentVaultActivity.this,DocumentUploaderActivity.class));
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        dList = new ArrayList<>();
        adapter = new DocumentsAdapter(this,dList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getData();

    }

    private void getData() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(mobile_number + "_documents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Toast.makeText(DocumentVaultActivity.this, "No Documents Added!", Toast.LENGTH_SHORT).show();
                            } else {
                                for (DocumentChange doc : task.getResult().getDocumentChanges()) {
                                    DocumentsModel model = doc.getDocument().toObject(DocumentsModel.class);
                                    dList.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}