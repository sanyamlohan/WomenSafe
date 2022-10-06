package com.example.womensafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;   //FirebaseAuth class is provide by Firebase using which we can register the user.
    // So, that's why we need instance of that class.

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.Password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.RegisterBtn);
        mLoginBtn = findViewById(R.id.alreadyRegistered);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        // Here, we are getting the current instance of the database from the Firebase.
        // So, that we can perform the various operation on the database.

        // if user is already logged in or he has already created the account before
        // then we need to send them to the main activity

        if(fAuth.getCurrentUser() != null){
            // we get the current user object if current user object is present i.e., user is already logged in
            // we will just simply send him to the main activity.
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // User fill the data in the edit text and we are going to let the user to click on the register button.
        // Once the register button is clicked we are going to validate the data
        // Ex:- Email is entered properly or not, password is not less than 6 characters or not
        // So, to do the validation we are going to need to handle the register click event.

        // when register button is clicked we are going to set the on click listener

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // And inside the onClick we are going to perform all the operation

                // First thing I'm gonna do here is I am going to get the values
                // that has been entered in email and password field. Both are string

                String email = mEmail.getText().toString().trim();
                // firstly mEmail is an object that's why we have to convert it into string and same for password
                String password = mPassword.getText().toString().trim();

                // So, now we need to validate the data that has been saved in email and password.


                // So, first thing we need to check that if the value is entered or not
                // if it is null or the email field is empty then we are not going to let the user register the account.

                // so, to check if it is empty or not we need to use one more class TextUtils

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must have more than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // if all these conditions are passed that means data are valid
                // and we can actually start registering the user.




                // register the user in firebase.
                // so, for that we need to use firebase object instance and
                // we need to use create user with email and password method.
                // so, here we need to pass two things first is username or email and second is password.
                // Now, we need to add some event listener so that we get to know that
                // if registration is successful or not
                // for that we need to use addOnCompleteListener method and inside this we need to create new OnCompleteListener
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // and here we will check if the registration is successful or not.

                        // process of registering the user is called as task here.
                        // so, if the task is successful means we have successfully created the user
                        // if not then we have encountered some error and we can display that error to the user.
                        // if account is created then i am gonna display user is created and redirect the user to main activity.
                        // so, we can use if-else statement here

                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();

                            // now we redirect the user to the main activity.
                            // To do that we need to start the new activity, inside this we need to add new intent from
                            // the current app context i am gonna send the user to the main activity like this :-

                            startActivity(new Intent(getApplicationContext(), AddDetailsActivity.class));
                        }
                        else{
                            // otherwise i will show the user that user isn't created
                            // and gonna attach the exception we get

                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

}