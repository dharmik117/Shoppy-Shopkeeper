package com.example.shopkeeper;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Change_Ready_Status extends AppCompatActivity {

    private Switch stateswitch;
    private String productID = "";
    private String Permission,Product_Ready;
    private DatabaseReference RequestsRef;
    private TextView dboypick,apready,notready;
    private String useridp = "";

    private FirebaseAuth firebaseauth;
    private String currentuserid;
    private FirebaseUser firebaseUser;
    private DatabaseReference profileuserref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__ready__status);

        stateswitch = findViewById(R.id.swreadystate);
        dboypick = findViewById(R.id.dboyready);
        apready = findViewById(R.id.apready);
        notready = findViewById(R.id.apnotready);

        firebaseauth = FirebaseAuth.getInstance();
        currentuserid = firebaseauth.getCurrentUser().getUid();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        productID = getIntent().getStringExtra("readystate");

        RequestsRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        Product_Ready = stateswitch.getTextOn().toString().trim();

        useridp = getIntent().getStringExtra("uidp1");
        Toast.makeText(Change_Ready_Status.this,"User" + useridp,Toast.LENGTH_LONG).show();

        stateswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    Product_Ready = stateswitch.getTextOn().toString();

                    HashMap Usermap = new HashMap();
                    Usermap.put("Product_Ready",Product_Ready);
                    RequestsRef.child(currentuserid).child(useridp).updateChildren(Usermap);

                }

                else
                {
                    Product_Ready = stateswitch.getTextOff().toString();

                    HashMap Usermap = new HashMap();
                    Usermap.put("Product_Ready",Product_Ready);
                    RequestsRef.child(currentuserid).child(useridp).updateChildren(Usermap);

                }

            }
        });

        RequestsRef.child(currentuserid).child(useridp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String Product_Ready = dataSnapshot.child("Product_Ready").getValue().toString();

                    if(Product_Ready.equals("Ready"))
                    {
                        stateswitch.setChecked(true);
                        stateswitch.setVisibility(View.INVISIBLE);
                        apready.setText("Our Deliver Boy Recive Product Soon");
                        notready.setVisibility(View.VISIBLE);

                    }

                    else
                    {
                        stateswitch.setChecked(false);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        notready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Product_Ready = stateswitch.getTextOff().toString();

                HashMap Usermap = new HashMap();
                Usermap.put("Product_Ready",Product_Ready);
                RequestsRef.child(currentuserid).child(useridp).updateChildren(Usermap);

                stateswitch.setVisibility(View.VISIBLE);
                apready.setText("All Products Are Ready???");
                notready.setVisibility(View.INVISIBLE);


            }
        });
    }
}