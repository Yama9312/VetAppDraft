package com.example.vetappdraft;

import android.annotation.SuppressLint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;

public class ChangeStepOrder extends BaseActivity {
    public ArrayList<Page> theSteps;
    private RecyclerView recyclerView;

    private StepAdapter mcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_step_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        theSteps = new ArrayList<Page> ();
        theSteps.add(new Page("Step1", Page.PageType.TEXT, "take a deep breath", ""));
//        theSteps.add(new Page("Step1", step1.class, "no", "take a deep breath"));
//        theSteps.add(new Page("Step2", step1.class, "no", "Review reasons for living"));
//        theSteps.add(new Page("Step3", step1.class, "no", "H.A.L.T"));
//        theSteps.add(new Page("Step4", step1.class, "no", "Practice Breathing Skills"));
//        theSteps.add(new Page("Step5", step1.class, "no", "Identify current thoughts, emotions, and body sensations"));
//        theSteps.add(new Page("Step6", step1.class, "no", "Do Body Scan Meditation"));
//        theSteps.add(new Page("Step7", step1.class, "no", "Listen to Music"));
//        theSteps.add(new Page("Step8", step1.class, "no", "Call Someone / See who is online"));
//        theSteps.add(new Page("Step9", step1.class, "no", "Call Hotlines"));
//        theSteps.add(new Page("Step10", step1.class, "no", "Put down the weapon and put both hands on the phone"));

        RecyclerView rvOrder = findViewById(R.id.rvStepOrder);
        rvOrder.setHasFixedSize(true);
        rvOrder.setLayoutManager(new LinearLayoutManager (this));



    }
}