package com.example.owl.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.owl.Model.post;
import com.example.owl.R;
import com.example.owl.activity.post_detailact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context mcontext;
    List<post> mData;
    FirebaseAuth mauth;
    FirebaseUser firebaseUser;
    public PostAdapter(Context mcontext, List<post> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(mcontext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvtitle.setText(mData.get(position).getTitle());
        Glide.with(mcontext).load(mData.get(position).getPicture()).into(holder.imgpost);
        Glide.with(mcontext).load(mData.get(position).getUserphoto()).into(holder.imagepostprofile);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvtitle;
        ImageView imgpost,imagepostprofile;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtitle=itemView.findViewById(R.id.row_post_username);
            imgpost=itemView.findViewById(R.id.row_post_imageid);
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

        }
    }

}
