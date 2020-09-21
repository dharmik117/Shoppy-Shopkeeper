package com.example.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.squareup.picasso.Picasso;

import Model.Products;
import View_Holders.EP_View_Holder;


public class Edit_Product extends AppCompatActivity {

    private DatabaseReference productref;
    private RecyclerView recyclerView;
    RecyclerView .LayoutManager layoutManager;
    private String shopkeeperid;

    private FirebaseAuth firebaseauth;
    private String currentuserid;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productref = FirebaseDatabase.getInstance().getReference().child("Products List");
        recyclerView = findViewById(R.id.eprecycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseauth = FirebaseAuth.getInstance();
        currentuserid = firebaseauth.getCurrentUser().getUid();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new
                FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productref.child(currentuserid),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, EP_View_Holder> adapter =
                new FirebaseRecyclerAdapter<Products, EP_View_Holder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EP_View_Holder onClick_viewHolder, int i, @NonNull final Products products)
                    {
                        onClick_viewHolder.txtproductname.setText(products.getProductname());
                        onClick_viewHolder.txtproductdes.setText(products.getDescryption());
                        onClick_viewHolder.txtproductprice.setText(products.getPrice());

                        Picasso.get().load(products.getImage()).into(onClick_viewHolder.pdimageview);


                        onClick_viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(Edit_Product.this,UD_Products.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);


                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EP_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ep_layout,parent,false);
                        EP_View_Holder holder = new EP_View_Holder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    }
