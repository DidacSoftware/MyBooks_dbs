package com.didacsoftware.mybooks.BDSQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.didacsoftware.mybooks.BookListActivity;
import com.didacsoftware.mybooks.Global;
import com.didacsoftware.mybooks.Model.model;
import com.didacsoftware.mybooks.R;

import java.util.ArrayList;

public class GestionBD_Temp extends AppCompatActivity {



    TextView txvID;

    Button btnBDConsultar, btnBDGuardar, btnBDActualizar, btnBDEliminar;



    ListView lsvBD;
    ConexionSQLiteHelper conn;
    ArrayList<String> DetalleBooks;
    public  ArrayList<model> ListaBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_bd__temp);


      //  this.sta

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_basedatos",null,1);

        consultarListaBooks();

        lsvBD=  findViewById(R.id.lsvBD);
        txvID = findViewById(R.id.txvID);






        btnBDConsultar =  findViewById(R.id.btnBDConsultar);
        btnBDGuardar =  findViewById(R.id.btnBDGuardar);
        btnBDActualizar = findViewById(R.id.btnBDActualizar);
        btnBDEliminar =  findViewById(R.id.btnBDEliminar);



        //

        ArrayAdapter adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1,DetalleBooks);
        lsvBD.setAdapter(adaptador);

        lsvBD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {



                //txvM.setText(ListaPersonas.get(pos).getsNombre());

                String informacion="id: "+ListaBooks.get(pos).getTitle()+"\n";
                informacion+="Nombre: "+ListaBooks.get(pos).getAuthor()+"\n";


                Toast.makeText(getApplicationContext(),informacion,Toast.LENGTH_LONG).show();


            }
        });






        btnBDGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              //  pvGuardarDatos();
                pvGuardarInicio();
            }
        });






    }






    private void consultarListaBooks() {

       SQLiteDatabase db=conn.getReadableDatabase();

        model mdl = null;

        ListaBooks = new ArrayList<model>();

        Cursor cursor=db.rawQuery("SELECT * FROM "+ CampoTabla.TABLA_BOOKS,null);

        while (cursor.moveToNext()){
            mdl=new model();
            mdl.setAuthor(cursor.getString(1));
            mdl.setDescription(cursor.getString(2));
            mdl.setPublication_date(cursor.getString(3));
            mdl.setTitle(cursor.getString(4));
            mdl.setUrl_image(cursor.getString(5));

            //Log.i("id",persona.getiId().toString());
            //Log.i("Nombre",persona.getsNombre());
            // Log.i("Tel",persona.getsTelefono());

            ListaBooks.add(mdl);

        }
        obtenerLista();

    }


    private void obtenerLista() {

        DetalleBooks=new ArrayList<String>();



        for(int i=0;i<ListaBooks.size();i++){
            DetalleBooks.add("Autor :.- "
                    +ListaBooks.get(i).getAuthor()+"\n.\n Title:.- "
                    +ListaBooks.get(i).getTitle()+"\n.\n Public:.- "
                    +ListaBooks.get(i).getPublication_date()+"\n.\n Url:.- "
                    +ListaBooks.get(i).getUrl_image()+"\n.\n desc"
                    +ListaBooks.get(i).getDescription());


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

