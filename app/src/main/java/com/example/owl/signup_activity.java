package com.example.owl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class signup_activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView reguserimage;
    EditText regusername, regemail, regpassword, regconfirmpass;
    Button signupbtn;
    ProgressBar regprogressbar;
    static int REQCODE = 1;
    static int pREQCODE = 1;
    Uri pickimageuri;
    Boolean ok;

    DatabaseReference imagefilepath;
    StorageReference storageReference;



    public void showmessage(String messagetext) {
        Toast.makeText(getApplicationContext(), messagetext, Toast.LENGTH_SHORT).show();
    }
    private void gohome() {
        Intent intent=new Intent(getApplicationContext(),HOME.class);
        startActivity(intent);
        finish();
    }
    //SIGN UP START//

    private void update_user_info(final String username, final Uri pickimageuri, final FirebaseUser currentUser) {
       StorageReference mstorage= FirebaseStorage.getInstance().getReference().child("users_Photos");
        final StorageReference imagefilePath=mstorage.child(pickimageuri.getLastPathSegment());
        imagefilePath.putFile(pickimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                //image upload successfully
                imagefilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //uri content user image
                        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                         //user info upgrade successfull
                                            showmessage("Registration Completed");
                                            gohome();
                                        }
                    }
                                });

                    }
                });
            }
        });


    }

    //update userinfo without photo
    private void update_user_infowithout_photo(final String username, final FirebaseUser currentUser) {

                        UserProfileChangeRequest profileUpdate=new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //user info upgrade successfull
                                            showmessage("Registration Completed");
                                            gohome();
                                        }
                                    }
                                });

                    }


    //SIGN UP END//

    public void signup(View view) {
        mAuth = FirebaseAuth.getInstance();
        String email, pass, confirm;
        final String username = regusername.getText().toString().trim();
        email = regemail.getText().toString().trim();
        pass = regpassword.getText().toString().trim();
        confirm = regconfirmpass.getText().toString().trim();
        ok=true;
        //validation
     if (username.isEmpty()) {
            regusername.setError("Email Should not Empty");
            regusername.requestFocus();
            ok = false;
            return;
        }
        if (email.isEmpty()) {
            regemail.setError("Email Should not Empty");
            regemail.requestFocus();
            ok = false;
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regemail.setError("Enter a valid Email address");
            regemail.requestFocus();
            ok = false;
            return;
        }
        if (pass.isEmpty()) {
            regpassword.setError("Password Should not Empty");
            regpassword.requestFocus();
            ok = false;
            return;
        }
        if (pass.length() < 8) {
            regpassword.setError("Password need morethan 8");
            regpassword.requestFocus();
            ok = false;
            return;
        }
        if (confirm.isEmpty()) {
            regconfirmpass.setError("Retype your Password");
            regconfirmpass.requestFocus();
            ok = false;
            return;
        }
        if (!pass.equals(confirm)) {
            regconfirmpass.setError("Password doesn't match!");
            regconfirmpass.requestFocus();
            ok = false;
            return;

        }

        //validation end
        if (ok) {
            signupbtn.setVisibility(View.INVISIBLE);
            regprogressbar.setVisibility(View.VISIBLE);

        }
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {

                             //Account create Successful
                             showmessage("Account Created");
                             //update user info
                             if(pickimageuri!=null)
                              update_user_info(username, pickimageuri, mAuth.getCurrentUser());
                             else
                                 update_user_infowithout_photo(username,mAuth.getCurrentUser());


                         } else {
                             regprogressbar.setVisibility(View.INVISIBLE);
                             signupbtn.setVisibility(View.VISIBLE);
                             if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                 showmessage("Already registered");
                             else
                                 showmessage("Error:"+task.getException().getMessage());
                         }
                     }
                 }

                );

    }



    private void opengallary() {
        Intent gallaryintent=new Intent(Intent.ACTION_GET_CONTENT);
        gallaryintent.setType("image/*");
        startActivityForResult(gallaryintent,REQCODE);



    }

    private void checkandrepermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(signup_activity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {

                ActivityCompat.requestPermissions(signup_activity.this,
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


//setting radius

            reguserimage.setImageURI(pickimageuri);

        }
    }



    public void addimage(View view)
    {
        if(Build.VERSION.SDK_INT>=22)
        {
            checkandrepermission();
        }
        else
        {
           opengallary();

        }
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reguserimage = findViewById(R.id.reguserimage);
        regusername = findViewById(R.id.reguserid);
        regemail = findViewById(R.id.regemailid);
        regpassword = findViewById(R.id.regpassid);
        regconfirmpass = findViewById(R.id.confirmpassid);
        signupbtn = findViewById(R.id.signupbtn);
        regprogressbar = findViewById(R.id.logprogress);



    }
}