package com.didacsoftware.mybooks;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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



    DatabaseReference dbr;
    private FirebaseAuth mAuth;

    ArrayList<model> almModel;
    ListView lsv_Lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        lsv_Lista = findViewById(R.id.lsv_Lista);
        mAuth = FirebaseAuth.getInstance();

        dbr = FirebaseDatabase.getInstance().getReference("books");

        dbr.addValueEventListener(new ValueEventListener() {

            // se llama a este metodo cada vez que haya cambios en la bd
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                model mdl=null;
                almModel= new ArrayList<model>();

                ArrayAdapter<String> arrayAdapter;
                ArrayList<String> listado = new ArrayList<String>();


                //ContentValues values=new ContentValues();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    mdl = new model();
                    model md =  dataSnapshot1.getValue(model.class);

                    // values.put("author",md.getAuthor());
                    //String sAutor = md.getAuthor()+md.getDescription();

                    //mdl.setAuthor(md.getAuthor());

                    almModel.add(md);
                    //listado.add(sAutor);
                }



                BookItem.alModel =almModel;

                for(int i=0;i<almModel.size();i++){
                    listado.add(almModel.get(i).getAuthor()+" - "
                            +almModel.get(i).getDescription()+"\n"+almModel.get(i).getTitle());
                }

                arrayAdapter = new ArrayAdapter<String>(BookListActivity.this,android.R.layout.simple_list_item_1,listado);
                lsv_Lista.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
       // recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, BookItem.ITEMS, mTwoPane));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, BookItem.ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final BookListActivity mParentActivity;
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

        return super.onOptionsItemSelected(menu_seleccionado);
    }
    // Menu principal [fin]





    private void pv_BDFireBase(){


        // Referencia de la base de datos con el que nos hemos conctado
        dbr = FirebaseDatabase.getInstance().getReference("books");


        dbr.addValueEventListener(new ValueEventListener() {

            // se llama a este metodo cada vez que haya cambios en la bd
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayAdapter<String> arrayAdapter;
                ArrayList<String> listado = new ArrayList<String>();


                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                   // model md =  dataSnapshot1.getValue(model.class);

                   // String sAutor = md.getAuthor();
                  //  listado.add(sAutor);
                }


               // arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,listado);
               // lsv_Lista.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // Notificacion abriendoactivity
        if (getIntent().getExtras() != null){
            for (String key : getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString(key);
                // colocar mas textos sin pderder los anteriores
                //txv.append("\n" +key + ": " + value);
            }
        }





        // Firabase Token
        FirebaseInstanceId.getInstance().getInstanceId().
                addOnSuccessListener( BookListActivity.this,
                        new OnSuccessListener<InstanceIdResult>() {

                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();

                                Log.e("newToken",newToken);

                                // txv.setText(instanceIdResult.getId().toString());

                            }
                        });
    }





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


}
