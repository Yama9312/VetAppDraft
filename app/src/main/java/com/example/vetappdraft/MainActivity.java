package com.example.vetappdraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private Spinner mSpinChoice;
    public String sBranch;
    private Button btnSubmit;
    private String eContact;

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
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xFFF6F1E6,0xFFFFC107}
        );
        getWindow().getDecorView().setBackground(gradientDrawable);

        Intent intent= new Intent(this, ChangeStepOrder.class);
        btnSubmit = findViewById(R.id.btnSubBranch);
        DatabaseHelper VetDB = new DatabaseHelper(this);

        btnSubmit.setOnClickListener( (view) -> {
            //sBranch = mSpinChoice.getSelectedItem().toString();
            //eContact = findViewById(R.id.phEContact1).toString();
            //VetDB.insertUser(eContact, sBranch);
            startActivity(intent);
        });

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