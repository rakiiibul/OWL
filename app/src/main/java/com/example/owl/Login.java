package com.example.owl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView signupimage;
    FirebaseUser currentUser;
    EditText logemail, logpassword;
    Button signinbtn;
    ProgressBar logprogressbar;
    Boolean ok;


    //Show messaege or dailog//
    public void showmessage(String messagetext) {
        Toast.makeText(getApplicationContext(), messagetext, Toast.LENGTH_SHORT).show();
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent homeintent = new Intent(getApplicationContext(), HOME.class);
        startActivity(homeintent);
        finish();
    }

    public void signin(View view) {
        mAuth = FirebaseAuth.getInstance();
        String email, pass;
        email = logemail.getText().toString().trim();
        pass = logpassword.getText().toString().trim();

        ok = true;
        //validation

        if (email.isEmpty()) {
            logemail.setError("Email Should not Empty");
            logemail.requestFocus();
            ok = false;
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            logemail.setError("Enter a valid Email address");
            logemail.requestFocus();
            ok = false;
            return;
        }
        if (pass.isEmpty()) {
            logpassword.setError("Password Should not Empty");
            logpassword.requestFocus();
            ok = false;
            return;
        }
        if (pass.length() < 8) {
            logpassword.setError("Password need more than 8");
            logpassword.requestFocus();
            ok = false;
            return;
        }


        //validation end
        if (ok) {
            signinbtn.setVisibility(View.INVISIBLE);
            logprogressbar.setVisibility(View.VISIBLE);

        }
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            showmessage("Welcome to OWL");
                            updateUI(user);
                        } else {
                            signinbtn.setVisibility(View.VISIBLE);
                            logprogressbar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            showmessage("signInWithEmail:failure" + task.getException());
                        }
                    }
                });
    }

    public void Signup(View view) {
        Intent signupintent = new Intent(Login.this, signup_activity.class);
        startActivity(signupintent);
        finish();


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signinbtn = findViewById(R.id.signinbtn);

        logemail = findViewById(R.id.logemailid);
        logpassword = findViewById(R.id.logpassid);
        signupimage = findViewById(R.id.signupimage);
        logprogressbar = findViewById(R.id.logprogress);





    }


}