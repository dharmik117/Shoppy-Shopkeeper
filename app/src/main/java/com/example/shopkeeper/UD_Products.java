package com.example.shopkeeper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class UD_Products extends AppCompatActivity {

    private EditText udname,uddes,udcat,udprice;
    private Button updatebtn,uddeletebtn;

    private DatabaseReference udref;

    private FirebaseAuth firebaseauth;
    private String currentuserid;
    private FirebaseUser firebaseUser;

    private String productid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud_products);

        udname = findViewById(R.id.udproductname);
        uddes = findViewById(R.id.udproductdescryption);
        udcat = findViewById(R.id.udproductcatgeroy);
        udprice = findViewById(R.id.udproductprice);

        updatebtn = findViewById(R.id.udupdatebtn);
        uddeletebtn = findViewById(R.id.uddeletebtn);

        udref = FirebaseDatabase.getInstance().getReference().child("Products List");
        productid = getIntent().getStringExtra("pid");

        firebaseauth = FirebaseAuth.getInstance();
        currentuserid = firebaseauth.getCurrentUser().getUid();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UpdateProduct();
            }
        });

        uddeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(UD_Products.this);
                builder.setTitle("Are You Sure Want To Delete This Product");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        udref.child(currentuserid).child(productid).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(UD_Products.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setCancelable(false);
                builder.show();


                udref.child(currentuserid).child(productid).removeValue();
            }
        });

        udref.child(currentuserid).child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shopname = dataSnapshot.child("productname").getValue().toString().trim();
                    String shopdes = dataSnapshot.child("descryption").getValue().toString().trim();
                    String shopcat = dataSnapshot.child("category").getValue().toString().trim();
                    String price = dataSnapshot.child("price").getValue().toString().trim();

                    udname.setText(shopname);
                    uddes.setText(shopdes);
                    udcat.setText(shopcat);
                    udprice.setText(price);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateProduct()
    {
        String upshopname = udname.getText().toString();
        String upshopdes = uddes.getText().toString();
        String upshopcat = udcat.getText().toString();
        String upprice =  udprice.getText().toString();

        UpdateproductInfo(upshopname,upshopdes,upshopcat,upprice);

    }

    private void UpdateproductInfo(String upshopname, String upshopdes, String upshopcat, String upprice)
    {
        HashMap userMap = new HashMap();
        userMap.put("productname",upshopname);
        userMap.put("descryption",upshopdes);
        userMap.put("category",upshopcat);
        userMap.put("price",upprice);

        udref.child(currentuserid).child(productid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(UD_Products.this,"Product Updated Sucessfully...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(UD_Products.this,"Error...",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    }
