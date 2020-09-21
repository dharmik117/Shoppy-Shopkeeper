package com.example.shopkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ViewProfile extends AppCompatActivity {

    private TextView userprofname,userphone,useremail,uid;

    private DatabaseReference profileuserref;
    private FirebaseAuth mauth;
    private String currentuserid;
    private FloatingActionButton editbutton;
    private FirebaseUser firebaseUser;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        profileuserref = FirebaseDatabase.getInstance().getReference().child("ShopKeeperData").child(currentuserid);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userprofname =(TextView) findViewById(R.id.myusername);
        userphone =(TextView) findViewById(R.id.myphoneno);
        useremail = (TextView) findViewById(R.id.myemailid);
        uid =(TextView)findViewById(R.id.myuserid) ;
        editbutton = (FloatingActionButton)findViewById(R.id.floatingeditbutton);


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfile.this,Edit_Profile.class);
                startActivity(intent);
            }
        });

        profileuserref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shopname = dataSnapshot.child("shopname").getValue().toString().trim();
                    String myphone = dataSnapshot.child("phoneno").getValue().toString().trim();
                    String myemail = dataSnapshot.child("emailid").getValue().toString().trim();
                    String myuid = dataSnapshot.child("userid").getValue().toString().trim();

                    userprofname.setText(shopname);
                    userphone.setText(myphone);
                    useremail.setText(myemail);
                    uid.setText(myuid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
