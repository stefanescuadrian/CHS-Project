package com.upt.cti.photogmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {
    EditText eFirstNameRegister, eLastNameRegister, eEmailRegister, ePasswordRegister, eConfirmPasswordRegister, ePhoneNumber;
    Spinner sRoleRegister;
    Button btnRegister;
    TextView tGoToLogin;
    ProgressBar progressBarRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_register);

        eFirstNameRegister = findViewById(R.id.eFirstNameRegister);
        eLastNameRegister = findViewById(R.id.eLastNameRegister);
        eEmailRegister = findViewById(R.id.eEmailRegister);
        ePasswordRegister = findViewById(R.id.ePasswordRegister);
        eConfirmPasswordRegister = findViewById(R.id.eConfirmPasswordRegister);
        ePhoneNumber = findViewById(R.id.ePhoneNumber);
        sRoleRegister = findViewById(R.id.sRoleRegister);
        btnRegister = findViewById(R.id.btnRegister);
        progressBarRegister = findViewById(R.id.progressBarRegister);
        tGoToLogin = findViewById(R.id.tLoginHere);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();






        sRoleRegister.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String role = sRoleRegister.getSelectedItem().toString();
                boolean isClient = role.equals("Client");
                int visibilityForPhone = isClient ? View.INVISIBLE : View.VISIBLE;
                ePhoneNumber.setVisibility(visibilityForPhone);// set phone Number enable just if role is photographer
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Actions to do when Register button is clicked
        btnRegister.setOnClickListener(v -> {

            String email = eEmailRegister.getText().toString().trim(); // get email
            String password = ePasswordRegister.getText().toString().trim(); // get password
            String confirmPassword = eConfirmPasswordRegister.getText().toString().trim(); //get confirm password
            String firstName = eFirstNameRegister.getText().toString(); //get confirm password
            String lastName = eLastNameRegister.getText().toString(); //get last name
            String role = sRoleRegister.getSelectedItem().toString();



            if (!(validateRegisterFields(email,password,confirmPassword,firstName,lastName))) {
                return;
            }

            progressBarRegister.setVisibility(View.VISIBLE);

            //Create User With Email And Password
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(Register.this, "User Created...", Toast.LENGTH_SHORT).show();
                    // STORE ADDITIONAL DATA FOR USER CREATED TO CLOUD FIREBASE
                    userID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("mail", email);
                    user.put("userType", role);

                    if (ePhoneNumber.getVisibility()==View.VISIBLE) {
                        String phoneNumber = ePhoneNumber.getText().toString(); // get phone number
                        user.put("phoneNumber", phoneNumber);
                        user.put("score", 0);
                        user.put("noOfVotes",0);
                        user.put("country", "Fără țară");
                        user.put("county", "Fără județ");
                        user.put("locality", "Fără localitate");

                    }

                    documentReference.set(user).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Profilul utilizatorului a fost creat pentru: " + userID));
                    if (role.equals("Client")){
                        startActivity(new Intent(getApplicationContext(), MainActivityClient.class));

                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), MainActivityPhotographer.class));
                    }

                } else {
                    progressBarRegister.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });


    tGoToLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));

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