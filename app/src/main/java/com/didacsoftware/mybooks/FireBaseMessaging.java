package com.didacsoftware.mybooks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.didacsoftware.mybooks.BookListActivity;
import com.didacsoftware.mybooks.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.Intent.ACTION_ATTACH_DATA;
import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_INSERT;
import static android.content.Intent.ACTION_SEND;

public class FireBaseMessaging extends FirebaseMessagingService {

    String TAG = "Mensaj FB : ";





    // Token
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        // Codigo del originante del mensaje, o remitente
        Log.d(TAG, "From: " + remoteMessage.getFrom());



        String book_position = null;



        // Si llega datos avanzados aparte de la notificacion
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "data" + remoteMessage.getData());


            book_position = remoteMessage.getData().get("book_position").toString();

        }



        // Se ejecuta cuando recibimos el mensaje, como puede ser null hacemos la validacion
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            //Notificacion simple
           // mostrarNotificacion(remoteMessage.getNotification().getTitle(),getApplicationContext());

            //Notificacion expandida
            mostrarNotificacion(remoteMessage.getNotification().getTitle(),book_position,getApplicationContext());


            //remoteMessage.getNotification().getBody()
        }


        if (remoteMessage.getData().size() > 0){

        }
    }




    /*
    // Notificaciones simples
    private void mostrarNotificacion(String title, Context context) {

        Intent intent = new Intent(this, BookListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // sonido al recibir la notificacion
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)

                // Cambiar el icono que se muestra al recivir mensaje
                .setSmallIcon(R.drawable.libros_en_pila)
                .setContentTitle(title)
               // .setContentText(body)
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);


        notificationManager.notify(0, notificationBuilder.build());

    }*/




    // Notificaciones Expandidas
    private void mostrarNotificacion( String title, String book_position, Context context) {

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


}
