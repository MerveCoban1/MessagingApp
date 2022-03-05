package Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.MessageModel;
import de.hdodenhof.circleimageview.CircleImageView;

//chat activitydeki recyclerview için adapter oluşturduk.
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    List<String> keysList;
    Context context;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference5;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;
    List<MessageModel> list;
    Boolean state;
    int view_type_send=1,view_type_received=2;

    //keylisti almamızdaki sebep mesajı silerken bu keylisti kullanacağız.
    public MessagesAdapter(List<String> keysList, Context context, Activity activity,List<MessageModel> list) {
        this.keysList = keysList;
        this.context = context;
        this.activity = activity;
        this.list=list;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference5 = firebaseDatabase.getReference().child("Kullanicilar");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        state=false;
    }

    @NonNull
    @Override
    //layout tanımlaması yapılacak burada
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==view_type_send){
            view=LayoutInflater.from(context).inflate(R.layout.message_send_adapter_layout, parent, false);
            return new ViewHolder(view);
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.message_receive_adapter_layout, parent, false);
            return new ViewHolder(view);
            //bunları return edince viewHolderı tetiklemiş oluyoruz.
        }
    }

    //viewlarımıza gerekli atamaları burada yapacağız.
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
        if (list.get(position).getType().equals("text")){
            holder.messageText.setText(list.get(position).getText());
            holder.imageView.setMaxHeight(0);
            holder.imageView.setMaxWidth(0);
        }
        if (list.get(position).getType().equals("image")){
            holder.imageView.setImageBitmap(stringToBitmap(list.get(position).getText()));
            holder.messageText.setMaxHeight(0);
            holder.messageText.setMaxWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //burada viewlarımızı tanımlayacağız.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            if (state==true){
                messageText = (TextView) itemView.findViewById(R.id.message_send_textView);
                imageView=(ImageView)itemView.findViewById(R.id.insOlurSend);
            }else {
                messageText = (TextView) itemView.findViewById(R.id.message_received_textView);
                imageView=(ImageView)itemView.findViewById(R.id.insOlurReceived);
            }


        }
    }

    //mesaj işlemlerinde 2 tane view type kullanmamız gerekiyor. Normalde bu otomatik kendi bir tane viewtype olduğu için alıyormuş ama
    //biz iki tane kullanacağımız için kendimiz yazdık viewtype'ımızı.
    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getFrom().equals(userId)){
            state=true;
            return view_type_send;
        }else{
            state=false;
            return view_type_received;
        }
    }

    public Bitmap stringToBitmap(String str) {
        byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }
}
