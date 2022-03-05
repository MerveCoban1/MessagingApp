package com.example.sohbetuygulamasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.MessagesAdapter;
import Models.KullanicilarModels;
import Models.MessageModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    TextView chat_userName_textView,chat_state_textView;
    EditText send_message_EditText;
    CircleImageView mesajlasilanKisiResmi;
    ImageView chat_state_imageView;
    Button chat_send_message_buton,chat_send_image_buton;
    RecyclerView chat_activity_recyclerView;
    String kisiIsmi,otherUserId;
    FirebaseDatabase database;
    DatabaseReference reference,reference2;
    FirebaseAuth auth;
    FirebaseUser user;
    List<MessageModel> list;
    MessagesAdapter adapter;
    List<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        action();
        kisiResmiCek();
        otherKisiCevrimIciKontrolu();
        kontrol();
    }

    public void kontrol(){
        reference.child("Kullanicilar").child(user.getUid()).child("state").setValue(true);
    }

    public void getValues(){
        Bundle bundle=getIntent().getExtras();
        kisiIsmi=bundle.getString("kisiIsmi");
        otherUserId=bundle.getString("digerKisiId");
    }

    public void tanimla(){
        getValues();
        chat_userName_textView=(TextView)findViewById(R.id.chat_userName_textView);
        chat_state_textView=(TextView)findViewById(R.id.chat_state_textView);
        chat_send_message_buton=(Button)findViewById(R.id.chat_send_message_buton);
        chat_send_image_buton=(Button)findViewById(R.id.chat_send_image_buton);
        send_message_EditText=(EditText) findViewById(R.id.send_message_EditText);
        chat_activity_recyclerView=(RecyclerView) findViewById(R.id.chat_activity_recyclerView);
        mesajlasilanKisiResmi=(CircleImageView) findViewById(R.id.mesajlasilanKisiResmi);
        chat_state_imageView=(ImageView) findViewById(R.id.chat_state_imageView);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        reference2=database.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        list=new ArrayList<>();
        keyList=new ArrayList<>();
        loadMessage();

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(ChatActivity.this,1);
        chat_activity_recyclerView.setLayoutManager(layoutManager);
        adapter=new MessagesAdapter(keyList,ChatActivity.this,ChatActivity.this,list);
        chat_activity_recyclerView.setAdapter(adapter);

    }

    public void action(){
        chat_userName_textView.setText(kisiIsmi);
        chat_send_message_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=send_message_EditText.getText().toString();
                send_message_EditText.setText("");
                sendMessage(user.getUid(),otherUserId,"text",getDate(),false,message);
            }
        });
        chat_send_image_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ResimYollaChatActivity.class);
                intent.putExtra("digerKisiIdsi",otherUserId);
                intent.putExtra("kisiIsmisi",kisiIsmi);
                startActivity(intent);
            }
        });
    }

    //ne tür bir mesaj göndereceksek onu belirtmek için textTipe önemli resim mi ses mi
    public void sendMessage(final String userId, final String otherId, String textTipe, String date, Boolean seen, String messageText){
        //önce bir key almamız lazım her bir mesaj için.Her bir mesaja id vermek istiyoruz biz.
        final String mesajId=reference.child("Mesajlar").child(userId).child(otherId).push().getKey();
        final Map map=new HashMap();
        map.put("type",textTipe);
        map.put("seen",seen);
        map.put("time",date);
        map.put("text",messageText);
        map.put("from",userId);
        //mesajları listelerken ne tarafa doğru listelenmesi gerektiğini from'a bakarak belirleyeceğiz.
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

    //mesajların listelenmesi işlemini gerçekleştireceğiz.
    public void loadMessage(){
        reference.child("Mesajlar").child(user.getUid()).child(otherUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel=snapshot.getValue(MessageModel.class);
                list.add(messageModel);
                adapter.notifyDataSetChanged();
                keyList.add(snapshot.getKey());
                chat_activity_recyclerView.scrollToPosition(list.size()-1);
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
    }

    public void kisiResmiCek(){
        reference.child("Kullanicilar").child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KullanicilarModels kullanici=snapshot.getValue(KullanicilarModels.class);
                mesajlasilanKisiResmi.setImageBitmap(stringToBitmap(kullanici.getResim()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public Bitmap stringToBitmap(String str) {
        byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public void otherKisiCevrimIciKontrolu(){
        reference2.child("Kullanicilar").child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KullanicilarModels durum=snapshot.getValue(KullanicilarModels.class);
                Boolean cevrimicilik=durum.getState();
                if (cevrimicilik){

                }else{
                    chat_state_textView.setText("cevrimdışı");
                    chat_state_imageView.setImageResource(R.drawable.black_circle);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
