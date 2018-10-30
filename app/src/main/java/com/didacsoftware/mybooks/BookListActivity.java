package com.didacsoftware.mybooks;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.didacsoftware.mybooks.BDSQLite.CampoTabla;
import com.didacsoftware.mybooks.BDSQLite.ConexionSQLiteHelper;
import com.didacsoftware.mybooks.BDSQLite.GestionBD_Temp;
import com.didacsoftware.mybooks.Model.BookItem;
import com.didacsoftware.mybooks.Model.model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class BookListActivity extends AppCompatActivity {

    private boolean mTwoPane;



    SwipeRefreshLayout swipeRefreshLayout;

    DatabaseReference dbr;
    private FirebaseAuth mAuth;

    ArrayList<model> almModel;
    ListView lsvLista;

    ConexionSQLiteHelper conn;
    ArrayList<String> DetalleBooks;
    public static ArrayList<model> ListaBooks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);



        lsvLista= findViewById(R.id.lsvLista);
        mAuth = FirebaseAuth.getInstance();

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_basedatos",null,1);

        consultarListaBooks();

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

                arrayAdapter = new ArrayAdapter<String>(BookListActivity.this,android.R.layout.simple_list_item_1,listado);
                //lsvLista.setAdapter(arrayAdapter);

                ListaBooks=almModel;

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        //pv_FireBase();

        //String sFB = String.valueOf(almModel.size());
        //this.setTitle(" FB: "+String.valueOf(almModel.size())+" SQLite: "+String.valueOf(ListaBooks.size()));
        //this.setTitle(sFB);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                       // asyncTask = new BackgroundTask();
                    //    Void[] params = null;
                      //  asyncTask.execute(params);

                        Toast.makeText(getApplicationContext(),"este mensaje",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                });







        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());



        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                Intent intent = new Intent(BookListActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        if (findViewById(R.id.book_detail_container) != null) {

            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }





    // [inicio] .- FireBase
    private void pv_FireBase() {

        dbr = FirebaseDatabase.getInstance().getReference("books");

        dbr.addValueEventListener(new ValueEventListener() {

            // se llama a este metodo cada vez que haya cambios en la bd
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                almModel= new ArrayList<model>();


                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    model md =  dataSnapshot1.getValue(model.class);

                    almModel.add(md);

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }
    // [fin] .- FireBase





    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, BookItem.ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final BookListActivity mParentActivity;
        //private final List<BookItem.BookDetalle> mValues;
        private final List<BookItem.BookDetalle> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookItem.BookDetalle item = (BookItem.BookDetalle) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.iId));
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.iId));

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(BookListActivity parent,
                                      List<BookItem.BookDetalle> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }





        // variable para contar la lista que se va agregando
        int count = 1;

        // Agrega la lista con los formatos pares e impares
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // View para los pares
            View viewPar = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content_par, parent, false);

            // View para los impares
            View viewImpar = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content_impar, parent, false);

            // View return para definir pares o impares
            View viewReturn=viewImpar;;





            // Verificamos si es par o impar
            if (count%2==0){
                viewReturn=viewPar;

            }else{
                viewReturn=viewImpar;
            }



            return new ViewHolder(viewReturn);
        }





        // Construye los items
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).sTitulo);
            holder.mContentView.setText(mValues.get(position).sAutor);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);


            // Por cada item agregado suma 1
            count +=1;
        }





        //
        @Override
        public int getItemCount() {
                    return mValues.size();
          // return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }




    // Menu principal [inicio]
    @Override
    public boolean onCreateOptionsMenu(Menu menu_principal){

        getMenuInflater().inflate(R.menu.menu_principal, menu_principal);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem menu_seleccionado){
        int iId = menu_seleccionado.getItemId();

        if (iId == R.id.mnu_login){
            Intent intent = new Intent(BookListActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (iId == R.id.mnu_configuracion){

            return true;
        }
        if (iId == R.id.mnu_gestionar_bd){

            Intent intent = new Intent(BookListActivity.this, GestionBD_Temp.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(menu_seleccionado);
    }
    // Menu principal [fin]







    // Mostrar notificacion local o enviado desde Firebase
    private void mostrarNotificacion(String title, String body) {

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





        // mostrar notificaciones en alerta de notificaciones
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("info del contenido");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());

    }





  // verifica si esta logueado
    @Override
    public void onStart() {
        super.onStart();

        final FloatingActionButton fab = findViewById(R.id.fab);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null){

            Toast.makeText(BookListActivity.this, "Conectado",
                    Toast.LENGTH_SHORT).show();
            fab.setImageResource(R.drawable.ic_identificacion_verificada);

        }else{
            Toast.makeText(BookListActivity.this, "DesConectado",
                    Toast.LENGTH_SHORT).show();
            fab.setImageResource(R.drawable.ic_identificacion_no_verificada);
        }
    }




    // [inicio] Gestion BaseDatos SQLite
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
    // [fin] Gestion BaseDatos SQLite
}
