package com.cs386.bvoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity
{

    EditText uFullName, uEmail, uPassword;
    Button uRegisterBtn;
    TextView uLoginBtn;
    FirebaseAuth fbAuth;
    ProgressBar prgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        uFullName    = findViewById(R.id.userName);
        uEmail       = findViewById(R.id.userEmail);
        uPassword    = findViewById(R.id.userPassword);
        uRegisterBtn = findViewById(R.id.signupbtn);
        uLoginBtn    = findViewById(R.id.toLogin);

        fbAuth = FirebaseAuth.getInstance();
        prgBar = findViewById(R.id.progressBar);

        if(fbAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        uRegisterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    uEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    uPassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 8)
                {
                    uPassword.setError("Password Must Be 8 or More Characters");
                    return;
                }

                prgBar.setVisibility(View.VISIBLE);

                // register the user to firebase

                fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterPage.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(RegisterPage.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            prgBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        uLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}
