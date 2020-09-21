package com.example.shopkeeper;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.Image;

public class Add_Shop extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button addnewshopbutton,btnlogout;
    private EditText inputshopname,inputshopdescryption,inputshopcategory;
    private ImageView inputshopimage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String shopname,shopdescryption,shopcategory,sname,savecurrentdate,savecurrenttime,permissionswitch;
    private String shoprandomkey,downloadimageurl,currentuserid;
    private Task<Uri>  imageurl;
    private StorageReference productimagesref;
    private DatabaseReference shopsref,shoplist;
    private ProgressDialog loadingbar;
    private Switch pswitch;
    private StorageTask mUploadTask;
    String Permission = "";
    private FirebaseUser firebaseUser;
    private FirebaseAuth mauth;
    String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__shop);

        addnewshopbutton = (Button) findViewById(R.id.btnaddshop);
        inputshopimage = (ImageView) findViewById(R.id.addshopimagebutton);
        inputshopname = (EditText) findViewById(R.id.shopname);
        inputshopdescryption = (EditText) findViewById(R.id.shopdescryption);
        inputshopcategory = (EditText) findViewById(R.id.shopcatgeroy);
        pswitch = (Switch) findViewById(R.id.permissionswitch);
        loadingbar = new ProgressDialog(this);

        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        productimagesref = FirebaseStorage.getInstance().getReference().child("Shop Images");
        shopsref = FirebaseDatabase.getInstance().getReference().child("Add_Shop_From_ShopKeeper");
        shoplist = FirebaseDatabase.getInstance().getReference().child("Shop_List");

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        inputshopimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });
        addnewshopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateShopData();
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
            inputshopimage.setImageURI(ImageUri);
        }
    }

    private void ValidateShopData()
    {
        shopdescryption = inputshopdescryption.getText().toString();
        shopcategory = inputshopcategory.getText().toString();
        sname = inputshopname.getText().toString();
        permissionswitch = pswitch.getText().toString();


        if (pswitch.isChecked()){

            Permission="True";
        }

        else
        {
            Permission = "False";
        }



        if(ImageUri == null)
        {
            Toast.makeText(Add_Shop.this,"Please Choose Image....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(shopdescryption))
        {
            Toast.makeText(Add_Shop.this,"Please Write Product Descryption....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(shopcategory))
        {
            Toast.makeText(Add_Shop.this,"Please Write Product Category....",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(sname))
        {
            Toast.makeText(Add_Shop.this,"Please Write Product Shop Name....",Toast.LENGTH_SHORT).show();
        }

        else
        {
            StoreShopInformation();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void StoreShopInformation()
    {
        loadingbar.setTitle("Adding New Shop");
        loadingbar.setMessage("Please Wait...Shop Is Adding");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());

        shoprandomkey = savecurrentdate + savecurrenttime;

        final StorageReference filepath = productimagesref.child(System.currentTimeMillis() + "." + getFileExtension(ImageUri));
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                downloadimageurl= taskSnapshot.getUploadSessionUri().toString();

                Image image = new Image(taskSnapshot.getUploadSessionUri().toString());
                String uploadid = shopsref.push().getKey();
                shopsref.child(uploadid);

                SaveShopInfoToDatabase();

            }
        });

    }



    private void SaveShopInfoToDatabase()
    {
        HashMap<String,Object> productmap = new HashMap<>();
        productmap.put("shopid",shoprandomkey);
        productmap.put("date",savecurrentdate);
        productmap.put("time",savecurrenttime);
        productmap.put("image",downloadimageurl);
        productmap.put("descryption",shopdescryption);
        productmap.put("category",shopcategory);
        productmap.put("shopname",sname);
        productmap.put("permission",Permission);
        productmap.put("shopkeeperid",currentuserid);

        shoplist.child(shoprandomkey).updateChildren(productmap);

        shopsref.child(currentuserid).child(shoprandomkey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    loadingbar.dismiss();
                    Toast.makeText(Add_Shop.this,"Shop Is Added Sucessfully",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingbar.dismiss();

                    String message = task.getException().toString();
                    Toast.makeText(Add_Shop.this,"Error" + message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
