package com.example.sohbetuygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    Button cikisYap,profil,anasayfa,mesajlarButton,paylasilanlar,paylasimlarim;
    String kullaniciId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        kontrol();
        cikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);

            }
        });
        anasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnasayfaActivity.class);
                startActivity(intent);
            }
        });
        mesajlarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TumMesajlarActivity.class);
                startActivity(intent);
            }
        });
        paylasilanlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TumPaylasimlarActivity.class);
                startActivity(intent);
            }
        });
        paylasimlarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PaylasimlarimActivity.class);
                startActivity(intent);
            }
        });

    }

    public void tanimla() {
        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Kullanicilar");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        cikisYap=(Button)findViewById(R.id.cikisYap);
        profil=(Button)findViewById(R.id.profil);
        anasayfa=(Button)findViewById(R.id.anasayfa);
        mesajlarButton=(Button)findViewById(R.id.mesajlarButton);
        paylasilanlar=(Button)findViewById(R.id.paylasilanlar);
        paylasimlarim=(Button)findViewById(R.id.paylasimlarim);
        if (user!=null){
            kullaniciId=user.getUid();
        }

    }

    public void kontrol() {
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, KayitOlActivity.class);
            startActivity(intent);
            finish();
        }else{
            reference.child(kullaniciId).child("state").setValue(true);
        }
    }

    public void exit(){
        auth.signOut();
        reference.child(kullaniciId).child("state").setValue(false);
        Intent intent = new Intent(MainActivity.this, KayitOlActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        reference.child(kullaniciId).child("state").setValue(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        reference.child(kullaniciId).child("state").setValue(true);
    }
}
