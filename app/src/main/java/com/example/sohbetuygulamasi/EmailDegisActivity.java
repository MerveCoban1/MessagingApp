package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailDegisActivity extends AppCompatActivity {
    EditText yeniEmail;
    Button degistirEmail;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_degis);
        tanimla();
        degistirEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.updateEmail(yeniEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(EmailDegisActivity.this, MailDogrulamaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        yeniEmail = (EditText) findViewById(R.id.yeniEmail);
        degistirEmail = (Button) findViewById(R.id.degistirEmail);
    }
}
