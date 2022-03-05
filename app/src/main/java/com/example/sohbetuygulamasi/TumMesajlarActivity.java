package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Adapters.AllMessagesAdapter;
import Adapters.MessagesAdapter;

public class TumMesajlarActivity extends AppCompatActivity {
    RecyclerView tumMesajlarRecyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<String> mesajlasilanKisiler;
    AllMessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_mesajlar);
        tanimla();
        mesajlariListele(user.getUid());
        kontrol();
    }

    public void kontrol(){
        reference.child("Kullanicilar").child(user.getUid()).child("state").setValue(true);
    }

    public void tanimla(){
        mesajlasilanKisiler=new ArrayList<>();
        tumMesajlarRecyclerView=(RecyclerView)findViewById(R.id.tumMesajlarRecyclerView);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
    }
    public void mesajlariListele(String userId){
        reference.child("Mesajlar").child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mesajlasilanKisiler.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(TumMesajlarActivity.this,1);
        tumMesajlarRecyclerView.setLayoutManager(layoutManager);
        adapter=new AllMessagesAdapter(mesajlasilanKisiler,TumMesajlarActivity.this,TumMesajlarActivity.this);
        tumMesajlarRecyclerView.setAdapter(adapter);
    }

}
