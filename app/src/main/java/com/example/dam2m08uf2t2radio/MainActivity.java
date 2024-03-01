package com.example.dam2m08uf2t2radio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String[] nomEmisoras = {"Catalunya Ràdio","Catalunya Informació","Catalunya Mùsica ","Icat FM"};
    private final String[] urlEmisora = {
            "https://shoutcast.ccma.cat/ccma/catalunyaradioHD.mp3",
            "https://shoutcast.ccma.cat/ccma/catalunyainformacioHD.mp3",
            "https://shoutcast.ccma.cat/ccma/catalunyamusicaHD.mp3",
            "https://shoutcast.ccma.cat/ccma/icatHD.mp3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anyadirActionListenerBotones();
    }

    private void anyadirActionListenerBotones() {

        ArrayList<ImageButton> botones = new ArrayList<>();

        ImageButton b1 = findViewById(R.id.button1);
        botones.add(b1);
        ImageButton b2 = findViewById(R.id.button2);
        botones.add(b2);
        ImageButton b3 = findViewById(R.id.button3);
        botones.add(b3);
        ImageButton b4 = findViewById(R.id.button4);
        botones.add(b4);

        for (ImageButton b : botones) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    accionBoton(botones.indexOf(b));
                }
            });

        }

    }

    private void  accionBoton(int num){

        Toast.makeText(this, nomEmisoras[num], Toast.LENGTH_SHORT).show();
        // añadir intent a nueva activity
    }
}