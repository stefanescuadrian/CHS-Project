package com.upt.cti.photogmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText eEmailLogin, ePasswordLogin;
    Spinner sRoleLogin;
    Button btnLogin;
    TextView tGoToRegister;
    ProgressBar progressBarLogin;
    FirebaseAuth firebaseAuth;
    String userID;


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
            String role = sRoleLogin.getSelectedItem().toString();

            if (!(validateLoginFields(email, password)))
            {
                return;
            }

            progressBarLogin.setVisibility(View.VISIBLE);


            //Authenticate the user

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    FirebaseFirestore firebaseFirestore;
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);

                    documentReference.addSnapshotListener(this, (documentSnapshot, error) -> {
                        assert documentSnapshot != null;
                        String currentRole = documentSnapshot.getString("userType"); //get the type of current user from database
                        assert currentRole != null;
                        if (currentRole.equals(role)) {
                            Toast.makeText(Login.this, "Autentificare cu succes!", Toast.LENGTH_SHORT).show();
                            if (role.equals("Client")){
                                startActivity(new Intent(getApplicationContext(), HelloClientActivity.class));
                            }
                            else if (role.equals("Fotograf")){
                                startActivity(new Intent(getApplicationContext(), HelloPhotographerActivity.class));
                            }
                        }
                        else {
                            progressBarLogin.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Acest rol nu este înregistrat!", Toast.LENGTH_SHORT).show();
                        }
                    });
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
            eEmailLogin.setError("Adresa de mail trebuie completata!");
            return false;
        }

        if (TextUtils.isEmpty(password)){
            ePasswordLogin.setError("Parola trebuie completata!");
            return false;
        }

        if (password.length() < 8){
            ePasswordLogin.setError("Parola trebuie sa fie mai mare de 8 caractere!");
            return false;
        }

        return true;

    }

}