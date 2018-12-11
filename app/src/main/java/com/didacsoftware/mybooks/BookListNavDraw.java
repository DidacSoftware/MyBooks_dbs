package com.didacsoftware.mybooks;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.didacsoftware.mybooks.BDSQLite.CampoTabla;
import com.didacsoftware.mybooks.BDSQLite.ConexionSQLiteHelper;
import com.didacsoftware.mybooks.Model.BookItem;
import com.didacsoftware.mybooks.Model.model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_ATTACH_DATA;
import static android.content.Intent.ACTION_DELETE;


// envir por sociales
public class BookListNavDraw extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean mTwoPane;

    SwipeRefreshLayout swipeRefreshLayout;


    // FB BaseDatos
    DatabaseReference dbr;
    ArrayList<model> almModel;

    private FirebaseAuth mAuth;




    ConexionSQLiteHelper conn;
    ArrayList<String> DetalleBooks;
    public static ArrayList<model> ListaBooks;


    public static ArrayList<model> ListaBooksNew;


    ImageView imgFoto;
    TextView txvUsuario, txvEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_nav_draw);
        
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        
        */





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View navHeader = navigationView.getHeaderView(0);
        imgFoto = navHeader.findViewById(R.id.nvd_imgFoto);
        txvUsuario = navHeader.findViewById(R.id.nvd_txvUsuario);
        txvEmail = navHeader.findViewById(R.id.nvd_txvCorreo);



        imgFoto.setImageResource(R.drawable.ic_identificacion_no_verificada);
        txvUsuario.setText(Global.sUsuario);
        txvEmail.setText(Global.sEmail);


        //inicio codigo copiado


        mAuth = FirebaseAuth.getInstance();

        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_basedatos", null, 1);


        // FireBase BaseDatos
        dbr = FirebaseDatabase.getInstance().getReference("books");

        dbr.addValueEventListener(new ValueEventListener() {

            // se llama a este metodo cada vez que haya cambios en la bd
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                almModel = new ArrayList<model>();


                ArrayList<String> listado = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    model md = dataSnapshot1.getValue(model.class);

                    almModel.add(md);

                }

                for (int i = 0; i < almModel.size(); i++) {
                    listado.add(almModel.get(i).getAuthor() + " - "
                            + almModel.get(i).getDescription() + "\n" + almModel.get(i).getTitle());
                }


                ListaBooksNew = almModel;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        consultarListaBooks();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        try {
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            // asyncTask = new BackgroundTask();
                            //    Void[] params = null;
                            //  asyncTask.execute(params);


                            if (findViewById(R.id.book_detail_container) != null) {

                                mTwoPane = true;
                            } else {
                                View recyclerView = findViewById(R.id.book_list);
                                assert recyclerView != null;
                                setupRecyclerView((RecyclerView) recyclerView);
                                Toast.makeText(getApplicationContext(), "este mensaje", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }


                        }

                    });
        } catch (Exception e) {

        }

