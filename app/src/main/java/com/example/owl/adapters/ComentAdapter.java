package com.example.owl.adapters;

import android.content.Context;
import android.icu.text.TimeZoneFormat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.owl.Model.Coment;
import com.example.owl.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ComentAdapter extends RecyclerView.Adapter<ComentAdapter.MyViewHolder> {

    private Context context;
    List<Coment> mcoment;
    private Object LayoutInflater;

    public ComentAdapter(Context context, List<Coment> mcoment) {
        this.context = context;
        this.mcoment = mcoment;
    }







    @NonNull
    @Override
    public ComentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= android.view.LayoutInflater.from(context).inflate(R.layout.coment_item,parent,false);
        return new MyViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull ComentAdapter.MyViewHolder holder, int position) {
        holder.com_username.setText(mcoment.get(position).getUsername());
        holder.rowcoment.setText(mcoment.get(position).getContent());
        Glide.with(context).load(mcoment.get(position).getUserimage()).into(holder.userimage);


        holder.com_time.setText(timestamp((long)mcoment.get(position).getTimestamp()));

    }
    private String timestamp(long time) {
        Calendar calender = Calendar.getInstance(Locale.ENGLISH);
        String date;

        calender.setTimeInMillis(time);
        date = DateFormat.format(" hh:mm a   dd-MM-yyyy ", calender).toString();

        return date;

    }

    @Override
    public int getItemCount() {
        return mcoment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rowcoment,com_time,com_username;
        ImageView userimage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowcoment=itemView.findViewById(R.id.com_conid);
            com_time=itemView.findViewById(R.id.com_timeid);
            com_username=itemView.findViewById(R.id.com_nameid);
            userimage=itemView.findViewById(R.id.com_row_userimageid);

        }
    }

}
