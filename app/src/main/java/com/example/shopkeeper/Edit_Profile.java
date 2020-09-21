package com.example.shopkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Edit_Profile extends AppCompatActivity {

    private EditText etshopname,etphone,etemail;
    private Button btnupdate;
    private String currentuserid;
    private FirebaseAuth mauth;
    private DatabaseReference settinguserref;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        settinguserref = FirebaseDatabase.getInstance().getReference().child("ShopKeeperData").child(currentuserid);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        etshopname = (EditText) findViewById(R.id.etshopname);
        etphone = (EditText) findViewById(R.id.etphone);
        etemail = (EditText) findViewById(R.id.etemail);

        btnupdate = (Button) findViewById(R.id.btnupdate);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateAccountInfo();
            }
        });


        settinguserref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shopname = dataSnapshot.child("shopname").getValue().toString().trim();
                    String myphone = dataSnapshot.child("phoneno").getValue().toString().trim();
                    String myemail = dataSnapshot.child("emailid").getValue().toString().trim();

                    etshopname.setText(shopname);
                    etphone.setText(myphone);
                    etemail.setText(myemail);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void ValidateAccountInfo()
    {
        String shopname = etshopname.getText().toString();
        String phone = etphone.getText().toString();
        String email = etemail.getText().toString();


            UpdateAccountInfo(shopname,phone,email);

    }

    private void UpdateAccountInfo(String shopname, String phone, String email)
    {
        HashMap userMap = new HashMap();
        userMap.put("shopname",shopname);
        userMap.put("phoneno",phone);
        userMap.put("emailid",email);

        settinguserref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(Edit_Profile.this,ViewProfile.class);
                    startActivity(intent);
                    Toast.makeText(Edit_Profile.this,"Account Updated Sucessfully...",Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        Toast.makeText(Edit_Profile.this,"Error...",Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }


}
