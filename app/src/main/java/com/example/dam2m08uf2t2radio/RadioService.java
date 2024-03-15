// RadioService.java
package com.example.dam2m08uf2t2radio;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;

public class RadioService extends Service {

    private static final String CHANNEL_ID = "RadioChannel";
    private static final int NOTIFICATION_ID = 1;
    private String STREAM_URL = "";
    private SimpleExoPlayer player;
    private String nom;
    private int numeroEmisora;
    private Intent serviceIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        obtenerDatosIntent(intent);
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
        initializePlayer();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence channelName = "Radio Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);

            // Configure the channel's behavior
            channel.setDescription("Channel description");
            channel.enableLights(false);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


        }else{
            System.err.println("No es la version correcta");

        }
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, Reproductor.class);
        intent.putExtra("Emisora",(Serializable) new EmisoraModelo(numeroEmisora));
        intent.putExtra("num",numeroEmisora);
        intent.putExtra("intent", serviceIntent);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), icono));

        // Definir el tamaño deseado del icono (por ejemplo, 48x48 píxeles)
       // int tamañoIconoPx = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        //bitmapDrawable.setBounds(0, 0, tamañoIconoPx, tamañoIconoPx);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Radio en reproducció "  +  nom)
                .setContentText("Descripción de la transmisión")
                .setSmallIcon(R.drawable.ic_action_icono_radio)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Descripción más detallada de la transmisión"));

        Notification notification = notificationBuilder.build();

        // Agregar logs para verificar la creación de la notificación

        if (notification != null) {
            Log.d("RadioService", "Notificación creada correctamente.");
        } else {
            Log.e("RadioService", "Error al crear la notificación.");
        }

        return notification;
    }


    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this)
                .setLoadControl(new DefaultLoadControl())
                .build();

        MediaSource mediaSource = buildMediaSource(Uri.parse(STREAM_URL));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "RadioApp"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void obtenerDatosIntent(Intent intent) {
        if (intent != null) {
            EmisoraModelo emisora = (EmisoraModelo) intent.getSerializableExtra("emisora");
            if (emisora != null) {
                STREAM_URL = emisora.getUrl();
                //icono = emisora.getDraw();
                nom = emisora.getNom();
                numeroEmisora = emisora.getNum();
                serviceIntent = intent;

            }
        }
    }
}
