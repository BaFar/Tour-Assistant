package com.example.dell.tourassistant;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignupActivity extends AppCompatActivity  implements PermissionUtil.PermissionAskListener{

    private EditText username;
    private EditText useremail;
    private EditText userpass;
    private Button submit;
    private FirebaseAuth auth;
    ProgressDialog dialog;
    private boolean isInternetPermissionAvailable = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //username = (EditText) findViewById(R.id.usernameET);
        useremail = (EditText) findViewById(R.id.useremailET);
        userpass = (EditText) findViewById(R.id.userpasswordET);
        submit = (Button) findViewById(R.id.submitBtn);

        auth = FirebaseAuth.getInstance();

        PermissionUtil.checkPermission(this,Manifest.permission.INTERNET,this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInternetPermissionAvailable){
                    Toast.makeText(SignupActivity.this, "You must allow internet permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ConnectivityReceiver.isConnected()){
                    Toast.makeText(SignupActivity.this, "Opps! No internet Connection. First check internet connection, please!", Toast.LENGTH_LONG).show();
                    return;
                }
                validition();
            }
        });

    }


    public void validition(){

        final String userEmail = useremail.getText().toString();
        String pass = userpass.getText().toString();

        boolean value = true;

        if (userEmail.isEmpty() || pass.length()<6 ){
            value = false;
            useremail.setError("Please enter your Email");
            userpass.setError("Password must be 6 number");
        }
        if(value == true ){
            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setMessage("Please wait...");
            dialog.show();

            auth.createUserWithEmailAndPassword(userEmail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){


                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            userpass.setError("error_weak_password");
                            userpass.requestFocus();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            useremail.setError("error_invalid_email");
                            useremail.requestFocus();
                        } catch(FirebaseAuthUserCollisionException e) {
                            useremail.setError("error_user_exists");
                            useremail.requestFocus();
                        } catch(Exception e) {
                            Log.e("error", e.getMessage());
                        }


                        Log.e("Signup Error", "onCancelled", task.getException());
                        dialog.dismiss();

                    }
                    else {
                        finish();

                        dialog.dismiss();
                        Toast.makeText(SignupActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    @Override
    public void onNeedPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},PermissionUtil.INTERNET_REQUEST_CODE);
        Log.d("permission","Internet Permission requested:SignUp Activity");
    }

    @Override
    public void onPermissionPreviouslyDenied() {

    }

    @Override
    public void onPermissionDisabled() {

    }

    @Override
    public void onPermissionGranted() {
        isInternetPermissionAvailable = true;
        Log.d("permission","Internet permission granted");


    }
}
