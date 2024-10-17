package com.example.vetappdraft;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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
    private ActivityManager activityManager;
    private PageAdapter adapter;

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

        activityManager = new ActivityManager();
        activityManager.addPage(new Page("Step 1", Step1.class, "", "hello world"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PageAdapter(activityManager.getPages(), this);
        recyclerView.setAdapter(adapter);


        btnSubmit = findViewById(R.id.btnSubBranch);
        DatabaseHelper VetDB = new DatabaseHelper(this);
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sBranch = mSpinChoice.getSelectedItem().toString();
                        eContact = findViewById(R.id.phEContact1).toString();
                        VetDB.insertUser(eContact, sBranch);
                        // send to next step
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