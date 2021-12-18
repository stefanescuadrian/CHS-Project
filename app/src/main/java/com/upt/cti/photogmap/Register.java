package com.upt.cti.photogmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {
    EditText eFirstNameRegister, eLastNameRegister, eEmailRegister, ePasswordRegister, eConfirmPasswordRegister;
    Spinner sRoleRegister;
    Button btnRegister;
    ProgressBar progressBarRegister;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eFirstNameRegister = findViewById(R.id.eFirstNameRegister);
        eLastNameRegister = findViewById(R.id.eLastNameRegister);
        eEmailRegister = findViewById(R.id.eEmailRegister);
        ePasswordRegister = findViewById(R.id.ePasswordRegister);
        eConfirmPasswordRegister = findViewById(R.id.eConfirmPasswordRegister);
        sRoleRegister = findViewById(R.id.sRoleRegister);
        btnRegister = findViewById(R.id.btnRegister);
        progressBarRegister = findViewById(R.id.progressBarRegister);

        firebaseAuth = FirebaseAuth.getInstance();



        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //Actions to do when Register button is clicked
        btnRegister.setOnClickListener(v -> {

            String email = eEmailRegister.getText().toString().trim(); // get email
            String password = ePasswordRegister.getText().toString().trim(); // get password
            String confirmPassword = eConfirmPasswordRegister.getText().toString().trim(); //get confirm password
            String firstName = eFirstNameRegister.getText().toString().trim(); //get confirm password
            String lastName = eLastNameRegister.getText().toString().trim(); //get last name


            if (!(validateRegisterFields(email,password,confirmPassword,firstName,lastName))) {
                return;
            }

            progressBarRegister.setVisibility(View.VISIBLE);

            //Create User With Email And Password
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(Register.this, "User Created...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(Register.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            });



        });


    }

    private boolean validateRegisterFields(String email, String password, String confirmPassword, String firstName, String lastName){
        if (TextUtils.isEmpty(firstName)){
            eFirstNameRegister.setError("First Name is required!");
            return false;
        }

        if (TextUtils.isEmpty(lastName)){
            eLastNameRegister.setError("Last Name is required!");
            return false;
        }

        if (TextUtils.isEmpty(email)){
            eEmailRegister.setError("Email is required!");
            return false;
        }

        if (TextUtils.isEmpty(password)){
            ePasswordRegister.setError("Password is required!");
            return false;
        }

        if (password.length() < 8){
            ePasswordRegister.setError("Password must be greater than 8 characters!");
            return false;
        }


        if (TextUtils.isEmpty(confirmPassword)){
            eConfirmPasswordRegister.setError("Confirm Password is required!");
            return false;
        }


        if (!(password.equals(confirmPassword))){
            eConfirmPasswordRegister.setError("Confirm password is not the same with your password!");
            return false;
        }

        return true;

    }
}