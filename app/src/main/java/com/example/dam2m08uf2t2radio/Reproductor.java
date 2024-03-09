package com.example.dam2m08uf2t2radio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class Reproductor extends AppCompatActivity {

    private static final String STREAM_URL = "URL_DEL_STREAM"; // Replace with your stream URL
    private SimpleExoPlayer player;
    private ImageView playPauseButton;
    private TextView title;
    private TextView descripcion;
    private String streamUrl;
    private ProgressDialog progressDialog;

    private boolean estadoReproductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        initializeViews();
        obtenerDatosIntent();
        initializePlayer();
        estadoReproductor = false;

        if (savedInstanceState != null) {
            estadoReproductor = !savedInstanceState.getBoolean("eReproductor");
            // PlayerSerializable ps = (PlayerSerializable) savedInstanceState.getSerializable("player");
            // player = ps.getPlayer();
            togglePlayback();
        }else{
            // initializePlayer();
            // estadoReproductor = false;
        }


        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayback();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("eReproductor",estadoReproductor);
        PlayerSerializable ps = new PlayerSerializable(player);
        outState.putSerializable("player",ps);
    }

    private void initializeViews() {
        title = findViewById(R.id.titol);
        descripcion = findViewById(R.id.descripciontxt);
        playPauseButton = findViewById(R.id.play);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    private void togglePlayback() {

        estadoReproductor = !estadoReproductor;
        // Utilizando el operador ternario para cambiar la imagen del botón según el estado del reproductor
        playPauseButton.setImageResource((estadoReproductor) ?
                R.drawable.baseline_stop_24 : R.drawable.baseline_play_arrow_24);

        if (player.getPlaybackState() == SimpleExoPlayer.STATE_READY) {
            // El reproductor está preparado, se puede iniciar o pausar la reproducción
            player.setPlayWhenReady(estadoReproductor);
        } else {
            // El reproductor no está preparado, se muestra el diálogo de carga
            progressDialog.show();
        }
    }

    private void obtenerDatosIntent() {
        Intent intent = getIntent();
        EmisoraModelo emisora = (EmisoraModelo) intent.getSerializableExtra("Emisora");

        title.setText(emisora.getNom());
        descripcion.setText(emisora.getDescripcio());
        streamUrl = emisora.getUrl();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(new DefaultTrackSelector(this))
                .setLoadControl(new DefaultLoadControl())
                .build();

        MediaSource mediaSource = buildMediaSource(Uri.parse(streamUrl));
        player.prepare(mediaSource);
        player.setPlayWhenReady(estadoReproductor);
        player.addListener(new SimpleExoPlayer.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == SimpleExoPlayer.STATE_READY) {
                    // Dismiss progress dialog when player is ready
                    progressDialog.dismiss();
                    estadoReproductor = !estadoReproductor;
                    togglePlayback();
                }
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, "user-agent");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
