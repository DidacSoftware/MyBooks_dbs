package com.didacsoftware.mybooks;

import android.app.Activity;
import android.app.NotificationManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.didacsoftware.mybooks.BDSQLite.CampoTabla;
import com.didacsoftware.mybooks.BDSQLite.ConexionSQLiteHelper;
import com.didacsoftware.mybooks.Model.BookItem;
import com.didacsoftware.mybooks.Model.model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.ACTION_ATTACH_DATA;
import static android.content.Intent.ACTION_DELETE;

public class MostrarDetalle extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    ArrayList<String> DetalleBooks;
    public static ArrayList<model> ListaBooks;



    View mostrarDetalle;

    ImageView imgImagen;
    TextView txvAutor, txvFecha, txvDescripcion;

    String sRefPosicion="2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_detalle);


        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_basedatos", null, 1);

        mostrarDetalle = findViewById(R.id.mostrarDetalle);

        imgImagen = mostrarDetalle.findViewById(R.id.imgBooks);
        txvAutor = mostrarDetalle.findViewById(R.id.bkDt_txvAutor);
        txvFecha = mostrarDetalle.findViewById(R.id.bkDt_txvFecha);
        txvDescripcion = mostrarDetalle.findViewById(R.id.bkDt_Descripcion);



        if (getIntent().getExtras() != null) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //notificationManager.cancel(0);

            if (getIntent().getAction() == ACTION_ATTACH_DATA) {
                Log.d("BookListActivity", "bookmOSTRAR");

                Toast.makeText(MostrarDetalle.this, "bookmOSTRAR",
                        Toast.LENGTH_SHORT).show();




            }



        }

        consultarListaBooks();

        try {
            txvAutor.setText(ListaBooks.get(0).getAuthor()+"");
            txvFecha.setText(ListaBooks.get(0).getPublication_date()+"");
            txvDescripcion.setText(ListaBooks.get(0).getDescription()+"");


            String mItem = ListaBooks.get(0).getTitle()+"";

            // Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem);
            }

            Picasso.with(getApplicationContext())
                    .load(ListaBooks.get(0).getUrl_image()+"")
                    // en caso de que no descargue la imagen mostrara esto
                    .error(R.drawable.ic_identificacion_no_verificada)
                    // para que se adapte al image view
                    .fit()
                    .centerInside()
                    .into(((ImageView) mostrarDetalle.findViewById(R.id.imgBooks)));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El libro de esta posicion  ya no existe",Toast.LENGTH_LONG).show();

        }


    }


    private void consultarListaBooks() {

        SQLiteDatabase db=conn.getReadableDatabase();

        model mdl = null;

        ListaBooks = new ArrayList<model>();

        //Cursor cursor=db.rawQuery("SELECT * FROM "+ CampoTablas.TABLA_TBL1,null);

        //Cursor c = db.rawQuery(" SELECT codigo,nombre FROM Usuarios WHERE nombre='usu1' ", null);

        Cursor cursor=db.rawQuery(
                " SELECT "
                        + CampoTabla.BOOKS_iID+","
                        +CampoTabla.BOOKS_sAUTHOR+","
                        +CampoTabla.BOOKS_sDESCRIPTION+","
                        +CampoTabla.BOOKS_sPUBLICATION_DATE+","
                        +CampoTabla.BOOKS_sTITLE+","
                        +CampoTabla.BOOKS_sURL_IMAGE+

                        " FROM "
                        +CampoTabla.TABLA_BOOKS+

                        " WHERE "
                        +CampoTabla.BOOKS_iID+

                        "='"+sRefPosicion+"' ",null);


        while (cursor.moveToNext()){
            mdl=new model();
            mdl.setAuthor(cursor.getString(1));
            mdl.setDescription(cursor.getString(2));
            mdl.setPublication_date(cursor.getString(3));
            mdl.setTitle(cursor.getString(4));
            mdl.setUrl_image(cursor.getString(5));


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
}