/*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
*/

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                //Intent intent = new Intent(BookListNavDraw.this, LoginActivity.class);
                // startActivity(intent);

            }
        });


        if (findViewById(R.id.book_detail_container) != null) {

            fab.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Snackbar.make(view, "Transaccion", Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();


                                           Intent intent = new Intent(getApplicationContext(),WebViewForm.class);
                                           startActivity(intent);

                                       }
            });

            fab.setImageResource(R.drawable.transaccion);
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        if (ListaBooks.size() <= 0) {

            Toast.makeText(this, "Es menor", Toast.LENGTH_SHORT).show();
            // onCreateDialog("Entrar").show();


        } else {
            //   Toast.makeText(this,"Es mayor",Toast.LENGTH_SHORT).show();


        }


        if (getIntent().getExtras() != null) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //notificationManager.cancel(0);
            if (getIntent().getAction() == ACTION_DELETE) {
                Log.d("BookListNavDraw", "bookDelete");

                Toast.makeText(BookListNavDraw.this, "bookDelete",
                        Toast.LENGTH_SHORT).show();
            }

            if (getIntent().getAction() == ACTION_ATTACH_DATA) {
                Log.d("BookListNavDraw", "bookmOSTRAR");

                Toast.makeText(BookListNavDraw.this, "bookmOSTRAR",
                        Toast.LENGTH_SHORT).show();




            }
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("BookListNavDraw", "Key: " + key + "value : " + value);
            }


        }






        // fin onCreate
        
        
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_list_nav_draw, menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(BookListNavDraw.this,"Hola settin",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(BookListNavDraw.this, InicioActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

            /*
            Intent intent = new Intent(Intent.ACTION_SEND);
            Intent chooser = Intent.createChooser(intent, "Compartir con...");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }*/
            compartirImatge();

        } else if (id == R.id.nav_copy) {

            pv_CopiarTxtPortapapeles("label", txvUsuario.getText().toString());

        } else if (id == R.id.nav_send) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,  txvUsuario.getText().toString());
            i.setPackage("com.whatsapp");
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        
    }
    
    // Copiado New

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
                ListaBooksNew=almModel;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }
    // [fin] .- FireBase





    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        try{
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            recyclerView.setAdapter(new BookListNavDraw.SimpleItemRecyclerViewAdapter(BookListNavDraw.this, BookItem.ITEMS, mTwoPane));


                            swipeRefreshLayout.setRefreshing(false);


                           /* Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);*/

                            //  onStop();

                            //finish();
                            //    onRestart();

                        }


                    });

        }catch (Exception e){

        }


        recyclerView.setAdapter(new BookListNavDraw.SimpleItemRecyclerViewAdapter(this, BookItem.ITEMS, mTwoPane));


    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<BookListNavDraw.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final BookListNavDraw mParentActivity;

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

        SimpleItemRecyclerViewAdapter(BookListNavDraw parent,
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
            View viewReturn=viewImpar;





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
        public void onBindViewHolder(final BookListNavDraw.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).sTitulo);
            holder.mContentView.setText(mValues.get(position).sAutor);
            // holder.
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
            //final ImageView mImgView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }




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
        consultarListaBooks();

        final FloatingActionButton fab = findViewById(R.id.fab);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (mTwoPane){
            fab.setImageResource(R.drawable.transaccion);

        }else{
            if (currentUser!=null){

                Toast.makeText(BookListNavDraw.this, "Conectado",
                        Toast.LENGTH_SHORT).show();
                fab.setImageResource(R.drawable.ic_identificacion_verificada);
                imgFoto.setImageResource(R.drawable.ic_identificacion_verificada);
                //txvUsuario.setText(Global.sUsuario);

            }else{
                //   Toast.makeText(BookListNavDraw.this, "DesConectado",
                //        Toast.LENGTH_SHORT).show();
                fab.setImageResource(R.drawable.ic_identificacion_no_verificada);
                imgFoto.setImageResource(R.drawable.ic_identificacion_no_verificada);

            }
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
/*

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
// Esto es lo que hace mi botón al pulsar ir a atrás
            Toast.makeText(getApplicationContext(), "Voy hacia atrás!!",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        this.finish();
        return super.onKeyDown(keyCode, event);
    }
*/





    // Copiar texto en portapapeles
    private void pv_CopiarTxtPortapapeles(String sLabel, String sTexto){

        ClipData clip = ClipData.newPlainText(sLabel, sTexto);
        ClipboardManager clipboard = (ClipboardManager)this.getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(),"Copiado a portapeles \n"+sTexto,Toast.LENGTH_LONG).show();

    }





    // Enviar texto y imagen por sociales
    private void compartirImatge() {


        Uri imatgeAEnviar = prepararImatge();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, txvUsuario.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, imatgeAEnviar);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));

    }


    private Uri prepararImatge() {

        Drawable drawable= getResources().getDrawable(R.drawable.ic_identificacion_verificada);
/*
        if (Global.sUsuario == "Usuario_"){
            drawable = getResources().getDrawable(R.drawable.ic_identificacion_no_verificada);
        }else{
            drawable = getResources().getDrawable(R.drawable.ic_identificacion_verificada);
        }*/
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        File imagePath = new File(getFilesDir(), "temporal");
        imagePath.mkdir();
        File imageFile = new File(imagePath.getPath(), "ic_identificacion_verificada.png");

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return FileProvider.getUriForFile(getApplicationContext(), getPackageName(), imageFile);

    }


}
