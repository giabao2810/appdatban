package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.nio.file.Path;

public abstract class RegisterUser<patterns> extends AppCompatActivity  implements View.OnClickListener {
    private TextView banner, RegisterUser;
    private EditText editTextFullname, editTextemail, editTextPassword;
    private ProgressBar ProgressBar;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        RegisterUser = (Button) findViewById(R.id.registerUser);
        RegisterUser.setOnClickListener(this);

        editTextFullname = (EditText) findViewById(R.id.fullname);
        editTextemail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        ProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);


    }

    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.registerUser:
                RegisterUser();
                break;
        }
    }

    private void RegisterUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String Fullname = editTextFullname.getText().toString().trim();

        if (email.isEmpty()) {

            editTextemail.setError("email is requird!");
            editTextemail.requestFocus();
            return;
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("please provide valid email!");
            editTextemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is requird");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        ProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(Fullname,email);
                        FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterUser.this,  " User has been register successfull",Toast.LENGTH_LONG).show();
                                    ProgressBar.setVisibility(View.VISIBLE);

                                    //login

                                }else {
                                    Toast.makeText(RegisterUser.this,  "Failed to register! Try again ",Toast.LENGTH_LONG).show();
                                    ProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }else {
                        Toast.makeText(RegisterUser.this,  "Failed to register! Try again ",Toast.LENGTH_LONG).show();
                        ProgressBar.setVisibility(View.GONE);
                    }
                }
            });
    }
}