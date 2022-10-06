package com.example.womensafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button mUpdateDetailsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUpdateDetailsBtn = findViewById(R.id.UpdateDetails);
        mUpdateDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Taking you to edit details activity", Toast.LENGTH_SHORT).show();
                openActivity2();
            }
        });
    }

    public void Logout(View view){
        FirebaseAuth.getInstance().signOut();
        // signOut method will do the log out of the user.
        startActivity(new Intent(getApplicationContext(), Login.class));
        // then, i am sending the user to the login page.
        finish();
    }

    public void scream(View view){
        startActivity(new Intent(MainActivity.this, ScreamActivity.class));
    }

    public void openActivity2(){
        Intent intent = new Intent(this, EditDetailsActivity.class);
        startActivity(intent);
    }

}