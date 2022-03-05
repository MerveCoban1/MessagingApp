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
import Models.KullanicilarModels;
import com.example.sohbetuygulamasi.R;
import com.example.sohbetuygulamasi.OtherProfilActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

//recyclerview için adapter oluşturduk.
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<String> userKeysList;
    Context context;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeysList, Context context, Activity activity) {
        this.userKeysList = userKeysList;
        this.context = context;
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    //layout tanımlaması yapılacak burada
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    //viewlarımıza gerekli atamaları burada yapacağız.
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
        //holder.usernameTextView.setText(userKeysList.get(position).toString());//kullanıcının keyleri barınsın
        reference.child("Kullanicilar").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KullanicilarModels kl = snapshot.getValue(KullanicilarModels.class);
                holder.userImage.setImageBitmap(stringToBitmap(kl.getResim()));
                holder.usernameTextView.setText(kl.getIsim());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        holder.userAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, OtherProfilActivity.class);
                intent.putExtra("id",userKeysList.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userKeysList.size();
    }

    //burada viewlarımızı tanımlayacağız.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        CircleImageView userImage;
        LinearLayout userAnaLayout;
        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            userImage = (CircleImageView) itemView.findViewById(R.id.userImage);
            userAnaLayout = (LinearLayout) itemView.findViewById(R.id.userAnaLayout);
        }
    }
    public Bitmap stringToBitmap(String str) {
        byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }


}
