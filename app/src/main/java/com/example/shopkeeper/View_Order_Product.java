package com.example.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.View_All_Product;
import View_Holders.View_Ap_View_Holder;

public class View_Order_Product extends AppCompatActivity {

    private DatabaseReference productref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth firebaseauth;
    private String currentuserid;
    private FirebaseUser firebaseUser;
    private DatabaseReference profileuserref,uidref;
    private String useridp = "";
    private String shopid ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__order__product);

        recyclerView = findViewById(R.id.aprecyclerview);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseauth = FirebaseAuth.getInstance();
        currentuserid = firebaseauth.getCurrentUser().getUid();
        profileuserref = FirebaseDatabase.getInstance().getReference().child("User").child(currentuserid);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        useridp = getIntent().getStringExtra("uidp");
        shopid = getIntent().getStringExtra("shopid");

        Toast.makeText(this, "" + shopid, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        productref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child("Products");

        FirebaseRecyclerOptions<View_All_Product> options =
                new FirebaseRecyclerOptions.Builder<View_All_Product>()
                        .setQuery(productref.child(useridp)
                                ,View_All_Product.class)
                        .build();

        FirebaseRecyclerAdapter<View_All_Product, View_Ap_View_Holder> adapter =
                new FirebaseRecyclerAdapter<View_All_Product, View_Ap_View_Holder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull View_Ap_View_Holder view_ap_view_holder, int i, @NonNull final View_All_Product view_all_product)
                    {
                        view_ap_view_holder.textviewpname.setText(view_all_product.getPname());
                        view_ap_view_holder.textviewquentity.setText(view_all_product.getPrice());

                        view_ap_view_holder.rl1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(View_Order_Product.this,Change_Ready_Status.class);
                                intent.putExtra("uidp1",view_all_product.getUserid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public View_Ap_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_design,parent,false);
                        View_Ap_View_Holder holder = new View_Ap_View_Holder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}