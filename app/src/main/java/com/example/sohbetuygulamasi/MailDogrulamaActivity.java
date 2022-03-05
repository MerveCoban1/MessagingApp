package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MailDogrulamaActivity extends AppCompatActivity {
    TextView dogrulancakKisi;
    Button emailDogrulamaButtonu;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_dogrulama);
        tanimla();
        dogrulancakKisi.setText(user.getEmail());
        emailDogrulamaButtonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Mail Adresinize Dogrulama Kodu Gonderildi", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        if (user.isEmailVerified()) {
            Intent intent = new Intent(MailDogrulamaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void tanimla() {
        dogrulancakKisi = (TextView) findViewById(R.id.dogrulancakKisi);
        emailDogrulamaButtonu = (Button) findViewById(R.id.emailDogrulamaButtonu);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}
