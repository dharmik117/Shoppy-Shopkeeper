package com.example.shopkeeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.customer;
import Prevalent.Prevalent;

public class Add_Product extends AppCompatActivity {

    private Button addnewproductbutton;
    private EditText inputproductname,inputproductdescryption,inputproductcat,inputproductprice;
    private TextView uid;
    private ImageView inputaddproductimage;
    private ProgressDialog loadingbar;
    private StorageReference productimagesref;
    private DatabaseReference productref;
    private String Pname,Pdescryption,Pcategory,Price;
    private String savecurrentdate,savecurrenttime,productrandomkey;
    private String downloadimageurl;
    private String Shopname;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    String TAG = "";

    private DatabaseReference profileuserref;
    private FirebaseAuth mauth;
    private String currentuserid;
    private FirebaseUser firebaseUser;
    private String myuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);

        addnewproductbutton = (Button) findViewById(R.id.btnaddproduct);
        inputproductname = (EditText) findViewById(R.id.productname);
        inputproductdescryption = (EditText) findViewById(R.id.productdescryption);
        inputproductcat = (EditText) findViewById(R.id.productcatgeroy);
        inputproductprice = (EditText) findViewById(R.id.productprice);

        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        profileuserref = FirebaseDatabase.getInstance().getReference().child("ShopKeeperData").child(currentuserid);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = new Intent(String.valueOf(Add_Product.this));
        intent.putExtra("pid",productrandomkey);

        profileuserref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    myuid = dataSnapshot.child("userid").getValue().toString().trim();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        inputaddproductimage = (ImageView) findViewById(R.id.addproductimagebutton);
        loadingbar = new ProgressDialog(this);

        productimagesref = FirebaseStorage.getInstance().getReference().child("Product Images");
        productref = FirebaseDatabase.getInstance().getReference().child("Products List");

        inputaddproductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        addnewproductbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });


    }

    private void OpenGallery()
    {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode==RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            inputaddproductimage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {
        Pname = inputproductname.getText().toString();
        Pcategory = inputproductcat.getText().toString();
        Pdescryption = inputproductdescryption.getText().toString();
        Price = inputproductprice.getText().toString();


        if(ImageUri == null)
        {
            Toast.makeText(Add_Product.this,"Please Choose Image....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(Add_Product.this,"Please Write Product Name....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Pcategory))
        {
            Toast.makeText(Add_Product.this,"Please Write Product Category....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Pdescryption))
        {
            Toast.makeText(Add_Product.this,"Please Write Product Shop Descryption....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(Add_Product.this,"Please Write Product Price",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loadingbar.setTitle("Adding New Shop");
        loadingbar.setMessage("Please Wait...Shop Is Adding");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("dd MM, yyyy");
        savecurrentdate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());

        productrandomkey = savecurrentdate + savecurrenttime;

        final StorageReference filepath = productimagesref.child(ImageUri + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                downloadimageurl= taskSnapshot.getUploadSessionUri().toString();

                SaveProductInfoToDatabase();

            }
        });

    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String,Object> productmap = new HashMap<>();
        productmap.put("pid",productrandomkey);
        productmap.put("date",savecurrentdate);
        productmap.put("time",savecurrenttime);
        productmap.put("image",downloadimageurl);
        productmap.put("descryption",Pdescryption);
        productmap.put("category",Pcategory);
        productmap.put("productname",Pname);
        productmap.put("price",Price);
        productmap.put("shopkeeper_id",currentuserid);

        productref.child(currentuserid)
                .child(productrandomkey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    loadingbar.dismiss();

                    Toast.makeText(Add_Product.this,"Shop Is Added Sucessfully",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingbar.dismiss();

                    String message = task.getException().toString();
                    Toast.makeText(Add_Product.this,"Error" + message,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
