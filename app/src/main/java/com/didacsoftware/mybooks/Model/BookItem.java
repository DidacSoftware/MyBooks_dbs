package com.didacsoftware.mybooks.Model;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BookItem {


    // Array lista para los items
    public static final List<BookItem.BookDetalle> ITEMS = new ArrayList<BookItem.BookDetalle>();

    // HashMap para los detalles
    public static final Map<String, BookItem.BookDetalle> ITEM_MAP = new HashMap<String, BookItem.BookDetalle>();






    // Cantidad de intems que aparecera en la lista
    private static final int COUNT = 5;

    static {
        // Add some sample items.
        //for (int i = 1; i <= COUNT; i++) {
        //    addItem(createBookDetalle(i));
        //}
    }

    // Agrega los Items a los arrays
    private static void addItem(BookItem.BookDetalle item) {
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
        
        @Override
        public String toString() {
            return sTitulo;
        }
    }
}
