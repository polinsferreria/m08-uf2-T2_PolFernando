// RadioService.java
package com.example.dam2m08uf2t2radio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RadioService extends Service {

    private static final String CHANNEL_ID = "RadioChannel";
    private static final int NOTIFICATION_ID = 123;
    private String STREAM_URL = "";

    private SimpleExoPlayer player;

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
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Radio Channel", NotificationManager.IMPORTANCE_HIGH);
            //NotificationManager manager = getSystemService(NotificationManager.class);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null){
                manager.createNotificationChannel(channel);
            } else {
                System.out.println("--------------------------------------------------------");
            }

        }else{
            System.err.println("No es la version correcta");
        }
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, Reproductor.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                //.setSmallIcon(R.drawable.catalunyam)
                .setContentTitle("Radio en reproducción")
                //.setContentText("Temporitzador de: "+this.seg+" segons.")
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID).
                setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

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
            }
        }
    }
}
