package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResimYollaChatActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView gonderilecekResimImageView;
    Button resmiKisiyeGonderButon;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String otherUserId,kisiIsmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim_yolla_chat);
        tanimla();
        getValues();
        galeriAc();
        resmiKisiyeGonderButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=imageToString();
                sendMessage(user.getUid(),otherUserId,"image",getDate(),false,message);
                Intent intent = new Intent(ResimYollaChatActivity.this, ChatActivity.class);
                intent.putExtra("kisiIsmi",kisiIsmi);
                intent.putExtra("digerKisiId",otherUserId);
                startActivity(intent);
                finish();
            }
        });

    }

    public void tanimla(){
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        gonderilecekResimImageView=(ImageView)findViewById(R.id.gonderilecekResimImageView);
        resmiKisiyeGonderButon=(Button)findViewById(R.id.resmiKisiyeGonderButon);
    }

    public void getValues(){
        Bundle bundle=getIntent().getExtras();
        otherUserId=bundle.getString("digerKisiIdsi");
        kisiIsmi=bundle.getString("kisiIsmisi");
    }

    public void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            final Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                gonderilecekResimImageView.setImageBitmap(bitmap);
                gonderilecekResimImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String imageToString() {
        ByteArrayOutputStream byt = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byt);
        byte[] bytes = byt.toByteArray();
        String imageToString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imageToString;
    }

    public void sendMessage(final String userId, final String otherId, String textTipe, String date, Boolean seen, String messageText){
        final String mesajId=reference.child("Mesajlar").child(userId).child(otherId).push().getKey();
        final Map map=new HashMap();
        map.put("type",textTipe);
        map.put("seen",seen);
        map.put("time",date);
        map.put("text",messageText);
        map.put("from",userId);
        reference.child("Mesajlar").child(userId).child(otherId).child(mesajId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherId).child(userId).child(mesajId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }

    public String getDate(){
        DateFormat df=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today= Calendar.getInstance().getTime();
        String reportDate=df.format(today);
        return reportDate;
    }
}
