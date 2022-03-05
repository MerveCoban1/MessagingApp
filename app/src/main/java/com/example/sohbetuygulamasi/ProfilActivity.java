package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Models.KullanicilarModels;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    CircleImageView profile_image;
    EditText kullaniciAdi, kullaniciEgitim, kullaniciDogumTarihi, kullaniciHakkinda;
    Button bilgiGuncelleOlButonu, sifreDegis, emailDegis, hesabiSil;
    String imageUrl;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        tanimla();
        bilgileriGetir();
        action();
        kontrol();

    }

    public void kontrol(){
        reference.child(user.getUid()).child("state").setValue(true);
    }

    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Kullanicilar");

        kullaniciAdi = (EditText) findViewById(R.id.kullaniciAdi);
        kullaniciEgitim = (EditText) findViewById(R.id.kullaniciEgitim);
        kullaniciDogumTarihi = (EditText) findViewById(R.id.kullaniciDogumTarihi);
        kullaniciHakkinda = (EditText) findViewById(R.id.kullaniciHakkinda);
        bilgiGuncelleOlButonu = (Button) findViewById(R.id.bilgiGuncelleOlButonu);
        sifreDegis = (Button) findViewById(R.id.sifreDegis);
        emailDegis = (Button) findViewById(R.id.emailDegis);
        hesabiSil = (Button) findViewById(R.id.hesabiSil);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);

    }

    public void bilgileriGetir() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KullanicilarModels kl = snapshot.getValue(KullanicilarModels.class);
                if (!kl.getIsim().equals("null")) {
                    kullaniciAdi.setText(kl.getIsim());
                }
                if (!kl.getEgitim().equals("null")) {
                    kullaniciEgitim.setText(kl.getEgitim());
                }
                if (!kl.getDogumtarih().equals("null")) {
                    kullaniciDogumTarihi.setText(kl.getDogumtarih());
                }
                if (!kl.getHakkimda().equals("null")) {
                    kullaniciHakkinda.setText(kl.getHakkimda());
                }
                imageUrl = kl.getResim();
                if (!imageUrl.equals("null")) {
                    profile_image.setImageBitmap(stringToBitmap(imageUrl));
                } else {
                    profile_image.setImageResource(R.drawable.default_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void action() {
        bilgiGuncelleOlButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        sifreDegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, SifreDegisActivity.class);
                startActivity(intent);
            }
        });
        emailDegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, EmailDegisActivity.class);
                startActivity(intent);
            }
        });
        hesabiSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hesapSilDialogAc();
            }
        });
    }

    public void guncelle() {
        String isim = kullaniciAdi.getText().toString();
        String egitim = kullaniciEgitim.getText().toString();
        String dogum = kullaniciDogumTarihi.getText().toString();
        String hakkimda = kullaniciHakkinda.getText().toString();

        Map map = new HashMap();
        map.put("isim", isim);
        map.put("egitim", egitim);
        map.put("dogumtarih", dogum);
        map.put("hakkimda", hakkimda);
        if (imageUrl.equals("null")) {
            map.put("resim", "null");
        } else {
            map.put("resim", imageUrl);
        }
        reference.child(auth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //degişim gerçekleştikten sonra
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Bilgiler Basariyla Guncellendi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Guncelleme Gerçeklesemedi", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //bu iki fonksiyon galeriyi açıyor.
    public void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            final Uri filePath = data.getData();
            //resmi bitmapa çevircez image viewda göstermek için
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
                profile_image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Picasso.with(ProfilActivity.this).load(filePath).into(profile_image);
            //şimdi uri aldık bu uriyi firebase e atacağız.
            //ilk child klasör ismi ikinci child imageimizin ismidir.
            String isim = kullaniciAdi.getText().toString();
            String egitim = kullaniciEgitim.getText().toString();
            String dogum = kullaniciDogumTarihi.getText().toString();
            String hakkimda = kullaniciHakkinda.getText().toString();
            Map map = new HashMap();
            map.put("isim", isim);
            map.put("egitim", egitim);
            map.put("dogumtarih", dogum);
            map.put("hakkimda", hakkimda);
            map.put("resim", imageToString());
            reference.child(auth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //degişim gerçekleştikten sonra
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Bilgiler Basariyla Guncellendi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Guncellenemedi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void hesapSilDialogAc() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.hesabi_sil_alert_layout, null);
        final EditText hesapSilAlertMail = (EditText) view.findViewById(R.id.hesapSilAlertMail);
        final EditText hesapSilAlertSifre = (EditText) view.findViewById(R.id.hesapSilAlertSifre);
        Button hesabiSilAlert = (Button) view.findViewById(R.id.hesabiSilAlert);
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfilActivity.this);
        alert.setView(view);
        final AlertDialog dialog = alert.create();
        hesabiSilAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mmaill = hesapSilAlertMail.getText().toString();
                String ssifree = hesapSilAlertSifre.getText().toString();
                auth.signInWithEmailAndPassword(mmaill, ssifree).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.cancel();
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Hesap Silindi", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    //bitmap'i stringe çeviriyor bu.Bu şekilde saklayacağız.
    public String imageToString() {
        ByteArrayOutputStream byt = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byt);
        byte[] bytes = byt.toByteArray();
        String imageToString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imageToString;
    }

    //resmi görüntülemek için stringten bitmape çevirdik.
    public Bitmap stringToBitmap(String str) {
        byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }
}
