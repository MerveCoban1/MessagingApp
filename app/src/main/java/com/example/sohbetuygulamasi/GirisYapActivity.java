package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisYapActivity extends AppCompatActivity {
    EditText email, sifre;
    TextView sifremiUnuttum;
    Button girisYap,hesapYok;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);
        tanimla();
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ema = email.getText().toString();
                String pass = sifre.getText().toString();
                if (!ema.equals("") && !pass.equals("")) {
                    sistemeGiris(ema, pass);
                }
            }
        });
        hesapYok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisYapActivity.this, KayitOlActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diyalogAc();
            }
        });
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        email = (EditText) findViewById(R.id.email);
        sifre = (EditText) findViewById(R.id.sifre);
        sifremiUnuttum = (TextView) findViewById(R.id.sifremiUnuttum);
        girisYap = (Button) findViewById(R.id.girisYap);
        hesapYok = (Button) findViewById(R.id.hesapYok);
    }

    public void sistemeGiris(String mail, String password) {
        auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(GirisYapActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "giris yapma sirasinda bir hata olustu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void diyalogAc(){
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.sifre_sifirla_alert_layout,null);
        Button evet=(Button)view.findViewById(R.id.evet);
        final EditText grlnMail=(EditText)view.findViewById(R.id.grlnMail);
        AlertDialog.Builder alert=new AlertDialog.Builder(GirisYapActivity.this);
        alert.setView(view);
        final AlertDialog dialog=alert.create();
        evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                auth.sendPasswordResetEmail(grlnMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sifirlama Linki Gonderildi",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GirisYapActivity.this, GirisYapActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
