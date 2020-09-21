package com.example.shopkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Setting extends AppCompatActivity {

    private CardView cvviewprofile,cveditprofile;
    private DatabaseReference pidref;
    private String pid;
    private TextView tryit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cvviewprofile = (CardView) findViewById(R.id.cvviewprofile);
        cveditprofile = (CardView) findViewById(R.id.cveditprofile);
        tryit = findViewById(R.id.tryit);


        cvviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Setting.this,ViewProfile.class);
                startActivity(intent);
            }
        });

        cveditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Setting.this,Edit_Profile.class);
                startActivity(intent);
            }
        });
    }
}
