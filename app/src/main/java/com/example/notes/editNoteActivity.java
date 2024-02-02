package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class editNoteActivity extends AppCompatActivity {
    EditText titlid,msgid;
    Button btn;
    //ImageView img,img2;
    Uri uri;
    String val;
    FirebaseUser user;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        titlid=findViewById(R.id.titlid);
        msgid=findViewById(R.id.msgid);
        //img=findViewById(R.id.img);
        //img2=findViewById(R.id.img2);
        btn=findViewById(R.id.btn);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NotesSiuu");
        actionBar.setSubtitle("Update your note");


        Intent intent=getIntent();
         val= intent.getStringExtra("id");
        user= FirebaseAuth.getInstance().getCurrentUser();
        String qw=user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(qw).child("1")
                .child(val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataStoreModel m=snapshot.getValue(DataStoreModel.class);
                    titlid.setText(m.getTitle());
                    msgid.setText(m.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tt=titlid.getText().toString();
                    String mm=msgid.getText().toString();
                    DataStoreModel dd=new DataStoreModel();
                    //final StorageReference reference= storage.getReference().child()

                    if (!mm.equals("") && !tt.equals("")){
                        dd.setTitle(tt);
                        dd.setMessage(mm);
                        dd.setId(val);

                        FirebaseDatabase.getInstance().getReference().child("Users").child(qw).child("1")
                                .child(val).setValue(dd).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent in=new Intent(editNoteActivity.this,MainActivity.class);
                                startActivity(in);
                                finish();
                            }
                        });
                    }else{
                    Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"Please enter text",Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(getResources().getColor(R.color.ColorAccent));
                    snackbar.setTextColor(getColor(R.color.white));
                    snackbar.show();
                    }


                }
            });
        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,3);            }
        });*/
    }
    @Override
    public void onBackPressed() {
        Intent in=new Intent(editNoteActivity.this,MainActivity.class);
        startActivity(in);
        finish();
    }
    /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            uri = data.getData();
            img2.setImageURI(uri);

        }
    }*/
}