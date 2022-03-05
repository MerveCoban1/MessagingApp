package com.example.sohbetuygulamasi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import Adapters.PaylasimlarimRecyclerAdapter;

public class PaylasimlarimActivity extends AppCompatActivity {

    RecyclerView recyclerViewPaylasimlarim;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    ArrayList<String> userEmailFromDb;
    ArrayList<String> userResimFromDb;
    ArrayList<String> userYorumFromDb;
    ArrayList<String> gonderiIdFromDb;

    PaylasimlarimRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasimlarim);
        tanimla();
        verileriCek();
        verileriGoster();
    }

    public void tanimla() {

        recyclerViewPaylasimlarim = findViewById(R.id.recyclerViewPaylasimlarim);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userEmailFromDb = new ArrayList<>();
        userResimFromDb = new ArrayList<>();
        userYorumFromDb = new ArrayList<>();
        gonderiIdFromDb = new ArrayList<>();

    }


    public void verileriCek() {

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        String mailee = user.getEmail().toString();
        collectionReference.whereEqualTo("mail", mailee).orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String resim = (String) data.get("resim");
                        String username = (String) data.get("username");
                        String yorum = (String) data.get("yorum");
                        String gonderiId=(String)data.get("gonderiid");

                        userEmailFromDb.add(username);
                        userResimFromDb.add(resim);
                        userYorumFromDb.add(yorum);
                        gonderiIdFromDb.add(gonderiId);

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void verileriGoster() {
        recyclerViewPaylasimlarim.setLayoutManager(new LinearLayoutManager(PaylasimlarimActivity.this));
        adapter = new PaylasimlarimRecyclerAdapter(userEmailFromDb, userYorumFromDb, userResimFromDb,gonderiIdFromDb,PaylasimlarimActivity.this,PaylasimlarimActivity.this);
        recyclerViewPaylasimlarim.setAdapter(adapter);
    }
}
