package com.example.dell.tourassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.tourassistant.EventPackage.EventActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText userEmail;
    EditText userPass;
    Button login;

    FirebaseAuth auth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(LoginActivity.this,EventActivity.class);
            startActivity(intent);
            finish();
        }

        userEmail = (EditText) findViewById(R.id.emailET);
        userPass = (EditText) findViewById(R.id.passwordET);

        userEmail.setText("");
        userPass.setText("");


        login = (Button) findViewById(R.id.loginBt);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {





                final String email = userEmail.getText().toString();
                final String pass = userPass.getText().toString();
                boolean value = true;

                if (email.isEmpty() || pass.length()<6 ) {
                    value = false;
                    userEmail.setError("Please enter your Email");
                    userPass.setError("Password must be 6 number");
                }


                if (value == true){
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("Connecting...");
                    dialog.show();
                    login.setEnabled(false);
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    userPass.setError("error_weak_password");
                                    userPass.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    userEmail.setError("error_invalid_email");
                                    userEmail.requestFocus();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    userEmail.setError("error_user_exists");
                                    userEmail.requestFocus();
                                } catch (IllegalAccessException e){

                                    userEmail.setError("Please enter your email");
                                    userEmail.requestFocus();
                                }


                                catch(Exception e) {
                                    Log.e("error", e.getMessage());
                                }
                            }
                            FirebaseUser user = auth.getCurrentUser();
//                            Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                            if (user != null) {
                                //Toast.makeText(LoginActivity.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Login Seccessfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,EventActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                            else {
                                login.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                }

            }
        });


    }


    public void noAccount(View view) {

        Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    }
}

