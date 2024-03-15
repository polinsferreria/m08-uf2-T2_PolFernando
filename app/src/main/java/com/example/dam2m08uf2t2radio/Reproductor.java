// Reproductor.java
package com.example.dam2m08uf2t2radio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        obtainIntentData(savedInstanceState == null);

        if (savedInstanceState != null){
            isServiceRunning = (boolean) savedInstanceState.get("isService");
            serviceIntent = (Intent) savedInstanceState.get("intent");
        }
        updateButtonIcon();
    }

    private void toggleService() {
        if (isServiceRunning) {
            stopService();
        } else {
            startService();
        }
    }

    private void startService() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

        serviceIntent = new Intent(Reproductor.this, RadioService.class);
        serviceIntent.putExtra("emisora", em);
        startService(serviceIntent);
        isServiceRunning = true;
        updateButtonIcon();
    }

    private void stopService() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
            isServiceRunning = false;
            updateButtonIcon();
        }
    }

    private void updateButtonIcon() {
        playPauseButton.setImageResource(isServiceRunning ? R.drawable.baseline_stop_24 : R.drawable.baseline_play_arrow_24);
    }

    private void obtainIntentData(boolean save) {
        Intent intent = getIntent();
        em = (EmisoraModelo) intent.getSerializableExtra("Emisora");
        title.setText(em.getNom());
        description.setText(em.getDescripcio());

        if (save) {
            serviceIntent = intent.getParcelableExtra("intent");
            if (serviceIntent != null && intent.getIntExtra("num", -1) == em.getNum()) {
                System.out.println(intent.getIntExtra("num",-1)  + " " + em.getNum());
                System.out.println("hola");
                isServiceRunning = true;
            } else{
                isServiceRunning = false;

            }
        }
        updateButtonIcon();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el servicio cuando la actividad se destruye
        stopServiceIfNeeded();
    }

    private void stopServiceIfNeeded() {
        if (!isChangingConfigurations() && !isFinishing() && isServiceRunning) {
            stopService(serviceIntent);
        }
    }

    private void setResultAndFinish() {
        Intent resultIntent = new Intent();

        setResult(RESULT_OK, resultIntent);
        if (isServiceRunning) {
            resultIntent.putExtra("intent", serviceIntent);
            resultIntent.putExtra("num", em.getNum());
        }else{
            resultIntent.putExtra("num", -1);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResultAndFinish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isService", isServiceRunning);
        outState.putParcelable("intent", serviceIntent);
    }


}
