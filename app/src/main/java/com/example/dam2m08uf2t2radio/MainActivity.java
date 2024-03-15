package com.example.dam2m08uf2t2radio;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.Manifest;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;
    private Intent intentService;

    private static final int PERMISSION_REQUEST_CODE = 1001;


    private int num = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el launcher dentro del onCreate
        initializeLauncher();
        setupButtonListeners();

        // Verificar si el permiso ya está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está concedido, solicitarlo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE},
                    PERMISSION_REQUEST_CODE);
        } else {
            // El permiso ya está concedido, puedes proceder con la lógica de tu aplicación
            // ...
        }


    }

    private void initializeLauncher() {
        // Inicializar el launcher en onCreate
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        intentService = result.getData().getParcelableExtra("intent");
                        num = result.getData().getIntExtra("num",-1);
                        System.out.println(intentService);
                        System.out.println("result");
                        System.out.println(num);
                    }
                });
    }

    private void setupButtonListeners() {
        ArrayList<ImageButton> buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));

        for (ImageButton button : buttons) {
            button.setOnClickListener(v -> actionButton(buttons.indexOf(button)));
        }
    }

    private void actionButton(int num) {
        EmisoraModelo em = new EmisoraModelo(num);
        Intent intent = new Intent(this, Reproductor.class);
        intent.putExtra("Emisora", em);
        intent.putExtra("num",this.num);
        intent.putExtra("intent", intentService);
        launcher.launch(intent);
    }
}
