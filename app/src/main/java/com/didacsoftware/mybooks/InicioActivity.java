package com.didacsoftware.mybooks;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.didacsoftware.mybooks.BDSQLite.CampoTabla;
import com.didacsoftware.mybooks.BDSQLite.ConexionSQLiteHelper;
import com.didacsoftware.mybooks.Model.BookListNavDraw;
import com.didacsoftware.mybooks.Model.model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Intent.ACTION_ATTACH_DATA;
import static android.content.Intent.ACTION_DELETE;

public class InicioActivity extends AppCompatActivity {



    Button btnLogin, btnRegistrar, btnIr,btnNotificacion;
    EditText edtCorreo, edtContrasenha;
    ImageView imgLogin;



    // FB Login
    private FirebaseAuth mAuth;

    // FB BaseDatos
    DatabaseReference dbr;
    ArrayList<model> almModel;

    // SQLite
    ConexionSQLiteHelper conn;
    ArrayList<String> DetalleBooks;
    public  ArrayList<model> ListaBooks;
    ArrayList<model> ListaSQLite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        // [FB Login] instancia
        mAuth = FirebaseAuth.getInstance();

        // SQLite
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_basedatos",null,1);





        btnLogin = findViewById(R.id.inc_btnLogin);
        btnRegistrar = findViewById(R.id.inc_btnRegistrar);
        btnIr = findViewById(R.id.inc_btnIr);
        btnNotificacion = findViewById(R.id.inc_btnNotificacion);

        edtCorreo = findViewById(R.id.inc_edtEmail);
        edtContrasenha = findViewById(R.id.inc_edtContrasenha);

        imgLogin = findViewById(R.id.inc_imgLogin);





        // FB BaseDatos
        dbr = FirebaseDatabase.getInstance().getReference("books");

