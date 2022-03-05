package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {
    EditText email, sifre;
    Button kayitol,hesapVar;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        tanimla();
        kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eml = email.getText().toString();
                String sfr = sifre.getText().toString();
                if (!eml.equals("") && !sfr.equals("")) {
                    email.setText("");
                    sifre.setText("");
                    kayitOl(eml, sfr);
                }else{
                    Toast.makeText(getApplicationContext(), "Bilgileri Bos Giremezsin", Toast.LENGTH_SHORT).show();
                }

            }
        });
        hesapVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitOlActivity.this, GirisYapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        sifre = (EditText) findViewById(R.id.sifre);
        kayitol = (Button) findViewById(R.id.kayitol);
        hesapVar = (Button) findViewById(R.id.hesapVar);
    }

    public void kayitOl(String kullaniciMail, String kullaniciSifre) {
        auth.createUserWithEmailAndPassword(kullaniciMail, kullaniciSifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map=new HashMap();
                    map.put("resim","null");
                    map.put("isim","null");
                    map.put("egitim","null");
                    map.put("dogumtarih","null");
                    map.put("hakkimda","null");
                    map.put("state",true);
                    reference.setValue(map);
                    Intent intent = new Intent(KayitOlActivity.this, MailDogrulamaActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Kayit Olma Sirasinda Bir Hata Olustu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
