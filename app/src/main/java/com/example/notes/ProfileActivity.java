package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    EditText Name,Email,Number;
    Button btn;
    TextView logoutBtn;
    dataModel model;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Name=findViewById(R.id.profile_username);
        Email=findViewById(R.id.profile_email);
        Number=findViewById(R.id.profile_phone);
        btn=findViewById(R.id.profle_update_btn);
        logoutBtn=findViewById(R.id.logout_btn);
        auth=FirebaseAuth.getInstance();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setLogo(R.drawable.pencil);
        getUserData();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure want to logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.signOut();
                                Intent in=new Intent(ProfileActivity.this, login_Activity.class);
                                startActivity(in);
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    private void getUserData() {

        user= FirebaseAuth.getInstance().getCurrentUser();
        String id=user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model=snapshot.getValue(dataModel.class);
                Name.setText(model.getName());
                Email.setText(model.getEmail());
                Number.setText(model.getNumber());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}