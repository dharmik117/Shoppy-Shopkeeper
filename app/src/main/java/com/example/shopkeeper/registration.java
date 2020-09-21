package com.example.shopkeeper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Model.customer;


public class registration extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextemail;
    private EditText editTextpassword;
    private EditText edittextfullname;
    private EditText etuserid;
    private EditText edittextphone;
    private  RadioButton radiogendermale,radiogenderfemale;
    private Button buttonregister;
    private TextView textviewsignin;
    private Switch pemissionswitch;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    private DatabaseReference databaseReference;

    String Permission="";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();



        /*if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Board.class));
            finish();
        }*/


        progressDialog = new ProgressDialog(this);

        editTextpassword = (EditText) findViewById(R.id.password);
        editTextemail = (EditText) findViewById(R.id.edittextemail);
        edittextfullname = (EditText)findViewById(R.id.edittextfullname);
        etuserid = (EditText)findViewById(R.id.reguserid);
        edittextphone = (EditText)findViewById(R.id.edittextphone);
        buttonregister = (Button) findViewById(R.id.btnregister);
        textviewsignin = (TextView) findViewById(R.id.textviewsignin);

        pemissionswitch = (Switch) findViewById(R.id.permissionswitch);

        buttonregister.setOnClickListener(this);
        textviewsignin.setOnClickListener(this);


    }

    private void registerUser(){

        final String email = editTextemail.getText().toString().trim();
        final String password = editTextpassword.getText().toString().trim();
        final String fullname = edittextfullname.getText().toString().trim();
        final String userid = etuserid.getText().toString().trim();
        final String phone = edittextphone.getText().toString().trim();

        if (pemissionswitch.isChecked()){

            Permission="True";
        }

        else
            {
                Permission = "False";
            }


        if (TextUtils.isEmpty(email)){

            Toast.makeText(this,"Please Enter Email-Id",Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(password)){

            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(fullname)){

            Toast.makeText(this,"Please Enter Full Name",Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(userid)){

            Toast.makeText(this,"Please Enter Userid",Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(phone)){

            Toast.makeText(this,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show();
            return;

        }



        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            customer information = new customer(
                                    userid,
                                    fullname,
                                    email,
                                    phone,
                                    Permission
                            );

                            FirebaseDatabase.getInstance().getReference("ShopKeeperData")
                                    .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(information);


                            Toast.makeText(registration.this,"Sucessfully Registered",Toast.LENGTH_SHORT).show();
                            {
                                startActivity(new Intent(getApplicationContext(),Board.class));
                                finish();

                            }

                        }
                        else {
                            String message = task.getException().getMessage();
                            Toast.makeText(registration.this,"Error" + message,Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if (view == buttonregister)
        {
            registerUser();
        }

        if (view == textviewsignin){
            startActivity(new Intent(this,MainActivity.class));
        }

    }
}
