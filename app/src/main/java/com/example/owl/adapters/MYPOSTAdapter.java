package com.example.owl.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.owl.Model.post;
import com.example.owl.R;
import com.example.owl.activity.post_detailact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MYPOSTAdapter extends RecyclerView.Adapter<MYPOSTAdapter.MyvideoHolder> {

    Context mcontext;
    List<post> mData;
    FirebaseAuth mauth;
    FirebaseUser firebaseUser;


    public MYPOSTAdapter(Context mcontext, List<post> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyvideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mypostitem= LayoutInflater.from(mcontext).inflate(R.layout.my_post_item,parent,false);
        return new MyvideoHolder(mypostitem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyvideoHolder holder, int position) {
        holder.tvtitle.setText(mData.get(position).getTitle());
        Glide.with(mcontext).load(mData.get(position).getPicture()).into(holder.imgpost);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyvideoHolder extends RecyclerView.ViewHolder {
        TextView tvtitle;
        Dialog popdelete;
        ImageView imgpost,imagepostprofile;
        public MyvideoHolder(@NonNull View itemView) {
            super(itemView);
            tvtitle=itemView.findViewById(R.id.my_post_title);
            imgpost=itemView.findViewById(R.id.my_post_imageid);
            imagepostprofile=itemView.findViewById(R.id.rowpost_user_image);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postdetailac=new Intent(mcontext, post_detailact.class);
                    int position=getAdapterPosition();
                    postdetailac.putExtra("title",mData.get(position).getTitle());
                    postdetailac.putExtra("username",mData.get(position).getName());
                    postdetailac.putExtra("description",mData.get(position).getDescription());
                    postdetailac.putExtra("user_photo",mData.get(position).getUserphoto());
                    postdetailac.putExtra("post_image",mData.get(position).getPicture());
                    postdetailac.putExtra("post_key",mData.get(position).getPostkey());
                    // postdetailac.putExtra("user_name",mData.get(position).getCurrentUser());
                    long timestamp=(long)mData.get(position).getTimestamp();
                    postdetailac.putExtra("date",timestamp);
                    mcontext.startActivity(postdetailac);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    int position=getAdapterPosition();
                    String postid=mData.get(position).getPostkey();
                    DatabaseReference delpost=database.getReference("POSTS").child(postid);
                    DatabaseReference delpostcom=database.getReference("Coment").child(postid);
                    delpost.removeValue();
                    delpostcom.removeValue();


                    Toast.makeText(mcontext.getApplicationContext(),"Post deleted Successfully",Toast.LENGTH_LONG).show();
                    return false;
                }
            });





        }

    }
}
