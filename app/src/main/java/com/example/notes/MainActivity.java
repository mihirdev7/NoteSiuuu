package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn;
    RecyclerView recyid;

    LinearLayout linr,linrvw;
    FloatingActionButton fbtn;
   private FirebaseAuth auth;
   FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();;
    String gid;

    {
        assert user != null;
        gid = user.getUid();
    }

    ArrayList<DataStoreModel> list;
    DatabaseReference er;
    String iddd;
    DataStoreModel model;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intitvar();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NotSiuuu");
        actionBar.setSubtitle("Create a Notes");

        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_design);

                EditText editText,editText2;
                Button button;
                editText=dialog.findViewById(R.id.editText);
                editText2=dialog.findViewById(R.id.editText2);
                button=dialog.findViewById(R.id.button);
                //onBackPressed();


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String a=editText.getText().toString();
                        String b=editText2.getText().toString();
                        String c=a;


                        if(!b.equals("")&& !a.equals("")){
                            er=FirebaseDatabase.getInstance().getReference().child("Users").child(gid).child("1").push();
                            iddd=er.getKey();
                             DataStoreModel model=new DataStoreModel();
                            model.setMessage(b);
                            model.setTitle(c);
                            model.setId(er.getKey());
                            er.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }else{
                            showToast("Please enter Details");
                        }
                    }
                });
                dialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbtn.performClick();
            }
        });
        showNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void showNotes() {
        database.getReference().child("Users").child(gid)
                .child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               if(snapshot.exists()){
                    linr.setVisibility(View.GONE);
                    recyid.setVisibility(View.VISIBLE);
                    list.clear();
                    for(DataSnapshot s:snapshot.getChildren()) {
                        DataStoreModel n = s.getValue(DataStoreModel.class);
                        list.add(n);
                        notesAdapter adapter = new notesAdapter(MainActivity.this, list);
                        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
                        recyid.setLayoutManager(manager);
                        recyid.setAdapter(adapter);
                    }
                }else{
                    linr.setVisibility(View.VISIBLE);
                    recyid.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure want to Exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.profile_btn){
            Intent inn=new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(inn);
            finish();
            return true;}
        /*if(id==R.id.logout_btn){
            AlertDialog dialog=new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure want to logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            auth.signOut();
                            Intent in=new Intent(MainActivity.this, login_Activity.class);
                            startActivity(in);
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void intitvar() {
        btn=findViewById(R.id.btn);
        recyid=findViewById(R.id.recyid);
        fbtn=findViewById(R.id.fbtn);
        linr=findViewById(R.id.linr);
        linrvw=findViewById(R.id.linrvw);
        list=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        recyid.setLayoutManager(new GridLayoutManager(this,2));
    }
    public void showToast(String text){
        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(R.color.ColorAccent));
        snackbar.setTextColor(getColor(R.color.white));
        snackbar.show();
    }

}