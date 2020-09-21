package com.example.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Board extends AppCompatActivity
{
    private ImageView settingbutton;
    private Button btnlogout,btnaddshop,btnaddproduct,btnviewallorder,btnmanageproducts;
    private FirebaseAuth mauth;
    private FloatingActionButton editbutton;
    private DatabaseReference pidref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mauth = FirebaseAuth.getInstance();

        settingbutton = (ImageView) findViewById(R.id.setting);
        btnlogout=(Button) findViewById(R.id.logout);
        btnaddproduct = (Button) findViewById(R.id.btnaddproduct);
        btnaddshop = (Button) findViewById(R.id.btnaddshop);
        btnviewallorder = (Button) findViewById(R.id.btnvieworder);
        btnmanageproducts = findViewById(R.id.btnmanageproduct);

        pidref = FirebaseDatabase.getInstance().getReference().child("Products List");

        mauth = FirebaseAuth.getInstance();

        btnmanageproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Board.this,Edit_Product.class);
                startActivity(intent);
            }
        });


        settingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Board.this,Setting.class);
                startActivity(intent);
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mauth.signOut();
                finish();
                startActivity(new Intent(Board.this,MainActivity.class));
            }
        });

        btnaddshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Board.this,Add_Shop.class);
                startActivity(intent);
            }
        });

        btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Board.this,Add_Product.class);
                startActivity(intent);
            }
        });

        btnviewallorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Board.this,View_Orders.class);
                startActivity(intent);
            }
        });

    }
}
