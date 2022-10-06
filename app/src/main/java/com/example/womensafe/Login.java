package com.example.womensafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn, mforgotPassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.Password);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mCreateBtn = findViewById(R.id.NotRegistered);
        progressBar = findViewById(R.id.progressBar2);
        mforgotPassword = findViewById(R.id.forgotPassword);

        fAuth = FirebaseAuth.getInstance();

        // we are going to validate the users input once the user click on login.
        // and we are going to take the email and password to the firebase database
        // and if the information is correct we are going to let them to go the main activity.


        // So, first thing we are going to do to implement the onClickListener

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("password must have more than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Now we are going to authenticate the user
                // and for this we need to use this email and password

                //for this i am going to use this fAuth class object and signInWithEmailAndPassword method of firebase.

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // here, we need to check that the login is successful or not
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


        // whenever user click on forget password he can give his email and if that email exist in our firebase
        // authentication list then firebase will send him a email and with link and with that link
        // user can reset the password

        mforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inside this we write our code that will handle reset function

                // inside this onClick we need an Edit Text field so that the user can enter his email
                // so to do that i am gonna create an edit text field here

                // so, i am gonna say it the name as reset mail and i will be creating a new edit text field
                // and i am gonna get the context of current view

                EditText resetMail = new EditText(view.getContext());

                // now i am gonna integrate these edit text inside my alert dialog
                // so first I will be creating an alert dialog

                AlertDialog.Builder passwordReset = new AlertDialog.Builder(view.getContext());

                // now inside the alert dialog i can say the title and messages so that user will understand that what
                // he needs to do further to reset his password.

                passwordReset.setTitle("Reset Password");
                passwordReset.setMessage("Enter your email to receive reset link");

                // now i am gonna save my edit text to the alert dialog by using the set view

                passwordReset.setView(resetMail);

                // now i will handle the click button on alert dialog.
                // so our alert dialog will have two options:- yes or no

                // so, if user click on no will this exit the alert dialog and user go to the login screen

                // if user click on yes we will handle that and say take the email from the edit text a
                // and sent the reset email.

                passwordReset.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // inside this i will extract the email and sent reset link

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset email is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordReset.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the alert dialog
                    }
                });

                passwordReset.create().show();
            }
        });
    }
}