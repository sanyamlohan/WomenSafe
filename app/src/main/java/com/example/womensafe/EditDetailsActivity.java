package com.example.womensafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditDetailsActivity extends AppCompatActivity {
    EditText mContact1, mContact2, mContact3, mContact4, mContact5, mUsername;
    Button mEditDetailsBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar mProgressBar;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        mUsername = findViewById(R.id.usernameEdit);
        mContact1 = findViewById(R.id.contact1Edit);
        mContact2 = findViewById(R.id.contact2Edit);
        mContact3 = findViewById(R.id.contact3Edit);
        mContact4 = findViewById(R.id.contact4Edit);
        mContact5 = findViewById(R.id.contact5Edit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userID = user.getUid();

        mEditDetailsBtn = findViewById(R.id.EditDetailsButton);
        mProgressBar = findViewById(R.id.progressBar4);

        firebaseDatabase = FirebaseDatabase.getInstance();

        mEditDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                String userName = mUsername.getText().toString();
                String contact1 = mContact1.getText().toString();
                String contact2 = mContact2.getText().toString();
                String contact3 = mContact3.getText().toString();
                String contact4 = mContact4.getText().toString();
                String contact5 = mContact5.getText().toString();

                updateData(userName, contact1, contact2, contact3, contact4, contact5);
            }
        });


    }

    private void updateData(String userName, String contact1, String contact2, String contact3, String contact4, String contact5) {
        // to update we need to create a Hash map
        HashMap<String, Object> Data = new HashMap<String, Object>();
        Data.put("contactNo1", contact1);
        Data.put("contactNo2", contact2);
        Data.put("contactNo3", contact3);
        Data.put("contactNo4", contact4);
        Data.put("contactNo5", contact5);

        databaseReference = FirebaseDatabase.getInstance().getReference("Details");
        Log.e("testing", "enter in update data");
        databaseReference.child(userID).updateChildren(Data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // so, we will get the result in the form of a task
                Log.e("testing", "enter in firebase");

                if(task.isSuccessful()){

                    mUsername.setText("");
                    mContact1.setText("");
                    mContact2.setText("");
                    mContact3.setText("");
                    mContact4.setText("");
                    mContact5.setText("");


                    Toast.makeText(EditDetailsActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditDetailsActivity.this, MainActivity.class));
                }
                else{
                    Toast.makeText(EditDetailsActivity.this, "failed to update", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // first argument is name of the field and second argument is data entered by the user.



    }
}