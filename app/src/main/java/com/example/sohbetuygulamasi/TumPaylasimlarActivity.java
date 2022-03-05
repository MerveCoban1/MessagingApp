package com.example.sohbetuygulamasi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import Adapters.PaylasimlarRecyclerAdapter;

public class TumPaylasimlarActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button add_post_button;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    ArrayList<String> userEmailFromDb;
    ArrayList<String> userResimFromDb;
    ArrayList<String> userYorumFromDb;

    PaylasimlarRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_paylasimlar);
        tanimla();
        action();
        verileriCek();
        verileriGoster();
    }

    public void tanimla() {
        add_post_button = findViewById(R.id.add_post_button);
        recyclerView = findViewById(R.id.recyclerView);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userEmailFromDb = new ArrayList<>();
        userResimFromDb = new ArrayList<>();
        userYorumFromDb = new ArrayList<>();

    }

    public void action(){
        add_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TumPaylasimlarActivity.this,PaylasimYapActivity.class);
                startActivity(intent);
            }
        });
    }

    public void verileriCek() {

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String resim = (String) data.get("resim");
                        String username = (String) data.get("username");
                        String yorum = (String) data.get("yorum");

                        userEmailFromDb.add(username);
                        userResimFromDb.add(resim);
                        userYorumFromDb.add(yorum);

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void verileriGoster() {
        recyclerView.setLayoutManager(new LinearLayoutManager(TumPaylasimlarActivity.this));
        adapter = new PaylasimlarRecyclerAdapter(userEmailFromDb, userYorumFromDb, userResimFromDb);
        recyclerView.setAdapter(adapter);
    }


}
