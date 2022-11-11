package com.example.womensafe;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    FirebaseUser user;
    Button mUpdateDetailsBtn;
    String userID, address;
    double lati, longi;
    DatabaseReference databaseReference;
    DatabaseReference dbRef;
    WomenRVModal womenRVModal;
    int call = 0, message = 0;

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseLocationStuff();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userID = user.getUid();

        Button mCall = findViewById(R.id.call);
        Button mMsg = findViewById(R.id.msgBtn);

        mMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = 1;
                Toast.makeText(MainActivity.this, "messaging", LENGTH_SHORT).show();

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        takePhoneStatePemission();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 500);
                    }
                } else {
                    int requestCode;
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call = 1;
                Toast.makeText(MainActivity.this, "Calling.....", LENGTH_SHORT).show();
                takeReference();
            }
        });

        mUpdateDetailsBtn = findViewById(R.id.UpdateDetails);
        mUpdateDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Taking you to edit details activity", LENGTH_SHORT).show();
                openActivity2();
            }
        });
    }

    private void initialiseLocationStuff() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lastKnownLocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 500);
            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    updateLocation(lastKnownLocation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updateLocation(Location location) {

        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            lati = location.getLatitude();
            longi = location.getLongitude();

            List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
            address = addresses.get(0).getAddressLine(0);

            sendingSMS();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void takePhoneStatePemission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 5);
        } else {
            takeReference();
        }
    }


    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        // signOut method will do the log out of the user.
        startActivity(new Intent(getApplicationContext(), Login.class));
        // then, i am sending the user to the login page.
        finish();
    }

    public void scream(View view) {
        startActivity(new Intent(MainActivity.this, ScreamActivity.class));

    }

    public void openActivity2() {
        Intent intent = new Intent(this, EditDetailsActivity.class);
        startActivity(intent);
    }

    private void takeReference() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Details");
        Toast.makeText(this, userID + "check if this is working", LENGTH_SHORT).show();

        Toast.makeText(this, databaseReference + "", LENGTH_SHORT).show();
        Log.e("testing", "dbreference" + userID);

        fetchdata();

    }

    private void dialCall() {
        String phone;
        if (womenRVModal != null) {
            phone = womenRVModal.contactNo1;
        } else {
            phone = "987";
        }
        Toast.makeText(MainActivity.this, phone, LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + phone;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void fetchdata() {
        Log.e("testing", "fetch data calles");
        dbRef = databaseReference.child(userID);
        Log.e("inside fetck data", dbRef + "");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("testing", "on data change called");
                Toast.makeText(MainActivity.this, "snapsot " + snapshot, LENGTH_SHORT).show();

                WomenRVModal model = snapshot.getValue(WomenRVModal.class);
                womenRVModal = model;
                Log.e("testing", "inside datasnapshot with model as " + womenRVModal.toString());

                if (call == 1) {
                    call = 0;
                    dialCall();
                } else if (message == 1) {
                    message = 0;
                    sendMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Got error as value", LENGTH_SHORT).show();
            }
        });
    }


    private void sendMessage() {
        updateLocation(lastKnownLocation);
    }

    private void sendingSMS() {

        try {
            String phone1;
            if (womenRVModal != null) {
                phone1 = womenRVModal.contactNo1;
            } else {
                phone1 = "987";
            }

            if (womenRVModal != null) {

                String phone2 = womenRVModal.getContactNo2();
                String phone3 = womenRVModal.getContactNo3();
                String phone4 = womenRVModal.getContactNo4();
                String phone5 = womenRVModal.getContactNo5();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone1, null, "help pls " + address, null, null);
                smsManager.sendTextMessage(phone2, null, "help pls " + address, null, null);
                smsManager.sendTextMessage(phone3, null, "help pls " + address, null, null);
                smsManager.sendTextMessage(phone4, null, "help pls " + address, null, null);
                smsManager.sendTextMessage(phone5, null, "help pls " + address, null, null);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dialCall();
            } else {
                Toast.makeText(this, "Permission Denied", LENGTH_SHORT).show();
            }
        } else if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoneStatePemission();
            } else {
                Toast.makeText(this, "Permission Denied for request code 100", LENGTH_SHORT).show();
            }
        } else if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeReference();
            } else {
                Toast.makeText(this, "Permission Denied for request code 5", LENGTH_SHORT).show();
            }
        } else if (requestCode == 500) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation != null) {
                        updateLocation(lastKnownLocation);
                    }
                    takePhoneStatePemission();
                } else {
                    Toast.makeText(this, "Permission Denied for request code 500", LENGTH_SHORT).show();
                }
            }

        }

    }
}
