package com.example.owl;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.owl.Model.post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HOME extends AppCompatActivity {
    FirebaseUser currentuser;
    FirebaseAuth mAuth;
    //ini popup
    Dialog popadd;
    ImageView pop_userimage,create_btn,popimage;
    ProgressBar popprogressBar;
    EditText poptitle,popdescription;
    int REQCODE=2;
    int pREQCODE=2;




    private AppBarConfiguration mAppBarConfiguration;
    private Uri pickimageuri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_o_m_e);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();





        inipop();
        setuppopimageclick();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popadd.show();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,R.id.nav_profile,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);




        updatenavheader();
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,new logout()).commit();
    }

    private void setuppopimageclick() {
    }

    private void inipop() {
        popadd=new Dialog(this);
        popadd.setContentView(R.layout.pop_add);
        popadd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popadd.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popadd.getWindow().getAttributes().gravity= Gravity.TOP;
        pop_userimage=popadd.findViewById(R.id.userimage);
        create_btn=popadd.findViewById(R.id.create_btn);
        popprogressBar=popadd.findViewById(R.id.popprogressBar);
        poptitle=popadd.findViewById(R.id.titleid);
        popdescription=popadd.findViewById(R.id.descriptionid);
        popimage=popadd.findViewById(R.id.popimage);
        Glide.with(HOME.this).load(currentuser.getPhotoUrl()).into(pop_userimage);


        popimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open a image
                if(Build.VERSION.SDK_INT>=22)
                {
                    checkandrepermission();
                }
                else
                {
                    opengallary();

                }


            }
        });

       create_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               boolean ok=true;
               if(poptitle.getText().toString().isEmpty())
               {  poptitle.requestFocus();
               poptitle.setError("Write a title");
               return;}
               if(popdescription.getText().toString().isEmpty())
               {  popdescription.requestFocus();
                   popdescription.setError("Write a description");
                   return;}
               if(pickimageuri==null)
               {showmessage("add a photo");
                   return;}
               create_btn.setVisibility(View.INVISIBLE);
               popprogressBar.setVisibility(View.VISIBLE);


               //upload all to database
               StorageReference  storageReference= FirebaseStorage.getInstance().getReference();
               final StorageReference imagefilepath=storageReference.child(pickimageuri.getLastPathSegment());
               imagefilepath.putFile(pickimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       imagefilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                                String imagedownloadlink=uri.toString();
                                post post1=new post(currentuser.getDisplayName(),poptitle.getText().toString(),popdescription.getText().toString(),
                                        currentuser.getUid(),currentuser.getPhotoUrl().toString(),imagedownloadlink);
                                addpost(post1);






                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               //something wrong uploading picture;
                               showmessage("something is wrong when uploading image or post"+e.getMessage());
                               create_btn.setVisibility(View.VISIBLE);
                               popprogressBar.setVisibility(View.INVISIBLE);

                           }
                       });

                   }
               });





           }

       });



    }

    private void showmessage(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

    }

    private void addpost(post post1) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("POSTS").push();
        String key=myref.getKey();
        post1.setPostkey(key);

        ///add post data to firebase database
        myref.setValue(post1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showmessage("post added successfully");
                create_btn.setVisibility(View.VISIBLE);
                popprogressBar.setVisibility(View.INVISIBLE);
                popadd.dismiss();
                poptitle.setText(null);
                popdescription.setText(null);


            }
        });



    }

    //check permission for open gallary
    private void opengallary() {
        Intent gallaryintent=new Intent(Intent.ACTION_GET_CONTENT);
        gallaryintent.setType("image/*");
        startActivityForResult(gallaryintent,REQCODE);

    }

    private void checkandrepermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(HOME.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {

                ActivityCompat.requestPermissions(HOME.this,
                        new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        pREQCODE);
            }

        }
        else {
            opengallary();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==REQCODE && data!=null)
        {
            //the user successfully picked the image
            pickimageuri=data.getData();
            popimage.setImageURI(pickimageuri);

        }
    }
    //check permission for open gallary
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.





        getMenuInflater().inflate(R.menu.h_o_m_e, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    public void updatenavheader(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview=navigationView.getHeaderView(0);
        TextView navusername=headerview.findViewById(R.id.navusername_id);
        TextView navmail=headerview.findViewById(R.id.navemail_id);
        ImageView navuserimage=headerview.findViewById(R.id.navuserimage_id);
        navusername.setText(currentuser.getDisplayName());
        navmail.setText(currentuser.getEmail());
        if(currentuser.getPhotoUrl()!=null)
        {Glide.with(this).load(currentuser.getPhotoUrl()).into(navuserimage);}
        else
        {
            Glide.with(this).load(R.drawable.image_icon).into(navuserimage);

        }






    }

}
