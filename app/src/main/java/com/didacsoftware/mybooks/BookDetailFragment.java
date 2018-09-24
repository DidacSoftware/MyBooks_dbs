package com.didacsoftware.mybooks;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.didacsoftware.mybooks.Model.BookItem;

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





        if (mItem != null) {

            ((TextView) rootView.findViewById(R.id.bkDt_txvAutor)).setText(mItem.sAutor);
            ((TextView) rootView.findViewById(R.id.bkDt_txvFecha)).setText(sDate);
            ((TextView) rootView.findViewById(R.id.bkDt_Descripcion)).setText(mItem.sDescripcion);

        }

        return rootView;
    }
}
