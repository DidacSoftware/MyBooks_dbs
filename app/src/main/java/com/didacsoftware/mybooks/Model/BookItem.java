package com.didacsoftware.mybooks.Model;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.didacsoftware.mybooks.BDSQLite.CampoTabla;
import com.didacsoftware.mybooks.BDSQLite.ConexionSQLiteHelper;
import com.didacsoftware.mybooks.BookListActivity;
import com.didacsoftware.mybooks.Global;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BookItem {




    // Array lista para los items
    public static final List<BookDetalle> ITEMS = new ArrayList<BookDetalle>();

    // HashMap para los detalles
    public static final Map<String, BookDetalle> ITEM_MAP = new HashMap<String, BookDetalle>();


    static BookListActivity alListaBooks;
    //public static List<model> alModel;



    // Cantidad de intems que aparecera en la lista
    //private static final int COUNT = 5;





    // Fechas Temporales
    static String sNuevaFecha1 = "01/02/2012";
    static String sNuevaFecha2 = "05/07/2015";
    static String sNuevaFecha3 = "03/10/2016";
    static String sNuevaFecha4 = "24/04/2018";

    // Date con la nueva fecha
    static Date date1 = new Date(sNuevaFecha1);
    static Date date2 = new Date(sNuevaFecha2);
    static Date date3 = new Date(sNuevaFecha3);
    static Date date4 = new Date(sNuevaFecha4);





    //
    static {
    /* addItem(new BookDetalle(1,BookListActivity.ListaBooks.get(1).getTitle(),BookListActivity.ListaBooks.get(1).getAuthor(),date1,"Curso de Java", "URL"));
        addItem(new BookDetalle(2,"C++ Developer","DidacSoftware",date2,"Curso de C++", "URL"));
        addItem(new BookDetalle(3,"Pascal Developer","DidacSoftware",date3,"Curso de Pascal", "URL"));
        addItem(new BookDetalle(4,"IDE Eclipse","DidacSoftware",date4,"Manual Eclipse", "URL"));

*/

    for (int i =0;i<alListaBooks.ListaBooks.size();i++){

        addItem(new BookDetalle(i,alListaBooks.ListaBooks.get(i).getTitle(),
                alListaBooks.ListaBooks.get(i).getAuthor(),
                date4,
                alListaBooks.ListaBooks.get(i).getDescription(),
                alListaBooks.ListaBooks.get(i).getUrl_image()));
    }




    }






    // Agrega los Items a los arrays
    private static void addItem(BookDetalle item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.iId), item);
    }





    /*
    private static BookItem.BookDetalle createBookDetalle(int position) {
        return new BookItem.BookDetalle(position, "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
    */





    // Lista de cada detalle que tendra cada libro
    public static class BookDetalle {
        public final int iId;
        public final String sTitulo;
        public final String sAutor;
        public final Date dtDataPublicacion;
        public final String sDescripcion;
        public final String sImageURL;


        public BookDetalle(int iId, String sTitulo, String sAutor, Date dtDataPublicacion, String sDescripcion, String sImageURL) {
            this.iId = iId;
            this.sTitulo = sTitulo;
            this.sAutor = sAutor;
            this.dtDataPublicacion = dtDataPublicacion;
            this.sDescripcion = sDescripcion;
            this.sImageURL = sImageURL;
        }

        public int getiId() {
            return iId;
        }

        public String getsTitulo() {
            return sTitulo;
        }

        public String getsAutor() {
            return sAutor;
        }

        public Date getDtDataPublicacion() {
            return dtDataPublicacion;
        }

        public String getsDescripcion() {
            return sDescripcion;
        }

        public String getsImageURL() {
            return sImageURL;
        }

        @Override
        public String toString() {
            return sTitulo;
        }
    }



}
