package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sohbetuygulamasi.ChatActivity;
import com.example.sohbetuygulamasi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Models.KullanicilarModels;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllMessagesAdapter extends RecyclerView.Adapter<AllMessagesAdapter.ViewHolder> {
    List<String> kisiler;
    Context context;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;
    String kisi,kisiIsmi;

    public AllMessagesAdapter(List<String> kisiler, Context context, Activity activity) {
        this.kisiler = kisiler;
        this.context = context;
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_messages_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
        kisi = kisiler.get(position);
        reference.child("Kullanicilar").child(kisi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KullanicilarModels kkk = snapshot.getValue(KullanicilarModels.class);
                holder.kisiResmiAdapter.setImageBitmap(stringToBitmap(kkk.getResim()));
                holder.kisiIsmiAdapter.setText(kkk.getIsim());
                kisiIsmi=kkk.getIsim();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        holder.messagesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("kisiIsmi",kisiIsmi);
                intent.putExtra("digerKisiId",kisi);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kisiler.size();
    }

    //burada viewlarımızı tanımlayacağız.
    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView kisiResmiAdapter;
        TextView kisiIsmiAdapter;
        LinearLayout messagesLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            kisiResmiAdapter = (CircleImageView) itemView.findViewById(R.id.kisiResmiAdapter);
            kisiIsmiAdapter = (TextView) itemView.findViewById(R.id.kisiIsmiAdapter);
            messagesLinearLayout = (LinearLayout) itemView.findViewById(R.id.messagesLinearLayout);
        }
    }

    public Bitmap stringToBitmap(String str) {
        byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

}
