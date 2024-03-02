package com.example.dam2m08uf2t2radio;

import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

public class Reproductor extends AppCompatActivity {
    private NotificationManager notificationManager;
    private MediaPlayer player;
    private TextView title;
    private String streamUrl;

    private boolean isPlaying = false;
    private ProgressControl progressControl;
    private ImageView imageView;
    //private Switch bucle; //borrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        //this.bucle = this.findViewById(R.id.bucle); // no hay boton de bucle hay

        Intent intent = this.getIntent();
        this.title = findViewById(R.id.titol);
        this.title.setText(intent.getStringExtra("title"));//nomEmisora
        this.streamUrl = intent.getStringExtra("urlEmisora");
        this.imageView = findViewById(R.id.image);
        this.imageView.setImageDrawable(getDrawable(intent.getIntExtra("image",0)));

        this.player = MediaPlayer.create(this, intent.getIntExtra("audio", 0));
        /*this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Reproductor.this.prepareProgressBar();// no hace falta la linea del reproductro
            }
        });*/


    }
    /*private void prepareProgressBar() {// no hace falta la linea del reproductro
        this.seekBar = this.findViewById(R.id.seekBar);
        this.seekBar.setMax(this.player.getDuration());

    }*/
    private void startMediaPlayer() {//inicia el audio :D
        if (player == null) {
            player = new MediaPlayer();
            try {
                player.setDataSource(streamUrl);
                player.prepare();
                player.start();
                isPlaying = true;
            } catch (IOException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
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
