package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_Activity extends AppCompatActivity {
    TextInputEditText nameEditText, emailEditText, mobileEditText, passEditText;
    TextInputLayout passwordLayout;
    Button registerBTN;
    TextView loginText;
    static String Names, Emails, Passs, Phones;
    private FirebaseAuth auth;
    private static FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference ref;
     ProgressBar progressBar;
    private ConstraintLayout MainLayout;
   static boolean checkEmailVerification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        nameEditText = findViewById(R.id.nameEditText);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        passEditText = findViewById(R.id.passEditText);
        registerBTN = findViewById(R.id.registerBTN);
        loginText = findViewById(R.id.loginText);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        passEditText.addTextChangedListener(new passwordValidator());
       progressBar=findViewById(R.id.progressBar);


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(sign_Activity.this, login_Activity.class);
                startActivity(j);
                finish();
            }
        });
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emails = emailEditText.getText().toString();
                Passs = passEditText.getText().toString();
                Phones = mobileEditText.getText().toString();
                Names = nameEditText.getText().toString();

                if(checkEmailVerification){
                    checkEmailVerification =false;
                    checkVerificationStatus();
                    return;
                }

                if (Emails.equals("") || Passs.equals("") || Phones.equals("") || Names.equals("") || !isValidPassword(Passs)) {
                    showToast("Please Enter Credentials");
                } else {
                    registerBTN.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(Emails, Passs)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                         user=auth.getCurrentUser();
                                        if (user != null) {
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    if (task.isSuccessful()) {
                                                            checkVerificationStatus();
                                                    } else {
                                                        registerBTN.setEnabled(true);
                                                        showToast("Failed to send verification email");
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        registerBTN.setEnabled(true);
                                        showToast("Registration Failed: " + task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }
    private class passwordValidator implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String password = s.toString();
            if (isValidPassword(password)) {
                passwordLayout.setError("");
            } else {
                passwordLayout.setError("Password must contain upper, lowercase characters, numbers, symbol, and have a length of 8 or greater!");
            }
        }
    }

    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return !TextUtils.isEmpty(password) && password.matches(pattern);
    }

    private void checkVerificationStatus() {
        progressBar.setVisibility(View.VISIBLE);
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);

                        if (user.isEmailVerified()) {
                            dataModel userData = new dataModel(Names, Emails, Passs, Phones);
                            String userId = auth.getCurrentUser().getUid();
                            database.getReference().child("Users").child(userId).setValue(userData);

                            showToast("Registration successful");

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("ky", userId);
                            startActivity(intent);
                            finish();
                        } else {
                            registerBTN.setEnabled(true);
                            showToast("Please verify your email before logging in");
                            checkEmailVerification =true;
                        }
                    }
                });
    }
    public void showToast(String text){
        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),text,Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(R.color.ColorAccent));
        snackbar.setTextColor(getColor(R.color.white));
        snackbar.show();
    }
}