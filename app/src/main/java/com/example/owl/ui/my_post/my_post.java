package com.example.owl.ui.my_post;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owl.Model.post;
import com.example.owl.R;
import com.example.owl.adapters.MYPOSTAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class my_post extends Fragment {

  RecyclerView mypostrv;
  GridLayoutManager mypost_gridLayoutManager;
    MYPOSTAdapter postAdapter;
    FirebaseAuth mAuth;
    FirebaseUser currentuseer;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<post> postList;
    public my_post() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static my_post newInstance(String param1, String param2) {
        my_post fragment = new my_post();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mypostfrag= inflater.inflate(R.layout.fragment_my_post, container, false);
         mypostrv = mypostfrag.findViewById(R.id.my_postrv);
// set a GridLayoutManager with default vertical orientation and 3 number of columns
        mypost_gridLayoutManager = new GridLayoutManager(getActivity(),2);
        mypostrv.setLayoutManager(mypost_gridLayoutManager);
        mAuth=FirebaseAuth.getInstance();
        currentuseer=mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("POSTS");
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mypostrv);
        return mypostfrag;
    }

    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    post posts = postsnap.getValue(post.class);
                    if(posts.getUserid().equals(currentuseer.getUid()))
                    postList.add(posts);

                }
                postAdapter = new MYPOSTAdapter(getActivity(), postList);
                mypostrv.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
