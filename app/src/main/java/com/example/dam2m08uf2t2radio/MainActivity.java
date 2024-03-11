package com.example.dam2m08uf2t2radio;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;
    private Intent intentService;

    private int num = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el launcher dentro del onCreate
        initializeLauncher();
        setupButtonListeners();
    }

    private void initializeLauncher() {
        // Inicializar el launcher en onCreate
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        intentService = result.getData().getParcelableExtra("intent");
                        num = result.getData().getIntExtra("num",50);
                        System.out.println(intentService);
                        System.out.println("result");
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
        intent.putExtra("num",num);
        intent.putExtra("intent", intentService);
        launcher.launch(intent);
    }
}
