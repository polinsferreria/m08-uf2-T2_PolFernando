package com.example.dam2m08uf2t2radio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

public class Reproductor extends AppCompatActivity {

    private ImageView playPauseButton;
    private boolean isServiceRunning = false;
    private TextView title;
    private TextView descripcion;

    private EmisoraModelo em;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        title = findViewById(R.id.titol);
        descripcion = findViewById(R.id.descripciontxt);

        obtenerDatosIntent();

        playPauseButton = findViewById(R.id.play);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent((Reproductor.this),RadioService.class);
                i.putExtra("emisora", em);

                if (isServiceRunning) {
                    stopService(i);
                } else {
                    startService(i);
                }
                isServiceRunning = !isServiceRunning;
                updateButtonIcon();
            }
        });
    }

    private void updateButtonIcon() {
        playPauseButton.setImageResource(isServiceRunning ? R.drawable.baseline_stop_24 : R.drawable.baseline_play_arrow_24);
    }

    private void obtenerDatosIntent() {
        Intent intent = getIntent();
        em = (EmisoraModelo) intent.getSerializableExtra("Emisora");

        title.setText(em.getNom());
        descripcion.setText(em.getDescripcio());

    }
}
