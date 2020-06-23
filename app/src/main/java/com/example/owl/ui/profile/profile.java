package com.example.owl.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class profile extends Fragment {
    TextView p_email,p_phone,p_username,p_address;
    ImageView p_userimage;
    private FirebaseDatabase firebaseDatabase;
   private FirebaseAuth mAuth;
 private  FirebaseUser firebaseUser;




    public profile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
       firebaseUser=mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profilefrag=inflater.inflate(R.layout.fragment_profile, container, false);
        p_username=profilefrag.findViewById(R.id.p_user_nameid);
        p_email=profilefrag.findViewById(R.id.p_emailid);
        p_phone=profilefrag.findViewById(R.id.p_phoneid);
        p_address=profilefrag.findViewById(R.id.p_addressid);
        p_userimage=profilefrag.findViewById(R.id.p_user_imageid);


        p_username.setText(firebaseUser.getDisplayName());
        p_email.setText(firebaseUser.getEmail());
        if(firebaseUser.getPhotoUrl()!=null)
        {Glide.with(getContext()).load(firebaseUser.getPhotoUrl()).into(p_userimage);}
        else
        {p_userimage.setImageResource(R.drawable.ic_user3);}



            p_phone.setText(firebaseUser.getUid());







        return profilefrag;
    }
}
