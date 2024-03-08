package com.example.dam2m08uf2t2radio;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class Reproductor extends AppCompatActivity {

    private static final String TAG = "Reproductor";

    private MediaPlayer player;
    private TextView title;
    private TextView descripcion;
    private String streamUrl;
    private ImageView play;
    private AudioManager audioManager;
    private SeekBar volumeControl;
    private boolean isPrepared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        // Inicializar vistas
        initializeViews();

        // Obtener datos de la intención
        obtenerDatosIntent();
    }

    private void initializeViews() {
        title = findViewById(R.id.titol);
        descripcion = findViewById(R.id.descripciontxt);
        play = findViewById(R.id.play);
        volumeControl = findViewById(R.id.volumeControl);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayback();
            }
        });

        // Configurar control de volumen
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeControl.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void obtenerDatosIntent() {
        Intent intent = getIntent();
        EmisoraModelo emisora = (EmisoraModelo) intent.getSerializableExtra("Emisora");

        title.setText(emisora.getNom());
        descripcion.setText(emisora.getDescripcio());
        streamUrl = emisora.getUrl();
    }

    private void togglePlayback() {
        if (player != null && player.isPlaying()) {
            player.pause();
            play.setImageResource(R.drawable.baseline_play_arrow_24); // Cambiar icono a reproducir
        } else {
            try {
                if (player == null) {
                    initializePlayer();
                } else {
                    if (isPrepared) {
                        player.start(); // Reanudar la reproducción si está pausada
                    }
                }
                play.setImageResource(R.drawable.baseline_stop_24); // Cambiar icono a pausa
            } catch (IOException e) {
                Log.e(TAG, "Error initializing player: " + e.getMessage());
                Toast.makeText(this, "Error al inicializar el reproductor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializePlayer() throws IOException {
        player = new MediaPlayer();
        player.setDataSource(streamUrl);
        player.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "MediaPlayer está preparado para reproducir.");
                isPrepared = true;
                mp.start(); // Iniciar la reproducción aquí una vez que el reproductor esté preparado
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                // Manejar la actualización de buffering si es necesario
                if (!isPrepared) {
                    // Si aún no está preparado, iniciar la reproducción cuando el búfer esté listo
                    isPrepared = true;
                    try {
                        player.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "Error durante la reproducción - what: " + what + ", extra: " + extra);
                mp.reset();
                return false;
            }
        });
        player.prepareAsync(); // Iniciar la preparación asíncrona del reproductor
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMediaPlayer();
    }

    private void stopMediaPlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}

