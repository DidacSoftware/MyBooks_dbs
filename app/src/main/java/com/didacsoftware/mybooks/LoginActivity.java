package com.didacsoftware.mybooks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.didacsoftware.mybooks.BDSQLite.CampoTabla;
import com.didacsoftware.mybooks.BDSQLite.ConexionSQLiteHelper;
import com.didacsoftware.mybooks.Model.model;
import com.didacsoftware.mybooks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPasword;

    Button btnEntrar, btnRegistrar, lgn_btnIr;


    private FirebaseAuth mAuth;


    ConexionSQLiteHelper conn;

    public ArrayList<model> ListaBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_basedatos",null,1);

        //consultarListaBooks();

        pvGuardarInicio();


        edtEmail = findViewById(R.id.edtEmail);

        edtPasword = findViewById(R.id.edtPasword);

        btnEntrar = findViewById(R.id.btnEntrar);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        lgn_btnIr = findViewById(R.id.lgn_btnIr);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String email = edtEmail.getText().toString();

                    String password = edtPasword.getText().toString();

                    login(email,password);
                }catch (Exception e){

                    Toast.makeText(LoginActivity.this, "Correo electronico o contraseñas incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String email = edtEmail.getText().toString();

                    String password = edtPasword.getText().toString();

                    registrar(email,password);
                }catch (Exception e){

                    Toast.makeText(LoginActivity.this, "Correo electronico o contraseñas incorrectos",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });




        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String sCadena = "";
        if(bundle!=null){

             sCadena = (String) bundle.get("DatoEnviado");

        }

        final String finalSCadena = sCadena;
        lgn_btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(LoginActivity.this,BookListActivity.class);
                intent.putExtra("DatoEnviado", finalSCadena);

                LoginActivity.this.startActivity(intent);
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




    private void pvGuardarInicio() {


        SQLiteDatabase db=conn.getWritableDatabase();



        ListaBooks = BookListActivity.ListaBooksNew;

        for (int i = 0;i <ListaBooks.size();i++){
            ContentValues values=new ContentValues();


            values.put(CampoTabla.BOOKS_sAUTHOR, ListaBooks.get(i).getAuthor());
            values.put(CampoTabla.BOOKS_sDESCRIPTION, ListaBooks.get(i).getDescription());
            values.put(CampoTabla.BOOKS_sPUBLICATION_DATE, ListaBooks.get(i).getPublication_date());
            values.put(CampoTabla.BOOKS_sTITLE, ListaBooks.get(i).getTitle());
            values.put(CampoTabla.BOOKS_sURL_IMAGE, ListaBooks.get(i).getUrl_image());


            // variable para mostrar la posicion en que se ha guardado
            Long idResultante=db.insert(CampoTabla.TABLA_BOOKS, CampoTabla.BOOKS_iID,values);
        }




        db.close();
        Toast.makeText(getApplicationContext(),"Guardado TBL2 Registro: ",Toast.LENGTH_SHORT).show();

    }


}
