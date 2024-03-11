// Reproductor.java
package com.example.dam2m08uf2t2radio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Reproductor extends AppCompatActivity {
    private ImageView playPauseButton;
    private ImageView back;
    private boolean isServiceRunning = false;
    private TextView title;
    private TextView description;
    private EmisoraModelo em;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        title = findViewById(R.id.titol);
        description = findViewById(R.id.descripciontxt);



        playPauseButton = findViewById(R.id.play);
        playPauseButton.setOnClickListener(v -> toggleService());

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> setResultAndFinish());

        obtainIntentData();

    }

    private void toggleService() {
        if (isServiceRunning) {
            stopService();
        } else {
            startService();
        }
    }

    private void startService() {

        if (serviceIntent != null){
            stopService();
            serviceIntent = null;
            startService();

        } else{
            serviceIntent = new Intent(Reproductor.this, RadioService.class);
            serviceIntent.putExtra("emisora", em);
            startService(serviceIntent);
            isServiceRunning = true;
            updateButtonIcon();
        }

    }

    private void stopService() {
        stopService(serviceIntent);
        isServiceRunning = false;
        updateButtonIcon();
    }

    private void updateButtonIcon() {
        playPauseButton.setImageResource(isServiceRunning ? R.drawable.baseline_stop_24 : R.drawable.baseline_play_arrow_24);
    }

    private void obtainIntentData() {
        Intent intent = getIntent();
        em = (EmisoraModelo) intent.getSerializableExtra("Emisora");
        title.setText(em.getNom());
        description.setText(em.getDescripcio());
        serviceIntent = intent.getParcelableExtra("intent");

        if (serviceIntent != null &&
                intent.getIntExtra("num",50) == em.getNum()){
            isServiceRunning = true;
            updateButtonIcon();
        }
        System.out.println(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        setResultAndFinish();
        super.onDestroy();
    }

    private void setResultAndFinish() {
        Intent resultIntent = new Intent();
        if (isServiceRunning) {
            resultIntent.putExtra("intent", serviceIntent);
            resultIntent.putExtra("num", em.getNum());
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }
}