        dbr.addValueEventListener(new ValueEventListener() {

            // se llama a este metodo cada vez que haya cambios en la bd
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                almModel= new ArrayList<model>();

                ArrayAdapter<String> arrayAdapter;
                ArrayList<String> listado = new ArrayList<String>();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    model md =  dataSnapshot1.getValue(model.class);

                    almModel.add(md);

                }

                for(int i=0;i<almModel.size();i++){
                    listado.add(almModel.get(i).getAuthor()+" - "
                            +almModel.get(i).getDescription()+"\n"+almModel.get(i).getTitle());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

/*
        if (getIntent().getExtras()!=null){
            for(String key:getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString(key);
                edtCorreo.append("\n"+ key + " : " + value);

            }
        }*/



        // Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String email = edtCorreo.getText().toString();

                    String password = edtContrasenha.getText().toString();

                    login(email,password);

                }catch (Exception e){

                    Toast.makeText(InicioActivity.this, "Correo electronico o contraseñas incorrectos",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });





        // Registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                try {
                    String email = edtCorreo.getText().toString();

                    String password = edtContrasenha.getText().toString();

                    registrar(email,password);
                }catch (Exception e){

                    Toast.makeText(InicioActivity.this, "Correo electronico o contraseñas incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
*/

                Intent intent = new Intent(InicioActivity.this,BookListNavDraw.class);
                InicioActivity.this.startActivity(intent);

            }
        });





        // Abrir Lista de Libros
        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultarListaSQLite();

                if (almModel.size()!=ListaSQLite.size() && ListaSQLite.size()<=0){


                    //int fb =almModel.size();
                    //int sq =ListaSQLite.size();

                   // Toast.makeText(InicioActivity.this, "difer "+fb+"  sq "+sq,
                    //        Toast.LENGTH_SHORT).show();

                    pvGuardarSQLite();

                   //

                }


                //Intent intent = new Intent(InicioActivity.this,MostrarDetalle.class);

                Intent intent = new Intent(InicioActivity.this,BookListActivity.class);
                InicioActivity.this.startActivity(intent);

            }
        });


        // Abrir Lista de Libros
        btnNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarNotificacion("Se elimina o se borra un libro","2","elimina o se borra un libro");
                //Intent intent = new Intent(InicioActivity.this,BookListActivity.class);
                //InicioActivity.this.startActivity(intent);

            }
        });




        // mensaje fB prueba
        if (getIntent().getExtras() !=null){
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //notificationManager.cancel(0);
            if (getIntent().getAction()== ACTION_DELETE){
                Log.d("BookListActivity","bookDelete");

                //Toast.makeText(InicioActivity.this, "book_Eliminar",
                 //       Toast.LENGTH_SHORT).show();
                pvEliminar("2");
            }
            if (getIntent().getAction()== ACTION_ATTACH_DATA){
                Log.d("BookListActivity","book_MOSTRAR");

                Toast.makeText(InicioActivity.this, "book_MOSTRAR",
                        Toast.LENGTH_SHORT).show();
            }

            for(String key : getIntent().getExtras().keySet()){
                Object value = getIntent().getExtras().get(key);
                Log.d("BookListActivity","Key: "+key+"value : "+value);
            }



        }else {
            Toast.makeText(InicioActivity.this, "BookListActivity",
                    Toast.LENGTH_SHORT).show();
        }


    }
    //[fin OnCreate]





    // [FB Login] Verifica si esta logueado
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null){

           // Toast.makeText(InicioActivity.this, "Conectado",
            //        Toast.LENGTH_SHORT).show();

            // cambiar imagen de login
            imgLogin.setImageResource(R.drawable.ic_identificacion_verificada);


        }else{
         //   Toast.makeText(InicioActivity.this, "DesConectado",
         //           Toast.LENGTH_SHORT).show();

            // cambiar imagen de login
            imgLogin.setImageResource(R.drawable.ic_identificacion_no_verificada);
        }
    }





    // [FB Login] Hacer login
    private void login(String email, String password) {

        mAuth = FirebaseAuth.getInstance();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(InicioActivity.this, "Login sussefull."+ user.getUid(),
                                    Toast.LENGTH_SHORT).show();

                            // cambiar imagen de login
                            imgLogin.setImageResource(R.drawable.ic_identificacion_verificada);

                        } else {

                            Toast.makeText(InicioActivity.this, "Login failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
        });
    }





    // [] Registrar
    private void registrar(String email, String password) {

        mAuth = FirebaseAuth.getInstance();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(InicioActivity.this, "Authentication sussefull."+ user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            mAuth.signOut();

                        } else {
                            Toast.makeText(InicioActivity.this, "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }





    //  Gestion BaseDatos SQLite
    private void consultarListaSQLite() {

        SQLiteDatabase db=conn.getReadableDatabase();

        model mdl = null;

        ListaSQLite = new ArrayList<model>();

        Cursor cursor=db.rawQuery("SELECT * FROM "+ CampoTabla.TABLA_BOOKS,null);

        while (cursor.moveToNext()){
            mdl=new model();
            mdl.setAuthor(cursor.getString(1));
            mdl.setDescription(cursor.getString(2));
            mdl.setPublication_date(cursor.getString(3));
            mdl.setTitle(cursor.getString(4));
            mdl.setUrl_image(cursor.getString(5));



            ListaSQLite.add(mdl);

        }

    }





    // Guardar en SQLite datos del FireBase
    private void pvGuardarSQLite() {


        SQLiteDatabase db=conn.getWritableDatabase();



        ListaBooks = almModel;

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



    // Mostrar notificacion local o enviado desde Firebase
    private void mostrarNotificacion(String title, String book_position, String body) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";



        // Necesario para versiones anteriores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "esta notificacion", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Parte Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }







        Intent intent = new Intent(this, InicioActivity.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra("book_position",book_position);

        PendingIntent borrarIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Intent intent2 = new Intent(this, MostrarDetalle.class);
        intent2.setAction(ACTION_ATTACH_DATA);
        intent2.putExtra("accion","MOSTRAR");
        PendingIntent resendIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent2, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.libros_en_pila)
                        .setContentTitle(title)
                        .setContentText("MyBooks_dbs")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("El libro que se va a eliminar o mostrar esta en la posicion: "+book_position))
                        .addAction(new NotificationCompat.Action(R.drawable.ic_launcher_foreground, "Borrar", borrarIntent))
                        .addAction(new NotificationCompat.Action(R.drawable.ic_launcher_foreground, "Mostrar", resendIntent));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Mostrar la notificación
        mNotificationManager.notify(0, mBuilder.build());


    }

    private void pvEliminar(String iId) {

        SQLiteDatabase db=conn.getReadableDatabase();


            db.delete(CampoTabla.TABLA_BOOKS, CampoTabla.BOOKS_iID+"="+iId, null);

            Toast.makeText(getApplicationContext(),"El libro de la posicion "+iId+" ha sido Eliminado",Toast.LENGTH_LONG).show();


    }



}
