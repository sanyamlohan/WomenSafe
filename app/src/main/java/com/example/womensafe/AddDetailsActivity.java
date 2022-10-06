package com.example.womensafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddDetailsActivity extends AppCompatActivity {
    EditText mContact1, mContact2, mContact3, mContact4, mContact5, mUsername;
    Button mAddDetailsBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar mProgressBar;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        mUsername = findViewById(R.id.usernameEdit);
        mContact1 = findViewById(R.id.contact1Edit);
        mContact2 = findViewById(R.id.contact2Edit);
        mContact3 = findViewById(R.id.contact3Edit);
        mContact4 = findViewById(R.id.contact4Edit);
        mContact5 = findViewById(R.id.contact5Edit);

        mAddDetailsBtn = findViewById(R.id.AddDetails);
        mProgressBar = findViewById(R.id.progressBar4);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Details");

        mAddDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                String Username = mUsername.getText().toString();
                String ContactNo1 = mContact1.getText().toString();
                String ContactNo2 = mContact2.getText().toString();
                String ContactNo3 = mContact3.getText().toString();
                String ContactNo4 = mContact4.getText().toString();
                String ContactNo5 = mContact5.getText().toString();
                userID = Username;

                // Now, we are getting all these data from the edit text .
                // so, now we have to add this data in our firebase database.
                // so, for that we have to create a model class inside which we will be adding all these
                // variables and will be passing the variable for that model class inside the firebase database.

                // so for that we have to create a model

                WomenRVModal womenRVModal = new WomenRVModal(Username, ContactNo1, ContactNo2, ContactNo3, ContactNo4, ContactNo5, userID);

                // Now, after this we will be adding this modal to our database
                // for that firstly we have to call database reference

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(AddDetailsActivity.this, "start uploading", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        // inside onDataChange method we have to set this data
                        databaseReference.child(userID).setValue(womenRVModal);
                        // so, this will add all the data

                        Toast.makeText(AddDetailsActivity.this, "Data Loaded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddDetailsActivity.this, MainActivity.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // we display the error which we get caught while adding the data
                        Toast.makeText(AddDetailsActivity.this, "Error occured" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}