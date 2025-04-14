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

        RecyclerView rvOrder = findViewById(R.id.rvStepOrder);
        rvOrder.setHasFixedSize(true);
        rvOrder.setLayoutManager(new LinearLayoutManager (this));



    }
}