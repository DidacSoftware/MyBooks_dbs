package com.didacsoftware.mybooks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.didacsoftware.mybooks.Model.BookItem;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

/**
 * 
 */
public class BookDetailFragment extends Fragment {
   
    public static final String ARG_ITEM_ID = "item_id";
  
    private BookItem.BookDetalle mItem;



    public BookDetailFragment() {
    }





    // Titulo de cabecera
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem = BookItem.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.sTitulo);
            }
        }




    }





    // Vista de cada item de la lista
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);





        // Formato de fecha
        SimpleDateFormat sdf
                = new SimpleDateFormat ("dd.MM.yyyy");
        String sDate = sdf.format(mItem.dtDataPublicacion);



    /*    Picasso.with(getContext())
                .load("http://didacsoftware.com/onewebstatic/c565166c1a-Imagen-Programas-NetBeans.png")
                // en caso de que no descargue la imagen mostrara esto
                .error(R.drawable.ic_identificacion_no_verificada)
                // para que se adapte al image view
                .fit()
                .centerInside()
                .into(imgImagen);
*/

        if (mItem != null) {

            ((TextView) rootView.findViewById(R.id.bkDt_txvAutor)).setText(mItem.sAutor);
            ((TextView) rootView.findViewById(R.id.bkDt_txvFecha)).setText(sDate);
            ((TextView) rootView.findViewById(R.id.bkDt_Descripcion)).setText(mItem.sDescripcion);
            //((ImageView) rootView.findViewById(R.id.imgBooks)).setImageResource();


            Picasso.with(getContext())
                    .load(mItem.getsImageURL())
                    // en caso de que no descargue la imagen mostrara esto
                    .error(R.drawable.ic_identificacion_no_verificada)
                    // para que se adapte al image view
                    .fit()
                    .centerInside()
                    .into(((ImageView) rootView.findViewById(R.id.imgBooks)));

        }

        return rootView;
    }

}
