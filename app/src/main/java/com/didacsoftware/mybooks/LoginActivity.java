package com.didacsoftware.mybooks;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.didacsoftware.mybooks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPasword;

    Button btnEntrar,btnRegistrar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        edtEmail = findViewById(R.id.edtEmail);

        edtPasword = findViewById(R.id.edtPasword);

        btnEntrar = findViewById(R.id.btnEntrar);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();

                String password = edtPasword.getText().toString();

                login(email,password);
            }
        });


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = edtEmail.getText().toString();

                String password = edtPasword.getText().toString();

                registrar(email,password);


            }
        });
    }

    private void registrar(String email, String password) {

        mAuth = FirebaseAuth.getInstance();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Authentication sussefull."+ user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            mAuth.signOut();

                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void login(String email, String password) {

        mAuth = FirebaseAuth.getInstance();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Login sussefull."+ user.getUid(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    // verifica si esta logueado
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null){

            Toast.makeText(LoginActivity.this, "Conectado",
                    Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(LoginActivity.this, "DesConectado",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
