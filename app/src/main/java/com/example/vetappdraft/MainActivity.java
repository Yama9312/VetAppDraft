package com.example.vetappdraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    private Spinner mSpinChoice;
    public String sBranch;
    private Button btnSubmit;
    private String eContact;
    private VetDatabase mcDB;
    private VetDAO mcDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mcDB = Room.databaseBuilder (getApplicationContext (),
            VetDatabase.class, "VET-DB").build();
        mcDAO = mcDB.vetDAO ();


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent= new Intent(this, ChangeStepOrder.class);

        new Thread(() -> {
            int userCount = mcDAO.getSize();

            runOnUiThread(() -> {
                if (userCount > 0) {
                    startActivity(intent);
                }
            });
        }).start();

        btnSubmit = findViewById(R.id.btnSubBranch);

        btnSubmit.setOnClickListener( (view) -> {
            sBranch = mSpinChoice.getSelectedItem().toString();
            TextView Contact= findViewById(R.id.phEContact1);
            eContact= Contact.getText ().toString ();
            Executors.newSingleThreadExecutor().execute(() -> {
                VetUser newUser = new VetUser (sBranch, eContact);
                mcDAO.insert(newUser);
            });
            startActivity(intent);
        });

        // initializing spinner
        String[] choiceArray = new String[] {
                "Army", "Marine Corps", "Navy", "Air Force"
        };
        ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, choiceArray);
        choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinChoice = (Spinner) findViewById(R.id.spnBranch);
        mSpinChoice.setAdapter(choiceAdapter);

        // spinner with branches
        mSpinChoice.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener () {
                    @Override
                    public void onItemSelected (AdapterView<?> adapterView,
                                                View view, int i, long l) {
                        String selectedBranch = (String) adapterView.getItemAtPosition (i);

                        getSharedPreferences("AppPreferences", MODE_PRIVATE)
                                .edit()
                                .putString("selectedBranch", selectedBranch)
                                .apply();

                        updateColorScheme(selectedBranch);
                    }

                    private void updateColorScheme(String selectedBranch) {
                        // change color scheme depending on choice of branch
                        int[] colors;
                        switch(selectedBranch) {
                            case "Army":
                                colors = new int[]{0xFFF6F1E6,0xFFFFC107};
                                break;
                            case "Marine Corps":
                                colors = new int[]{0xFFF4DF19, 0xFFF50001};
                                break;
                            case "Navy":
                                colors = new int[]{0xFFFFFF06,0xFF00007B};
                                break;
                            case "Air Force":
                                colors = new int[]{0xFFF8FBFA,0xFF144D80};
                                break;
                            default:
                                colors = new int[]{0xFFFFFFFF,0xFFFFFFFF};
                                break;
                        }

                        GradientDrawable gradientDrawable = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                colors
                        );

                        getWindow().getDecorView().setBackground(gradientDrawable);
                    }

                    @Override
                    public void onNothingSelected (AdapterView<?>
                                                           adapterView) {
                        // first option by default
                    }
                }
        );
    }
}