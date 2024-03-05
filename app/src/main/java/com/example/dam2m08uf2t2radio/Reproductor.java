package com.example.dam2m08uf2t2radio;

import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.io.Serializable;

public class Reproductor extends AppCompatActivity {
    private NotificationManager notificationManager;
    private MediaPlayer player;
    private TextView title;
    private TextView descripcion;
    private String streamUrl;

    private boolean isPlaying = false;
    private ProgressControl progressControl;
    private ImageView imageView;
    private Button play;
    private Button stop;
    //private Switch bucle; //borrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        //this.bucle = this.findViewById(R.id.bucle); // no hay boton de bucle hay

        Intent intent = this.getIntent();
        EmisoraModelo emisora = (EmisoraModelo) intent.getSerializableExtra("Emisora");

        this.title = findViewById(R.id.titol);
        this.title.setText(emisora.getNom());//nomEmisora
        this.descripcion = findViewById(R.id.descripciontxt);
        this.descripcion.setText(emisora.getDescripcio());//nomEmisora
        this.streamUrl = emisora.getUrl();
        this.imageView = findViewById(R.id.image);
        this.stop = findViewById(R.id.stop_button);
        this.play = findViewById(R.id.play_button);
   //     this.imageView.setImageDrawable(getDrawable(intent.getIntExtra("image",0)));

//        this.player = MediaPlayer.create(this, intent.getIntExtra("audio", 0));
        /*this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Reproductor.this.prepareProgressBar();// no hace falta la linea del reproductro
            }
        });*/

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startMediaPlayer();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopMediaPlayer();
            }
        });
    }
    /*private void prepareProgressBar() {// no hace falta la linea del reproductro
        this.seekBar = this.findViewById(R.id.seekBar);
        this.seekBar.setMax(this.player.getDuration());

    }*/

    private void startMediaPlayer() {
        if (player == null) {
            player = new MediaPlayer();
            try {
                player.setDataSource(streamUrl);
                player.prepare();
                player.start();
                isPlaying = true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MediaPlayer", "Error setting data source: " + e.getMessage());
                Toast.makeText(this, "Error al reproduir la ràdio", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MediaPlayer", "Error desconegut: " + e.getMessage());
                Toast.makeText(this, "Error desconegut", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopMediaPlayer() {//para el audio :D
        if (player != null) {
            player.release();
            player = null;
            isPlaying = false;
        }
    }
//mas metodos

    class ProgressControl extends Thread{//es un copia y pega del ejemplo media player hay q mirarlo
        private SeekBar seekBar;
        private MediaPlayer mediaPlayer;
        private boolean fin = false;

        public ProgressControl(SeekBar seekBar, MediaPlayer mediaPlayer){
            this.seekBar = seekBar;
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        public void run(){
            try {Thread.sleep(100);} catch (InterruptedException e) {}
            while (!this.fin && this.mediaPlayer!=null && this.mediaPlayer.isPlaying()){
                this.seekBar.setProgress(this.mediaPlayer.getCurrentPosition());
                try {Thread.sleep(100);} catch (InterruptedException e) {}
            }
        }

        public void finish() {
            this.fin = true;
        }
    }//------------------------------------es un copia y pega del ejemplo media player hay q mirarlo
}
