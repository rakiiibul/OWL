package com.example.owl.ui.logout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.owl.Login;
import com.example.owl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class nav_logout extends Fragment {

    ImageView signout;


    public nav_logout() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View signoutview=inflater.inflate(R.layout.nav_logout, container, false);
        signout=signoutview.findViewById(R.id.signoutid);
        signout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirebaseAuth.getInstance().signOut();
               getActivity().finish();
                Intent logintent = new Intent(getContext(), Login.class);
                startActivity(logintent);
                return false;
            }



        });
        return signoutview;
    }
}
