package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_Activity extends AppCompatActivity {
    Button button;
    TextView txtvw;
    String email,passs;
    TextInputEditText passEditText;
    TextInputEditText emailEditText;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        button=findViewById(R.id.button);
        txtvw=findViewById(R.id.txtvw);
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        passEditText=findViewById(R.id.passEditText);
        emailEditText=findViewById(R.id.emailEditText);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passs=passEditText.getText().toString();
                email=emailEditText.getText().toString();

                if(email.equals("") || passs.equals("")){
                    if(email.equals("")){
                        emailEditText.setError("Please enter an Email can not be empty");
                    }else{
                       showToast("Please Enter Password");
                    }
                } else{
                    auth.signInWithEmailAndPassword(email,passs)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        showToast("Email and Password not Matching!");
                                    }
                                }
                            });

                }

            }
        });

        txtvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b=new Intent(getApplicationContext(), sign_Activity.class);
                startActivity(b);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!= null){
            Intent intent=new Intent(login_Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void showToast(String text){
        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(R.color.ColorAccent));
        snackbar.setTextColor(getColor(R.color.white));
        snackbar.show();
    }
}