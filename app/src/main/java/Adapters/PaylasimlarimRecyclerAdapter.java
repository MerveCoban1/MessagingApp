package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetuygulamasi.PaylasimlarimActivity;
import com.example.sohbetuygulamasi.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PaylasimlarimRecyclerAdapter extends RecyclerView.Adapter<PaylasimlarimRecyclerAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private ArrayList<String> userYorumList;
    private ArrayList<String> userResimList;
    private ArrayList<String> gonderiIdList;

    List<DocumentSnapshot> documentSnapshots;

    FirebaseFirestore firebaseFirestore;

    Context context;
    Activity activity;

    public PaylasimlarimRecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> userYorumList, ArrayList<String> userResimList, ArrayList<String> gonderiIdList, Context context,Activity activity) {
        this.userEmailList = userEmailList;
        this.userYorumList = userYorumList;
        this.userResimList = userResimList;
        this.gonderiIdList = gonderiIdList;

        this.context = context;
        this.activity = activity;

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override//holder oluşturulunca ne yapacağımızı yazıyoruz
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_adapter_layout_paylasimlarim, parent, false);


        return new PostHolder(view);
    }

    @Override//bi holdera bağlanınca ne yapacağımızı yazıyoruz.
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {

        holder.userEmailText.setText(userEmailList.get(position));
        holder.commentText.setText(userYorumList.get(position));
        Picasso.get().load(userResimList.get(position)).into(holder.imageView);
        final String gonderiNo = gonderiIdList.get(position);

        holder.silPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Posts").document(gonderiNo).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Post Silindi", Toast.LENGTH_LONG).show();
                        Intent intento=new Intent(context, PaylasimlarimActivity.class);
                        context.startActivity(intento);
                        activity.finish();

                    }
                });
            }
        });

    }

    @Override//recyclerView'ımızda kaç tane row olacak onu yazıyoruz
    public int getItemCount() {
        return userEmailList.size();
    }

    //viewları tanımlıyoruz
    class PostHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView userEmailText, commentText;
        Button silPostButton;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rViewResimImageView);
            userEmailText = itemView.findViewById(R.id.rViewKullaniciAdiTextView);
            commentText = itemView.findViewById(R.id.rViewYorumTextView);
            silPostButton = itemView.findViewById(R.id.silPostButton);


        }
    }
}
