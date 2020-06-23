package com.example.owl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owl.Model.Coment;
import com.example.owl.Model.post;
import com.example.owl.R;
import com.example.owl.adapters.ComentAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class post_detailact extends AppCompatActivity {
    EditText post_comment;
    TextView post_title, post_description, post_date;
    ImageView post_detimage, post_userimage, com_current_userimage;
    Button add_combtn;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    String Postkey;
    RecyclerView comrecyclerView;
    ComentAdapter comentAdapter;
    List<Coment> comentList;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail_activity);
        post_title = findViewById(R.id.post_detail_titleid);
        post_description = findViewById(R.id.post_detail_descriptionid);
        post_detimage = findViewById(R.id.post_detail_imageid);

        post_userimage = findViewById(R.id.post_userimageid);
        post_date = findViewById(R.id.datepostid);

        com_current_userimage = findViewById(R.id.com_userimageid);
        post_comment = findViewById(R.id.com_editid);
        add_combtn = findViewById(R.id.com_addbtnid);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        comrecyclerView = findViewById(R.id.comentrv);
        firebaseDatabase = FirebaseDatabase.getInstance();


      String postimage = getIntent().getExtras().getString("post_image");
      String postusername = getIntent().getExtras().getString("username");
        String postuserimage = getIntent().getExtras().getString("user_photo");
        Glide.with(this).load(postuserimage).into(post_userimage);
        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        post_title.setText(title);
        post_description.setText(description);
        String date = getIntent().getExtras().getString("date");
        post_date.setText(date);
        Glide.with(this).load(postimage).into(post_detimage);
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(com_current_userimage);

        //get post key
        Postkey = getIntent().getExtras().getString("post_key");
        String postdate = timestamp(getIntent().getExtras().getLong("date"));

        post_date.setText(postdate + " by " + postusername);
        ini_rvcomment();


        add_combtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment;
                String uid,uimage,uname;
                uid=firebaseUser.getUid();
                uname=firebaseUser.getDisplayName();
                uimage=firebaseUser.getPhotoUrl().toString();
                comment=post_comment.getText().toString();
                firebaseDatabase=FirebaseDatabase.getInstance();

                DatabaseReference comentref=firebaseDatabase.getReference("Coment").child(Postkey).push();

                if(comment.isEmpty())
                {
                    post_comment.setError("Nothing to Add");
                    post_comment.requestFocus();
                    return;
                }
                add_combtn.setVisibility(View.INVISIBLE);

                Coment newcoment=new Coment(comment,uname,uid,uimage);

                comentref.setValue(newcoment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        add_combtn.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Succeesfully added",Toast.LENGTH_SHORT).show();
                        post_comment.setText(null);

                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error : "+e,Toast.LENGTH_SHORT).show();
                        add_combtn.setVisibility(View.VISIBLE);
                    }
                });

            }
        });





    }

    private void ini_rvcomment() {
        databaseReference = firebaseDatabase.getReference("Coment").child(Postkey);
        comrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comentList = new ArrayList<>();
                for (DataSnapshot comsnap : dataSnapshot.getChildren()) {
                    Coment coment1 = comsnap.getValue(Coment.class);
                    comentList.add(coment1);

                }
                comentAdapter = new ComentAdapter(getApplicationContext(), comentList);
                comrecyclerView.setAdapter(comentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String timestamp(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;

    }










}
