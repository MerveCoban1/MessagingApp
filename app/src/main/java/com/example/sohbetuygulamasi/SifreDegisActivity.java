package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SifreDegisActivity extends AppCompatActivity {
    EditText yeniSifre, yeniSifreTekrar;
    Button degistir;
    FirebaseAuth auth;
    FirebaseUser user;
    String ySifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degis);
        tanimla();
        degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yeniSifre.getText().toString().equals(yeniSifreTekrar.getText().toString())) {
                    ySifre = yeniSifre.getText().toString();
                    user.updatePassword(ySifre).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Sifreniz Degistirildi", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SifreDegisActivity.this, ProfilActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Girilen Sifreler Ayni Degil", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        yeniSifre = (EditText) findViewById(R.id.yeniSifre);
        yeniSifreTekrar = (EditText) findViewById(R.id.yeniSifreTekrar);
        degistir = (Button) findViewById(R.id.degistir);
    }
}
