package com.example.vetappdraft;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Spinner mSpinChoice;
    private String sBranch;
    private Button btnSubmit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnSubmit = findViewById(R.id.btnSubBranch);

        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sBranch = mSpinChoice.getSelectedItem().toString();
                    }
                }
        );


        String[] choiceArray = new String[] {
                "Army", "Marine Corps", "Navy", "Air Force"
        };
        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, choiceArray);
        choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinChoice = (Spinner) findViewById(R.id.spnBranch);
        mSpinChoice.setAdapter(choiceAdapter);
    }
}