package com.upt.cti.photogmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText eEmailLogin, ePasswordLogin;
    Spinner sRoleLogin;
    Button btnLogin;
    TextView tGoToRegister;
    ProgressBar progressBarLogin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_login);



        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        eEmailLogin = findViewById(R.id.eEmailLogIn);
        ePasswordLogin = findViewById(R.id.ePasswordLogIn);
        sRoleLogin = findViewById(R.id.sRoleLogIn);
        btnLogin = findViewById(R.id.btnLogIn);
        tGoToRegister = findViewById(R.id.tCreateAccount);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBarLogin = findViewById(R.id.progressBarLogin);

        btnLogin.setOnClickListener(v -> {
            String email = eEmailLogin.getText().toString().trim();
            String password = ePasswordLogin.getText().toString().trim();

            if (!(validateLoginFields(email, password)))
            {
                return;
            }

            progressBarLogin.setVisibility(View.VISIBLE);


            //Authenticate the user

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    progressBarLogin.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        });

        tGoToRegister.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register.class)));

    }

    private boolean validateLoginFields(String email, String password){
        if (TextUtils.isEmpty(email)){
            eEmailLogin.setError("Email is required!");
            return false;
        }

        if (TextUtils.isEmpty(password)){
            ePasswordLogin.setError("Password is required!");
            return false;
        }

        if (password.length() < 8){
            ePasswordLogin.setError("Password must be greater than 8 characters!");
            return false;
        }

        return true;

    }

}